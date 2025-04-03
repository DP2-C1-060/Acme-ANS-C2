
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignment flightAssignment;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		boolean correctMember = flightAssignment.getMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		status = flightAssignment != null && correctMember;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Collection<ActivityLog> activityLog;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		activityLog = this.repository.findActivityLogsByMasterId(masterId);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "description", "severity");
		super.addPayload(dataset, activityLog, "registrationMoment", "incidentType");

		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<ActivityLog> activityLog) {
		int masterId;
		FlightAssignment flightAssignment;
		final boolean showCreate;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		boolean inPast = MomentHelper.isPast(flightAssignment.getLeg().getScheduledArrival());
		boolean correctMember = super.getRequest().getPrincipal().getActiveRealm().getId() == flightAssignment.getMember().getId();
		showCreate = !flightAssignment.isDraftMode() && inPast && correctMember;

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);

	}

}
