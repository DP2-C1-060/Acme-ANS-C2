
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flights.Flight;

@GuiService
public class AdministratorBookingShowService extends AbstractGuiService<Administrator, Booking> {

	@Autowired
	private AdministratorBookingRepository administratorBookingRepository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id = super.getRequest().getData("id", int.class);

		booking = this.administratorBookingRepository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		assert booking != null;
		Dataset dataset;
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Collection<Flight> flights = this.administratorBookingRepository.findAllFlights();
		SelectChoices flightChoices = SelectChoices.from(flights, "id", booking.getFlight());

		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "price", "lastNibble", "isPublished", "id");
		dataset.put("travelClasses", travelClasses);
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

}
