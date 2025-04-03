
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.BookingRecord;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordListService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository customerBookingRecordRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<BookingRecord> bookingRecords = this.customerBookingRecordRepository.getBookingRecordsByCustomerId(customerId);
		super.getBuffer().addData(bookingRecords);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		super.getResponse().addData(dataset);
	}

}
