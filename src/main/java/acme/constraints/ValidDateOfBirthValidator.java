
package acme.constraints;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, Date> {

	private static final int	MIN_AGE	= 18;
	private static final int	MAX_AGE	= 100;


	@Override
	public boolean isValid(final Date dateOfBirth, final ConstraintValidatorContext context) {
		if (dateOfBirth == null)
			return false;
		LocalDate birthDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate today = LocalDate.now();

		int age = today.getYear() - birthDate.getYear();

		if (birthDate.plusYears(age).isAfter(today))
			age--;

		return age >= ValidDateOfBirthValidator.MIN_AGE && age <= ValidDateOfBirthValidator.MAX_AGE;
	}
}
