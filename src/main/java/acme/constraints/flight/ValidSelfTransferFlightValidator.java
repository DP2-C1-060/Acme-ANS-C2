
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
public class ValidSelfTransferFlightValidator extends AbstractValidator<ValidSelfTransferFlight, Flight> {

	@Autowired
	private FlightRepository flightRepository;


	@Override
	protected void initialise(final ValidSelfTransferFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		if (flight == null)
			return true;

		List<Leg> legs = this.flightRepository.findLegsByFlightId(flight.getId());
		if (legs == null || legs.isEmpty())
			return true;
		if (legs.size() == 1 && flight.isSelfTransfer())
			super.state(context, false, "selfTransfer", "acme.validation.flight.selftransfer.message");

		return !super.hasErrors(context);
	}
}
