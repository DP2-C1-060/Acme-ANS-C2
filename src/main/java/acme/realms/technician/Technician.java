
package acme.realms.technician;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidOptionalLongText;
import acme.constraints.ValidPhone;
import acme.constraints.ValidShortText;
import acme.constraints.ValidYearsOfExperience;
import acme.constraints.technician.ValidTechnician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidTechnician
public class Technician extends AbstractRole {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$", message = "El número de licencia debe seguir el patrón: 2-3 letras seguidas de 6 dígitos.")
	@Automapped
	private String				licenseNumber;

	@Mandatory
	@ValidPhone
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				specialization;

	@Mandatory
	@Automapped
	private boolean				passedHealthTest;

	@Mandatory
	@ValidYearsOfExperience(min = 0, max = 70)
	@Automapped
	private Integer				yearsOfExperience;

	@Optional
	@ValidOptionalLongText
	@Automapped
	private String				certifications;
}
