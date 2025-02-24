
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Integer				rankingPosition;
	private Integer				yearsToRetirement;
	private Double				ratioOnTimeLegs;
	private Double				ratioDelayedLegs;

	private String				mostPopularAirport;
	private String				leastPopularAirport;

	private Integer				legsOnTime;
	private Integer				legsDelayed;
	private Integer				legsCancelled;
	private Integer				legsLanded;

	private Double				averageFlightCost;
	private Double				minFlightCost;
	private Double				maxFlightCost;
	private Double				stdDevFlightCost;

}
