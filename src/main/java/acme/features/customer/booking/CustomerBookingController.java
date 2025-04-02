
package acme.features.customer.booking;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiController
public class CustomerBookingController extends AbstractGuiController<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingListService		customerBookingListService;

	@Autowired
	private CustomerBookingShowService		customerBookingShowService;

	@Autowired
	private CustomerBookingCreateService	customerBookingCreateService;

	@Autowired
	private CustomerBookingUpdateService	customerBookingUpdateService;

	@Autowired
	private CustomerBookingPublishService	customerBookingPublishService;

	@Autowired
	private CustomerBookingDeleteService	customerBookingDeleteService;


	// Constructors -----------------------------------------------------------
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		dateFormat.setLenient(false);
		// Configuramos para convertir a java.util.Date (lo que luego Hibernate normalmente mapea a java.sql.Timestamp)
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.customerBookingListService);
		super.addBasicCommand("show", this.customerBookingShowService);
		super.addBasicCommand("create", this.customerBookingCreateService);
		super.addBasicCommand("update", this.customerBookingUpdateService);
		super.addCustomCommand("publish", "update", this.customerBookingPublishService);
		super.addBasicCommand("delete", this.customerBookingDeleteService);
	}

}
