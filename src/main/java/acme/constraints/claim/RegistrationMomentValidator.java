
package acme.constraints.claim;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claim.Claim;

public class RegistrationMomentValidator extends AbstractValidator<ValidRegistrationMoment, Claim> {

	@Override
	protected void initialise(final ValidRegistrationMoment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (claim == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean consistentMoment;

			Date registrationMoment = claim.getRegistrationMoment();
			Date workStartMoment = claim.getAgent().getWorkStartMoment();

			consistentMoment = registrationMoment.after(workStartMoment);// && registrationMoment.after(claim.getLeg().getScheduledArrival());

			super.state(context, consistentMoment, "registrationMoment", "acme.validation.claim.registrationMoment.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
