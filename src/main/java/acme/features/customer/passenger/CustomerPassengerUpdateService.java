
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerUpdateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository customerPassengerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		Integer passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.customerPassengerRepository.getPassengerById(passengerId);
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = status && passenger.getCustomer().getId() == customerId;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Passenger passenger = this.customerPassengerRepository.getPassengerById(id);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {

	}

	@Override
	public void perform(final Passenger passenger) {
		this.customerPassengerRepository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds", "isPublished");
		super.getResponse().addData(dataset);
	}

}
