
package acme.entities.flights;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
import acme.entities.legs.Leg;
import acme.realms.manager.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "id, manager_id, draftMode"), @Index(columnList = "manager_id, draftMode")
})
@ValidFlight
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				selfTransfer;

	@Mandatory
	@ValidMoney(min = 0.00, max = 1000000.00)
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		Date result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.findLegsByFlightDeparture(this.getId());

		result = legs.isEmpty() ? null : legs.get(0).getScheduledDeparture();

		return result;
	}

	@Transient
	public String getFlightSummary() {

		var fmt = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm");
		String from = this.getDepartureCity() != null ? this.getDepartureCity() : "—";
		String to = this.getArrivalCity() != null ? this.getArrivalCity() : "—";
		String depart = this.getScheduledDeparture() != null ? fmt.format(this.getScheduledDeparture()) : "—";
		String arrive = this.getScheduledArrival() != null ? fmt.format(this.getScheduledArrival()) : "—";

		return String.format("%s -> %s --- %s // %s", from, to, depart, arrive);
	}

	@Transient
	public Date getScheduledArrival() {
		Date result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.findLegsByFlightArrival(this.getId());

		result = legs.isEmpty() ? null : legs.get(0).getScheduledArrival();

		return result;
	}

	@Transient
	public String getDepartureCity() {
		String result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.findLegsByFlightDeparture(this.getId());

		result = legs.isEmpty() ? null : legs.get(0).getDeparture().getCity();
		return result;

	}

	@Transient
	public String getArrivalCity() {
		String result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.findLegsByFlightArrival(this.getId());

		result = legs.isEmpty() ? null : legs.get(0).getArrival().getCity();
		return result;

	}

	@Transient
	public Integer getLayovers() {
		Integer result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);

		List<Leg> legs = repository.findLegsByFlight(this.getId());

		result = legs.size() > 0 ? legs.size() - 1 : 0;

		return result;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager manager;

}
