
package acme.constraints.leg;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

public class UniqueFlightNumberDigitsValidator extends AbstractValidator<ValidUniqueFlightNumberDigits, Leg> {

	@Autowired
	private LegRepository legRepository;


	@Override
	protected void initialise(final ValidUniqueFlightNumberDigits annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		if (leg == null || leg.getFlightNumber() == null || leg.getFlightNumber().length() < 7)
			return true;

		String flightNumber = leg.getFlightNumber();
		String lastFourDigits = flightNumber.substring(3);

		Leg existingLeg = this.legRepository.findLegByLastFourDigits(lastFourDigits);
		boolean unique = existingLeg == null || existingLeg.equals(leg);

		if (!unique)
			super.state(context, false, "flightNumber", "acme.validation.leg.number.message");

		return unique;
	}
}
