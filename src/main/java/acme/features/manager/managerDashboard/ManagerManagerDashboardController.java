
package acme.features.manager.managerDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.manager.ManagerDashboard;
import acme.realms.manager.Manager;

@GuiController
public class ManagerManagerDashboardController extends AbstractGuiController<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ManagerManagerDashboardShowService showService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
