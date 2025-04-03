
package acme.features.manager.leg;

import java.util.Comparator;
import java.util.Date;
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
public class ManagerLegPublishService extends AbstractGuiService<Manager, Leg> {

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
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");

		int aircraftId = super.getRequest().getData("aircraft", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);

		int departureId = super.getRequest().getData("departureAirport", int.class);
		Airport departureAirport = this.repository.findAirportById(departureId);

		int arrivalId = super.getRequest().getData("arrivalAirport", int.class);
		Airport arrivalAirport = this.repository.findAirportById(arrivalId);

		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departureAirport);
		leg.setArrivalAirport(arrivalAirport);
	}

	@Override
	public void validate(final Leg leg) {
		Flight flight = leg.getFlight();
		Date scheduledDeparture = leg.getScheduledDeparture();
		Airport departureAirport = leg.getDepartureAirport();
		Airport arrivalAirport = leg.getArrivalAirport();

		List<Leg> publishedLegsSameFlight = List.of();

		if (flight != null)
			publishedLegsSameFlight = this.repository.findPublishedLegsByFlightId(flight.getId());

		if (flight != null && scheduledDeparture != null) {
			final boolean selfTransfer = flight.isSelfTransfer();

			if (!publishedLegsSameFlight.contains(leg))
				publishedLegsSameFlight.add(leg);
			publishedLegsSameFlight.sort(Comparator.comparing(Leg::getScheduledDeparture));

			final int index = publishedLegsSameFlight.indexOf(leg);
			if (index != -1) {
				if (index > 0) {
					final Leg previous = publishedLegsSameFlight.get(index - 1);
					final Airport previousArr = previous.getArrivalAirport();
					if (previousArr != null && departureAirport != null)
						if (selfTransfer) {
							if (previousArr.equals(departureAirport))
								super.state(false, "departureAirport", "acme.validation.leg.invalid-airport-link.previous.selfTransfer.message");
						} else if (!previousArr.equals(departureAirport))
							super.state(false, "departureAirport", "acme.validation.leg.invalid-airport-link.previous.message");
				}
				if (index < publishedLegsSameFlight.size() - 1) {
					final Leg next = publishedLegsSameFlight.get(index + 1);
					final Airport nextDep = next.getDepartureAirport();
					if (arrivalAirport != null && nextDep != null)
						if (selfTransfer) {
							if (arrivalAirport.equals(nextDep))
								super.state(false, "arrivalAirport", "acme.validation.leg.invalid-airport-link.next.selfTransfer.message");
						} else if (!arrivalAirport.equals(nextDep))
							super.state(false, "arrivalAirport", "acme.validation.leg.invalid-airport-link.next.message");
				}
			}
		}
	}

	@Override
	public void perform(final Leg leg) {
		leg.setDraftMode(false);
		this.repository.save(leg);
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
