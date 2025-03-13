
package acme.entities.weatherconditions;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidOptionalLongText;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WeatherConditions extends AbstractEntity {

	// Serialisation identifier ----------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Relationships ---------------------------------------------------------

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airport				airport;

	// Attributes ------------------------------------------------------------

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date				date;

	@Optional
	@ValidNumber
	@Automapped
	private Float				tempMax;

	@Optional
	@ValidNumber
	@Automapped
	private Float				tempMin;

	@Optional
	@ValidNumber
	@Automapped
	private Float				feelsLikeMax;

	@Optional
	@ValidNumber
	@Automapped
	private Float				feelsLikeMin;

	@Optional
	@ValidNumber
	@Automapped
	private Float				humidity;

	@Optional
	@ValidNumber
	@Automapped
	private Float				windSpeed;

	@Optional
	@ValidNumber
	@Automapped
	private Float				windGust;

	@Optional
	@ValidNumber
	@Automapped
	private Float				windDirection;

	@Optional
	@ValidNumber
	@Automapped
	private Float				precipProbability;

	@Optional
	@ValidOptionalLongText
	@Automapped
	private String				conditions;
}
