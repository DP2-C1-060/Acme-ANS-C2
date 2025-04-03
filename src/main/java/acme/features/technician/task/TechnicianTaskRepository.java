
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.InvolvedIn;
import acme.entities.tasks.Task;
import acme.realms.technician.Technician;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select tk from Task tk where tk.technician.id =:technicianId")
	Collection<Task> findAllTasksByTechnician(int technicianId);

	@Query("select tk from Task tk where tk.id =:taskId")
	Task findTaskById(int taskId);

	@Query("select t from Technician t")
	Collection<Technician> findAllTechnicians();

	@Query("select t from Technician t where t.id =:technicianId")
	Technician findTechnicianById(int technicianId);

	@Query("select tk from Task tk")
	Collection<Task> findAllTasks();

	@Query("select tk from Task tk where tk.draftMode = false")
	Collection<Task> findAllPublishedTasks();

	@Query("select ii from InvolvedIn ii where ii.task.id = :taskId")
	Collection<InvolvedIn> findInvolvedInFromTaskId(int taskId);

}
