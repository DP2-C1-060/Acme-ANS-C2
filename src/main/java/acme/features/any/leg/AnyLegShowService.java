
package acme.features.any.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

@GuiService
public class AnyLegShowService extends AbstractGuiService<Any, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Flight flight;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightByLegId(id);
		status = flight != null && !flight.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		dataset.put("departureCity", leg.getDepartureAirport().getCity());
		dataset.put("arrivalCity", leg.getArrivalAirport().getCity());
		dataset.put("aircraft", leg.getAircraft().getDisplayName());
		dataset.put("duration", leg.getDuration());

		super.getResponse().addData(dataset);
	}
}
