
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashBoard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	private Integer				totalMaintenanceRecordsPending;
	private Integer				totalMaintenanceRecordsInProgress;
	private Integer				totalMaintenanceRecordsCompleted;

	private String				nearestInspectionDueDate;

	private List<String>		top5AircraftsWithMostTasks;

	private double				avgMaintenanceCostLastYear;
	private Integer				minMaintenanceCostLastYear;
	private Integer				maxMaintenanceCostLastYear;
	private double				stdDevMaintenanceCostLastYear;

	private double				avgTaskDurationForTechnician;
	private Integer				minTaskDurationForTechnician;
	private Integer				maxTaskDurationForTechnician;
	private double				stdDevTaskDurationForTechnician;
}
