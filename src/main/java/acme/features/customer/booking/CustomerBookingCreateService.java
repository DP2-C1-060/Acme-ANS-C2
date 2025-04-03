
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository customerBookingRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking = new Booking();

		AbstractRealm principal = super.getRequest().getPrincipal().getActiveRealm();
		int customerId = principal.getId();
		Customer customer = this.customerBookingRepository.findCustomerById(customerId);

		Date date = MomentHelper.getCurrentMoment();

		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		int longitud = 6 + random.nextInt(3);
		StringBuilder sb = new StringBuilder(longitud);
		for (int i = 0; i < longitud; i++) {
			char c = chars.charAt(random.nextInt(chars.length()));
			sb.append(c);
		}

		booking.setCustomer(customer);
		booking.setPurchaseMoment(date);
		booking.setIsPublished(false);
		booking.setLocatorCode(sb.toString());

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		try {
			super.bindObject(booking, "flight", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");
			if (booking.getPurchaseMoment() != null)
				System.out.println("purchaseMoment class: " + booking.getPurchaseMoment().getClass());
			else
				System.out.println("purchaseMoment is null");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void validate(final Booking booking) {

	}

	@Override
	public void perform(final Booking booking) {
		Date today = MomentHelper.getCurrentMoment();
		booking.setPurchaseMoment(today);

		this.customerBookingRepository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.customerBookingRepository.findAllFlight();

		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "isPublished", "id");
		dataset.put("travelClass", travelClasses);

		if (!flights.isEmpty()) {
			SelectChoices flightChoices = SelectChoices.from(flights, "flightSummary", booking.getFlight());
			dataset.put("flights", flightChoices);
		}

		super.getResponse().addData(dataset);
	}

}
