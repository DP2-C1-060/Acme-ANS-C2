
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);
		boolean correctMember = activityLog.getFlightAssignment().getMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean flightAssignmentPublished = !activityLog.getFlightAssignment().isDraftMode();
		boolean inPast = MomentHelper.isPast(activityLog.getFlightAssignment().getLeg().getScheduledArrival());

		boolean status = correctMember && flightAssignmentPublished && inPast;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		FlightAssignment flightAssignment;
		final boolean showCreate;

		flightAssignment = activityLog.getFlightAssignment();

		dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "description", "severity", "draftMode");

		boolean inPast = MomentHelper.isPast(flightAssignment.getLeg().getScheduledArrival());
		boolean correctMember = super.getRequest().getPrincipal().getActiveRealm().getId() == flightAssignment.getMember().getId();
		showCreate = !flightAssignment.isDraftMode() && activityLog.isDraftMode() && inPast && correctMember;

		dataset.put("masterId", activityLog.getFlightAssignment().getId());
		dataset.put("buttonsAvaiable", showCreate);
		super.getResponse().addData(dataset);

	}

}
