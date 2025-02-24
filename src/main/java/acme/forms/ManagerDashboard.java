
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

	// Indicadores del manager
	private Integer				rankingPosition;
	private Integer				yearsToRetirement;
	private Double				ratioOnTimeLegs;
	private Double				ratioDelayedLegs;

	// Indicadores de popularidad de aeropuertos
	private String				mostPopularAirport;
	private String				leastPopularAirport;

	// Número de legs según su estado (correspondientes a ON_TIME, DELAYED, CANCELLED, LANDED)
	private Integer				legsOnTime;
	private Integer				legsDelayed;
	private Integer				legsCancelled;
	private Integer				legsLanded;

	// Estadísticas del coste de los vuelos
	private Double				averageFlightCost;
	private Double				minFlightCost;
	private Double				maxFlightCost;
	private Double				stdDevFlightCost;

	// Derived attributes -----------------------------------------------------
	// (No se definen métodos derivados en este formulario)

	// Relationships ----------------------------------------------------------
	// (Si fuese necesario, se definirían relaciones con otras entidades o formularios)
}
