
package acme.features.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.assignments.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiController
public class FlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentCreateService			createService;

	@Autowired
	private FlightAssignmentShowService				showService;

	@Autowired
	private CompletedFlightAssignmentListService	listCompletedService;

	@Autowired
	private PlannedFlightAssignmentListService		listPlannedService;

	@Autowired
	private FlightAssignmentUpdateService			updateService;

	@Autowired
	private FlightAssignmentPublishService			publishService;

	@Autowired
	private FlightAssignmentDeleteService			deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);

		super.addCustomCommand("list-completed", "list", this.listCompletedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
	}
}
