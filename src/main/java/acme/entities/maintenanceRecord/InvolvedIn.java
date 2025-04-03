
package acme.entities.maintenanceRecord;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.entities.tasks.Task;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InvolvedIn extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Relationships ----------------------------------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private MaintenanceRecord	maintenanceRecord;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Task				task;

}
