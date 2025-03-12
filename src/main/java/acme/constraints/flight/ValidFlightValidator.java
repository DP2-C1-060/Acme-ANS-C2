
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
		if (flight == null)
			return true;

		List<Leg> legs = this.flightRepository.findLegsByFlightId(flight.getId());

		if (legs != null && legs.size() >= 2) {
			legs.sort(Comparator.comparing(Leg::getScheduledDeparture));
			for (int i = 1; i < legs.size(); i++) {
				Leg previous = legs.get(i - 1);
				Leg current = legs.get(i);
				if (!current.getScheduledDeparture().after(previous.getScheduledArrival())) {
					super.state(context, false, "legs", "acme.validation.flight.legs.order.message");
					break;
				}
			}
		}

		if (!flight.isDraftMode()) {
			boolean hasLegs = legs != null && !legs.isEmpty();
			super.state(context, hasLegs, "*", "acme.validation.flight.published.message");
			if (hasLegs) {
				List<Leg> draftLegs = this.flightRepository.findDraftLegsByFlightId(flight.getId());
				boolean noDraftLegs = draftLegs == null || draftLegs.isEmpty();
				super.state(context, noDraftLegs, "draftMode", "acme.validation.flight.published.message");
			}
		}

		if (legs != null && legs.size() == 1 && flight.isSelfTransfer())
			super.state(context, false, "selfTransfer", "acme.validation.flight.selftransfer.message");

		return !super.hasErrors(context);
	}
}
