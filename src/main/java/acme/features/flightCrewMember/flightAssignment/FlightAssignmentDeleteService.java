
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.activityLogs.ActivityLogRepository;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.FlightAssignment;
import acme.entities.assignments.FlightCrewDuty;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightAssignmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository	repository;

	@Autowired
	private ActivityLogRepository		activityLogRepository;


	@Override
	public void authorise() {
		boolean status;
		int flightAssignmentId;
		FlightAssignment flightAssignment;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		status = flightAssignment.isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;

		flightAssignment = new FlightAssignment();

		flightAssignment.setDraftMode(true);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		Integer legId;
		Leg leg;

		Integer memberId;
		FlightCrewMember member;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		memberId = super.getRequest().getData("member", int.class);
		member = this.repository.findMemberById(memberId);

		super.bindObject(flightAssignment, "duty", "moment", "assignmentStatus", "remarks");
		flightAssignment.setLeg(leg);
		flightAssignment.setMember(member);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {

	}
	private boolean legIsCompatible(final Leg legToIntroduce, final Leg legInTheDB) {
		boolean departureIncompatible = MomentHelper.isInRange(legToIntroduce.getScheduledDeparture(), legInTheDB.getScheduledDeparture(), legInTheDB.getScheduledArrival());
		boolean arrivalIncompatible = MomentHelper.isInRange(legToIntroduce.getScheduledArrival(), legInTheDB.getScheduledDeparture(), legInTheDB.getScheduledArrival());
		return !departureIncompatible && !arrivalIncompatible;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		Collection<ActivityLog> activityLogs = this.repository.getActivityLogByFlightAssignmentId(flightAssignment.getId());

		this.activityLogRepository.deleteAll(activityLogs);
		this.repository.delete(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		SelectChoices assignmentStatus;
		SelectChoices duty;

		int memberId;
		Collection<Leg> legs;
		SelectChoices legChoices;

		Collection<FlightCrewMember> members;
		SelectChoices memberChoices;
		Dataset dataset;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		//legs = this.repository.findLegsByMemberId(memberId);
		legs = this.repository.findAllLegs();
		members = this.repository.findAllAvailableMembers();

		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		memberChoices = SelectChoices.from(members, "employeeCode", flightAssignment.getMember());

		assignmentStatus = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		duty = SelectChoices.from(FlightCrewDuty.class, flightAssignment.getDuty());

		dataset = super.unbindObject(flightAssignment, "duty", "moment", "assignmentStatus", "remarks", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("moment", MomentHelper.getBaseMoment());
		dataset.put("assignmentStatus", assignmentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("member", memberChoices.getSelected().getKey());
		dataset.put("members", memberChoices);

		super.getResponse().addData(dataset);
	}

}
