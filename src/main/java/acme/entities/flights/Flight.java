
package acme.entities.flights;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.entities.legs.Leg;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	@Column(length = 50)
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				selfTransfer;

	@Mandatory
	@ValidMoney(min = 0.01)
	@Automapped
	private Money				cost;

	@Optional
	@ValidLongText
	@Automapped
	@Column(length = 255)
	private String				description;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.findLegsByFlightId(this.getId());
		if (legs != null && !legs.isEmpty())
			return legs.get(0).getScheduledDeparture();
		return null;
	}

	@Transient
	public Date getScheduledArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.findLegsByFlightId(this.getId());
		if (legs != null && !legs.isEmpty())
			return legs.get(legs.size() - 1).getScheduledArrival();
		return null;
	}

	@Transient
	public String getOriginCity() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.findLegsByFlightId(this.getId());
		if (legs != null && !legs.isEmpty() && legs.get(0).getDepartureAirport() != null)
			return legs.get(0).getDepartureAirport().getCity();
		return null;
	}

	@Transient
	public String getDestinationCity() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.findLegsByFlightId(this.getId());
		if (legs != null && !legs.isEmpty() && legs.get(legs.size() - 1).getArrivalAirport() != null)
			return legs.get(legs.size() - 1).getArrivalAirport().getCity();
		return null;
	}

	@Transient
	public int getLayoverCount() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.findLegsByFlightId(this.getId());
		return legs != null && legs.size() > 1 ? legs.size() - 1 : 0;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@ManyToOne(optional = false)
	private Manager manager;
}
