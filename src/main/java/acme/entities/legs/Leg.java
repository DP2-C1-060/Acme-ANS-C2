
package acme.entities.legs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidShortText;
import acme.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{2}\\d{4}$")
	private String				flightNumber;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date				scheduledArrival;

	@Mandatory
	@ValidNumber(min = 0.1, max = 24, integer = 2, fraction = 2)
	@Automapped
	private double				duration; // en horas

	@Mandatory
	@Automapped
	private LegStatus			status;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne(optional = false)
	private Airport				departureAirport;

	@Mandatory
	@ManyToOne(optional = false)
	private Airport				arrivalAirport;

	@Mandatory
	@ManyToOne(optional = false)
	private Aircraft			aircraft;

	@Mandatory
	@ManyToOne(optional = false)
	private Flight				flight;
}
