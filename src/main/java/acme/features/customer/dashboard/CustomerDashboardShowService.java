
package acme.features.customer.dashboard;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void load() {
		Integer customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> bookings = this.repository.findAllBookingsOf(customerId);

		// Si no hay bookings, asigna un dashboard vacío (o valores por defecto)
		if (bookings == null || bookings.isEmpty()) {
			System.out.println("No se han encontrado bookings para el cliente " + customerId);
			CustomerDashboard emptyDashboard = new CustomerDashboard();
			super.getBuffer().addData(emptyDashboard);
			return;
		}

		// Moneda de la primera reserva o USD por defecto
		String currency = bookings.stream().findFirst().map(b -> b.getPrice().getCurrency()).orElse("USD");

		Integer thisYear = MomentHelper.getCurrentMoment().getYear();
		List<Booking> lastFiveYearsBookings = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > thisYear - 5).toList();

		// Evitamos divisiones por cero si no hubiera reservas en los últimos 5 años
		int total5YearsBookings = lastFiveYearsBookings.isEmpty() ? 1 : lastFiveYearsBookings.size();

		CustomerDashboard dashboard = new CustomerDashboard();

		// ---------------------------
		// 1. Últimos 5 destinos
		// ---------------------------
		Collection<String> last5destinations = bookings.stream().sorted(Comparator.comparing(Booking::getPurchaseMoment).reversed()).map(b -> b.getFlight().getArrivalCity()).distinct().limit(5).toList();
		dashboard.setLastFiveDestinations((List<String>) last5destinations);

		// ---------------------------
		// 2. Dinero gastado en el último año
		// ---------------------------
		Double totalMoney = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > thisYear - 1).map(Booking::getPrice).map(Money::getAmount).reduce(0.0, Double::sum);
		Money spentMoney = new Money();
		spentMoney.setAmount(totalMoney != null ? totalMoney : 0.0);
		spentMoney.setCurrency(currency);
		dashboard.setSpentMoneyLastYear(spentMoney);

		// ---------------------------
		// 3. Número de reservas Economy y Business
		// ---------------------------
		Integer economyBookings = (int) bookings.stream().filter(b -> b.getTravelClass().equals(TravelClass.ECONOMY)).count();
		dashboard.setEconomyBookings(economyBookings);

		Integer businessBookings = (int) bookings.stream().filter(b -> b.getTravelClass().equals(TravelClass.BUSINESS)).count();
		dashboard.setBusinessBookings(businessBookings);

		// ---------------------------
		// 4. Estadísticas de costes (últimos 5 años)
		// ---------------------------
		Money bookingTotalCost = new Money();
		bookingTotalCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).reduce(0.0, Double::sum));
		bookingTotalCost.setCurrency(currency);
		dashboard.setBookingCountCost(bookingTotalCost);

		Money bookingAverageCost = new Money();
		bookingAverageCost.setAmount(bookingTotalCost.getAmount() / total5YearsBookings);
		bookingAverageCost.setCurrency(currency);
		dashboard.setBookingAverageCost(bookingAverageCost);

		Money bookingMinimumCost = new Money();
		bookingMinimumCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).min(Double::compare).orElse(0.0));
		bookingMinimumCost.setCurrency(currency);
		dashboard.setBookingMinimumCost(bookingMinimumCost);

		Money bookingMaximumCost = new Money();
		bookingMaximumCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).max(Double::compare).orElse(0.0));
		bookingMaximumCost.setCurrency(currency);
		dashboard.setBookingMaximumCost(bookingMaximumCost);

		Money bookingDeviationCost = new Money();
		double varianzaCost = lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).mapToDouble(price -> Math.pow(price - bookingAverageCost.getAmount(), 2)).sum() / total5YearsBookings;
		double deviationCost = Math.sqrt(varianzaCost);
		bookingDeviationCost.setAmount(deviationCost);
		bookingDeviationCost.setCurrency(currency);
		dashboard.setBookingDeviationCost(bookingDeviationCost);

		// ---------------------------
		// 5. Estadísticas de pasajeros (últimos 5 años)
		//    Ajusta getNumberOfPassengers() al nombre real de tu getter.
		// ---------------------------
		Integer totalPassengers = lastFiveYearsBookings.stream().map(Booking::getNumberOfPassengers).reduce(0, Integer::sum);
		dashboard.setBookingCountPassengers(totalPassengers);

		double averagePassengers = (double) totalPassengers / total5YearsBookings;
		dashboard.setBookingAveragePassengers(averagePassengers);

		Integer minPassengers = lastFiveYearsBookings.stream().map(Booking::getNumberOfPassengers).min(Integer::compare).orElse(0);
		dashboard.setBookingMinimumPassengers(minPassengers);

		Integer maxPassengers = lastFiveYearsBookings.stream().map(Booking::getNumberOfPassengers).max(Integer::compare).orElse(0);
		dashboard.setBookingMaximumPassengers(maxPassengers);

		double varianzaPassengers = lastFiveYearsBookings.stream().map(Booking::getNumberOfPassengers).mapToDouble(x -> Math.pow(x - averagePassengers, 2)).sum() / total5YearsBookings;
		double deviationPassengers = Math.sqrt(varianzaPassengers);
		dashboard.setBookingDeviationPassengers(deviationPassengers);

		// Añade el dashboard al buffer (o a la response, según requiera tu framework)
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard object) {
		Dataset dataset = super.unbindObject(object, "lastFiveDestinations", "spentMoneyLastYear", "economyBookings", "businessBookings", "bookingCountCost", "bookingAverageCost", "bookingMinimumCost", "bookingMaximumCost", "bookingDeviationCost",
			"bookingCountPassengers", "bookingAveragePassengers", "bookingMinimumPassengers", "bookingMaximumPassengers", "bookingDeviationPassengers");

		super.getResponse().addData(dataset);
	}
}
