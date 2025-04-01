
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository customerBookingRecordRepository;


	@Override
	public void authorise() {
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.customerBookingRecordRepository.getBookingById(bookingId);

		Integer passengerId = super.getRequest().getData("passengerId", int.class);
		Passenger passenger = this.customerBookingRecordRepository.getPassengerById(passengerId);

		status = booking != null && passenger != null && customerId == booking.getCustomer().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.customerBookingRecordRepository.getBookingById(bookingId);
		BookingRecord BookingRecord = new BookingRecord();
		BookingRecord.setBooking(booking);
		super.getBuffer().addData(BookingRecord);
	}

	@Override
	public void bind(final BookingRecord BookingRecord) {
		super.bindObject(BookingRecord, "passenger", "booking");
	}

	@Override
	public void validate(final BookingRecord BookingRecord) {

	}

	@Override
	public void perform(final BookingRecord BookingRecord) {
		this.customerBookingRecordRepository.save(BookingRecord);
	}

	@Override
	public void unbind(final BookingRecord BookingRecord) {
		assert BookingRecord != null;

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Integer bookingId = super.getRequest().getData("bookingId", int.class);

		Collection<Passenger> alreadyAddedPassengers = this.customerBookingRecordRepository.getPassengersInBooking(bookingId);
		Collection<Passenger> noAddedPassengers = this.customerBookingRecordRepository.getAllPassengersByCustomerId(customerId).stream().filter(p -> !alreadyAddedPassengers.contains(p)).toList();
		SelectChoices passengerChoices = SelectChoices.from(noAddedPassengers, "fullName", BookingRecord.getPassenger());

		Dataset dataset = super.unbindObject(BookingRecord, "passenger", "booking");
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);

	}

}
