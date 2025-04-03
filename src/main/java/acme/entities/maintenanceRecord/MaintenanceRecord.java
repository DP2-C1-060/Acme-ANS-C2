
package acme.entities.maintenanceRecord;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidOptionalLongText;
import acme.constraints.maintenanceRecord.ValidMaintenanceRecord;
import acme.entities.aircraft.Aircraft;
import acme.realms.technician.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidMaintenanceRecord
public class MaintenanceRecord extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long			serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date						maintenanceDate;

	@Mandatory
	@Valid
	@Automapped
	private MaintenanceRecordsStatus	status;

	@Mandatory
	@ValidMoment(min = "2025/01/01 00:00")
	@Temporal(TemporalType.TIMESTAMP)
	private Date						nextInspectionDate;

	@Mandatory
	@ValidMoney(min = 1.00)
	@Automapped
	private Money						estimatedCost;

	@Optional
	@ValidOptionalLongText
	@Automapped
	private String						notes;

	@Mandatory
	@Automapped
	private boolean						draftMode;

	// RelationShips -------------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician					technician;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft					aircraft;

}
