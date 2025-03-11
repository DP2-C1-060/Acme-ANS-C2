
package acme.constraints.flight;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightRepository;
import acme.entities.legs.Leg;

@Validator
public class ValidPublishedFlightValidator extends AbstractValidator<ValidPublishedFlight, Flight> {

	@Autowired
	private FlightRepository flightRepository;


	@Override
	protected void initialise(final ValidPublishedFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		if (flight == null)
			return true;

		if (flight.isDraftMode())
			return true;

		List<Leg> legs = this.flightRepository.findLegsByFlightId(flight.getId());
		boolean hasLegs = legs != null && !legs.isEmpty();
		super.state(context, hasLegs, "*", "acme.validation.flight.published.message");

		if (hasLegs) {
			List<Leg> draftLegs = this.flightRepository.findDraftLegsByFlightId(flight.getId());
			boolean noDraftLegs = draftLegs == null || draftLegs.isEmpty();
			super.state(context, noDraftLegs, "draftMode", "acme.validation.flight.published.message");
		}

		return !super.hasErrors(context);
	}
}
