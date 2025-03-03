
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long		serialVersionUID	= 1L;

	private List<String>			lastFiveDestinations;
	private Money					moneySpentLastYear;
	private Map<String, Integer>	bookingsByTravelClass;

	private Integer					bookingCostCount;
	private Double					bookingCostAverage;
	private Double					bookingCostMin;
	private Double					bookingCostMax;
	private Double					bookingCostStdDev;

	private Integer					bookingPassengersCount;
	private Double					bookingPassengersAverage;
	private Double					bookingPassengersMin;
	private Double					bookingPassengersMax;
	private Double					bookingPassengersStdDev;
}
