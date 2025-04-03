
package acme.features.manager.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegDeleteService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		int legId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightByLegId(legId);
		boolean status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(id);
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		List<Aircraft> aircrafts = this.repository.findAircrafts();
		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "displayName", leg.getAircraft());

		List<Airport> airports = this.repository.findAirports();
		SelectChoices departureChoices = SelectChoices.from(airports, "city", leg.getDepartureAirport());
		SelectChoices arrivalChoices = SelectChoices.from(airports, "city", leg.getArrivalAirport());

		SelectChoices statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());

		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "draftMode");
		dataset.put("duration", leg.getDuration() + " min");
		dataset.put("status", statusChoices);
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("departures", departureChoices);
		dataset.put("departureAirport", departureChoices.getSelected().getKey());
		dataset.put("arrivals", arrivalChoices);
		dataset.put("arrivalAirport", arrivalChoices.getSelected().getKey());
		dataset.put("masterId", leg.getFlight().getId());

		super.getResponse().addData(dataset);
	}
}
