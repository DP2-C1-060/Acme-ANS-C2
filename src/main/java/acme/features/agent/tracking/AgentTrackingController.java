
package acme.features.agent.tracking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.tracking.Tracking;
import acme.realms.agent.Agent;

@GuiController
public class AgentTrackingController extends AbstractGuiController<Agent, Tracking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AgentTrackingListService	listService;

	@Autowired
	private AgentTrackingShowService	showService;

	@Autowired
	private AgentTrackingCreateService	createService;

	@Autowired
	private AgentTrackingUpdateService	updateService;

	@Autowired
	private AgentTrackingDeleteService	deleteService;

	@Autowired
	private AgentTrackingPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
