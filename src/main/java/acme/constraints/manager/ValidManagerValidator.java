
package acme.constraints.manager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.realms.Manager;

@Validator
public class ValidManagerValidator extends AbstractValidator<ValidManager, Manager> {

	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		if (manager == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		String identifier = manager.getIdentifier();
		String name = manager.getUserAccount().getIdentity().getName();
		String surname = manager.getUserAccount().getIdentity().getSurname();

		if (identifier != null && identifier.length() >= 2 && name != null && surname != null) {
			String expectedPrefix = name.substring(0, 1).toUpperCase() + surname.substring(0, 1).toUpperCase();
			if (!identifier.substring(0, 2).equals(expectedPrefix))
				super.state(context, false, "identifier", "acme.validation.manager.identifier.invalid");
		}

		Date dob = manager.getDateOfBirth();
		if (dob == null)
			return !super.hasErrors(context);

		LocalDate birthDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Date currentMoment = MomentHelper.getCurrentMoment();
		LocalDate today = currentMoment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (!birthDate.isBefore(today))
			return !super.hasErrors(context);

		int age = today.getYear() - birthDate.getYear();
		if (birthDate.plusYears(age).isAfter(today))
			age--;

		if (age < 16)
			super.state(context, false, "dateOfBirth", "acme.validation.manager.age.tooYoung");

		// Calcular la experiencia máxima permitida (edad - 16)
		int maxExperience = age - 16;
		if (maxExperience < 0)
			maxExperience = 0;

		// Verificar que los años de experiencia no excedan el máximo permitido
		if (manager.getYearsOfExperience() > maxExperience)
			super.state(context, false, "yearsOfExperience", "acme.validation.manager.experience.message");

		return !super.hasErrors(context);
	}
}
