
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
	private CompletedFlightAssignmentListService		completedListService;

	@Autowired
	private FlightAssignmentNotCompletedListService		notCompletedListService;
	@Autowired
	private FlightAssignmentMyCompletedListService		myCompletedListService;

	@Autowired
	private FlightAssignmentMyNotCompletedListService	myNotCompletedListService;

	@Autowired
	private FlightAssignmentCreateService				createService;

	@Autowired
	private FlightAssignmentUpdateService				updateService;

	@Autowired
	private FlightAssignmentPublishService				publishService;

	@Autowired
	private FlightAssignmentShowService					showService;

	@Autowired
	private FlightAssignmentDeleteService				deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("completedlist", "list", this.completedListService);
		super.addCustomCommand("notCompletedlist", "list", this.notCompletedListService);
		super.addCustomCommand("myCompletedList", "list", this.myCompletedListService);
		super.addCustomCommand("myNotCompletedList", "list", this.myNotCompletedListService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
