
package acme.constraints.leg;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.legs.Leg;

@Validator
public class ScheduledDatesValidator extends AbstractValidator<ValidScheduledDates, Leg> {

	@Override
	protected void initialise(final ValidScheduledDates annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (leg.getScheduledDeparture() == null || leg.getScheduledArrival() == null) {
		} else {
			boolean valid = leg.getScheduledArrival().after(leg.getScheduledDeparture());
			super.state(context, valid, "scheduledArrival", "acme.validation.leg.dates.message");
		}
		return !super.hasErrors(context);
	}
}
