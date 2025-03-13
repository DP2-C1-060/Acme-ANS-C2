
package acme.constraints.flight;

import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightRepository;
import acme.entities.legs.Leg;

@Validator
class ValidFlightValidator extends AbstractValidator<ValidFlight, Flight> {

	@Autowired
	private FlightRepository flightRepository;


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {

		assert context != null;

		if (flight == null) {
			super.state(context, false, "*", "acme.validation.flight.null.message");
			return false;
		}

		List<Leg> legs = this.flightRepository.findLegsByFlightId(flight.getId());

		// Las validaciones de coherencia solo se aplican si el vuelo no está en modo borrador
		if (!flight.isDraftMode()) {

			// Validación del orden de los legs: se comprueba que, al ordenarlos por fecha de salida,
			// la fecha de salida de cada leg sea posterior a la fecha de llegada del leg anterior.
			if (legs != null && legs.size() >= 2) {
				legs.sort(Comparator.comparing(Leg::getScheduledDeparture));
				for (int i = 1; i < legs.size(); i++) {
					Leg previous = legs.get(i - 1);
					Leg current = legs.get(i);
					if (!current.getScheduledDeparture().after(previous.getScheduledArrival())) {
						super.state(context, false, "*", "acme.validation.flight.legs.order.message");
						break;
					}
				}
			}

			// Validación: un vuelo publicado debe tener al menos un leg y no debe contener legs en modo borrador
			boolean hasLegs = legs != null && !legs.isEmpty();
			super.state(context, hasLegs, "*", "acme.validation.flight.published.message");
			if (hasLegs) {
				List<Leg> draftLegs = this.flightRepository.findDraftLegsByFlightId(flight.getId());
				boolean noDraftLegs = draftLegs == null || draftLegs.isEmpty();
				super.state(context, noDraftLegs, "draftMode", "acme.validation.flight.published.message");
			}

			// Validación para selfTransfer: no se permite un vuelo de self-transfer con un solo leg
			if (legs != null && legs.size() == 1 && flight.isSelfTransfer())
				super.state(context, false, "selfTransfer", "acme.validation.flight.selftransfer.message");

			// Nueva validación: el aeropuerto de salida del primer leg y el aeropuerto de llegada
			// del último leg deben ser distintos
			if (legs != null && !legs.isEmpty()) {
				Leg firstLeg = legs.get(0);
				Leg lastLeg = legs.get(legs.size() - 1);
				if (firstLeg.getDepartureAirport() != null && lastLeg.getArrivalAirport() != null) {
					boolean validAirports = !firstLeg.getDepartureAirport().equals(lastLeg.getArrivalAirport());
					super.state(context, validAirports, "*", "acme.validation.flight.airports.different.message");
				}
			}
		}

		return !super.hasErrors(context);
	}
}
