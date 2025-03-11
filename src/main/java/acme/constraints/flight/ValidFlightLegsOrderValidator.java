
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
public class ValidFlightLegsOrderValidator extends AbstractValidator<ValidFlightLegsOrder, Flight> {

	@Autowired
	private FlightRepository flightRepository;


	@Override
	protected void initialise(final ValidFlightLegsOrder annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		if (flight == null)
			return true;

		List<Leg> legs = this.flightRepository.findLegsByFlightId(flight.getId());
		if (legs == null || legs.size() < 2)
			return true;

		legs.sort(Comparator.comparing(Leg::getScheduledDeparture));

		boolean valid = true;
		for (int i = 1; i < legs.size(); i++) {
			Leg previous = legs.get(i - 1);
			Leg current = legs.get(i);
			if (!current.getScheduledDeparture().after(previous.getScheduledArrival())) {
				valid = false;
				super.state(context, false, "legs", "acme.validation.flight.legs.order.message");
				break;
			}
		}
		return !super.hasErrors(context);
	}
}
