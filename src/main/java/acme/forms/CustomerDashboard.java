
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	private List<String>		lastFiveDestinations;
	private Money				moneySpentLastYear;
	private Integer				businessBookings;
	private Integer				economyBookings;

	private Money				bookingCostCount;
	private Money				bookingCostAverage;
	private Money				bookingCostMin;
	private Money				bookingCostMax;
	private Money				bookingCostStdDev;

	private Integer				bookingPassengersCount;
	private Double				bookingPassengersAverage;
	private Integer				bookingPassengersMin;
	private Integer				bookingPassengersMax;
	private Double				bookingPassengersStdDev;
}
