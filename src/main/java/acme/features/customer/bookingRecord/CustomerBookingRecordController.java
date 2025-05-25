
package acme.features.customer.bookingRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.BookingRecord;
import acme.realms.Customer;

@GuiController
public class CustomerBookingRecordController extends AbstractGuiController<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordCreateService	customerBookingRecordCreateService;

	@Autowired
	private CustomerBookingRecordListService	customerBookingRecordListService;

	@Autowired
	private CustomerBookingRecordShowService	customerBookingRecordShowService;

	@Autowired
	private CustomerBookingRecordDeleteService	customerBookingRecordDeleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.customerBookingRecordListService);
		super.addBasicCommand("create", this.customerBookingRecordCreateService);
		super.addBasicCommand("show", this.customerBookingRecordShowService);
		super.addBasicCommand("delete", this.customerBookingRecordDeleteService);

	}
}
