
package acme.features.authenticated.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.manager.Manager;

@GuiService
public class AuthenticatedManagerUpdateService extends AbstractGuiService<Authenticated, Manager> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedManagerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Manager object;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.repository.findOneManagerByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Manager manager) {
		super.bindObject(manager, "identifier", "yearsOfExperience", "dateOfBirth", "pictureUrl", "airline");
	}

	@Override
	public void validate(final Manager manager) {
		// Aquí puedes agregar validaciones personalizadas si es necesario.
	}

	@Override
	public void perform(final Manager manager) {
		this.repository.save(manager);
	}

	@Override
	public void unbind(final Manager manager) {
		Dataset dataset;

		dataset = super.unbindObject(manager, "identifier", "yearsOfExperience", "dateOfBirth", "pictureUrl", "airline");

		List<Airline> airlines = this.repository.findAirlines();
		SelectChoices airlinesChoices = SelectChoices.from(airlines, "IATAcode", manager.getAirline());
		dataset.put("airlines", airlinesChoices);
		dataset.put("airline", airlinesChoices.getSelected().getKey());
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
