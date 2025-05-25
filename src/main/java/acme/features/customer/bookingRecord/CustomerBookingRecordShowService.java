
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.BookingRecord;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordShowService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository customerBookingRecordRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		Integer bookingRecordId = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecord = this.customerBookingRecordRepository.getBookingRecordByBookingRecordId(bookingRecordId);

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = status && bookingRecord.getBooking().getCustomer().getId() == customerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecords = this.customerBookingRecordRepository.getBookingRecordByBookingRecordId(id);
		super.getBuffer().addData(bookingRecords);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Boolean publishedBooking = bookingRecord.getBooking().getIsPublished();
		Dataset dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		dataset.put("bookingLocator", bookingRecord.getBooking().getLocatorCode());
		dataset.put("passengerFullName", bookingRecord.getPassenger().getFullName());
		dataset.put("publishedBooking", publishedBooking);
		super.getResponse().addData(dataset);
	}

}
