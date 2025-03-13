
package acme.entities.bannedPassengers;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.bannedPassenger.ValidBannedPassportNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBannedPassportNumber
public class BannedPassenger extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				fullName;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateOfBirth;

	@Mandatory
	@Automapped
	private String				passportNumber;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				nationality;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				reason;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				banIssuedDate;

	@Optional
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				liftDate;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
