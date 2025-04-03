
package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Customer;

@GuiService
public class AuthenticatedCustomerUpdateService extends AbstractGuiService<Authenticated, Customer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedCustomerRepository authenticatedCustomerRepository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customer object;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.authenticatedCustomerRepository.findCustomerByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Customer object) {
		assert object != null;

		super.bindObject(object, "customerIdentifier", "customerPhoneNumber", "physicalAddress", "city", "country", "earnedPoints");
	}

	@Override
	public void validate(final Customer object) {
		assert object != null;
		Customer existing = this.authenticatedCustomerRepository.findCustomerByCustomerIdentifier(object.getCustomerIdentifier());
		boolean valid = existing == null || existing.getId() == object.getId();
		super.state(valid, "identifier", "authenticated.customer.form.error.duplicateIdentifier");
	}

	@Override
	public void perform(final Customer object) {
		assert object != null;

		this.authenticatedCustomerRepository.save(object);
	}

	@Override
	public void unbind(final Customer object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "customerIdentifier", "customerPhoneNumber", "physicalAddress", "city", "country", "earnedPoints");
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
