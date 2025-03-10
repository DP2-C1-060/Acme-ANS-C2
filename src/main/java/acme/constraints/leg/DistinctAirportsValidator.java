
package acme.constraints.leg;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.legs.Leg;

@Validator
public class DistinctAirportsValidator extends AbstractValidator<ValidDistinctAirports, Leg> {

	@Override
	protected void initialise(final ValidDistinctAirports annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (leg.getDepartureAirport() == null || leg.getArrivalAirport() == null) {
		} else {
			boolean distinctAirports = !leg.getDepartureAirport().equals(leg.getArrivalAirport());
			super.state(context, distinctAirports, "arrivalAirport", "acme.validation.leg.airports.message");
		}

		boolean result = !super.hasErrors(context);
		return result;
	}
}
