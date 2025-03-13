
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes--------------------------------------------------------------
	// Airports
	private int					totalAirportsByOperationalScope;

	// Airlines
	private int					totalAirlinesByType;
	private double				airlinesWithEmailAndPhoneRatio;

	// Aircrafts
	private double				activeAircraftsRatio;
	private double				inactiveAircraftsRatio;

	// Reviews
	private double				reviewsAboveFiveRatio;
	private int					reviewsCountLast10Weeks;
	private double				avgReviewsLast10Weeks;
	private double				minReviewsLast10Weeks;
	private double				maxReviewsLast10Weeks;
	private double				stdDevReviewsLast10Weeks;
}
