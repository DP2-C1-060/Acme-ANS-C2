
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.BookingRecord;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository customerBookingRecordRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean pre = "POST".equalsIgnoreCase(super.getRequest().getMethod()) && super.getRequest().getPrincipal().hasRealmOfType(Customer.class) && super.getRequest().getData().containsKey("id");

		if (pre) {

			int recordId = super.getRequest().getData("id", int.class);
			BookingRecord br = this.customerBookingRecordRepository.getBookingRecordByBookingRecordId(recordId);

			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

			pre = br != null && br.getBooking().getCustomer().getId() == customerId;
		}

		super.getResponse().setAuthorised(pre);

	}

	@Override
	public void load() {
		int bookingRecordId = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecord = this.customerBookingRecordRepository.getBookingRecordByBookingRecordId(bookingRecordId);

		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {

	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		//		Booking booking = this.customerBookingRecordRepository.getBookingFromBookingRecord(bookingRecord.getBooking().getId());
		//		boolean status;
		//		status = booking.getPublished();
		//		super.state(!status, "*", "customer.booking.form.error.existingRecord");

	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.customerBookingRecordRepository.delete(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {

	}

}
