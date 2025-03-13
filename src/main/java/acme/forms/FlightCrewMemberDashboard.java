
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.realms.flightCrewMember.AvailabilityStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long						serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Últimos cinco destinos asignados
	private List<String>							lastFiveDestinations;

	// Número de legs con incidentes agrupados por severidad
	private Integer									legsWithLowSeverityIncidents;    // 0 - 3
	private Integer									legsWithMediumSeverityIncidents; // 4 - 7
	private Integer									legsWithHighSeverityIncidents;   // 8 - 10

	// Miembros de tripulación en su última leg
	private List<String>							lastLegCrewMembers;

	// Asignaciones de vuelo agrupadas por estado
	private Map<AvailabilityStatus, List<String>>	flightAssignmentsByStatus;

	// Estadísticas sobre el número de asignaciones en el último mes
	private Double									avgFlightAssignmentsLastMonth;
	private Integer									minFlightAssignmentsLastMonth;
	private Integer									maxFlightAssignmentsLastMonth;
	private Double									stdDevFlightAssignmentsLastMonth;
}
