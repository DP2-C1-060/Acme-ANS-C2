
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegListService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		int masterId = super.getRequest().getData("masterId", int.class);
		Flight flight = this.repository.findFlightById(masterId);
		boolean status = flight != null && (!flight.isDraftMode() || super.getRequest().getPrincipal().hasRealm(flight.getManager()));
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int masterId = super.getRequest().getData("masterId", int.class);
		Collection<Leg> legs = this.repository.findLegsByFlightId(masterId);
		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");

		if (leg.isDraftMode())
			dataset.put("draftMode", "✔");
		else
			dataset.put("draftMode", "✖");

		dataset.put("departure-arrival", leg.getDepartureAirport().getCity() + " - " + leg.getArrivalAirport().getCity());
		super.addPayload(dataset, leg, "status", "departureAirport.iataCode", "arrivalAirport.iataCode", "aircraft.registrationNumber");
		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Leg> legs) {
		int masterId = super.getRequest().getData("masterId", int.class);
		Flight flight = this.repository.findFlightById(masterId);
		boolean showCreate = flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}
}
