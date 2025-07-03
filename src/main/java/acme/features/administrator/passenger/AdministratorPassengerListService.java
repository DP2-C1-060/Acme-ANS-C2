
package acme.features.administrator.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Passenger;

@GuiService
public class AdministratorPassengerListService extends AbstractGuiService<Administrator, Passenger> {

	@Autowired
	private AdministratorPassengerRepository administratorPassengerRepository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;

		int bookingId = super.getRequest().getData("bookingId", int.class);
		passengers = this.administratorPassengerRepository.getPassengersFromBookingId(bookingId);

		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {
		assert passenger != null;

		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "passportNumber", "birthDate", "isPublished");

		super.getResponse().addData(dataset);
	}

}
