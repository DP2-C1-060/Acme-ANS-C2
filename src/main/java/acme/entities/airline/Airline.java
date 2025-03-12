
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Mandatory
	@ValidShortText
	@Automapped
	private String				name;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2}X$")
	@Column(unique = true)
	@Automapped
	private String				iataCode;

	@Optional
	@ValidUrl
	@Automapped
	private String				website;

	@Mandatory
	@Automapped
	private AirLineType			type;

	@Temporal(TemporalType.DATE)
	@Past(message = "La fecha de fundación debe estar en el pasado.")
	private Date				foundationDate;

	@Optional
	@ValidEmail
	@Automapped
	private String				email;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phone;

}
