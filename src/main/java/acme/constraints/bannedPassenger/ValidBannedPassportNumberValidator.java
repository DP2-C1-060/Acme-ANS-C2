
package acme.constraints.bannedPassenger;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.bannedPassengers.BannedPassenger;
import acme.entities.bannedPassengers.BannedPassengerRepository;

public class ValidBannedPassportNumberValidator implements ConstraintValidator<ValidBannedPassportNumber, BannedPassenger> {

	@Autowired
	protected BannedPassengerRepository	repository;

	// Patrón que exige mayúsculas (A-Z) y dígitos (0-9) de 6 a 9 caracteres
	private static final Pattern		PASSPORT_PATTERN	= Pattern.compile("^[A-Z0-9]{6,9}$");


	@Override
	public void initialize(final ValidBannedPassportNumber constraintAnnotation) {

	}

	@Override
	public boolean isValid(final BannedPassenger bannedPassenger, final ConstraintValidatorContext context) {

		if (bannedPassenger == null)
			return true;

		String passportNumber = bannedPassenger.getPassportNumber();
		if (passportNumber == null)

			return true;

		if (!ValidBannedPassportNumberValidator.PASSPORT_PATTERN.matcher(passportNumber).matches()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{acme.validation.banned.passportNumber.pattern}").addPropertyNode("passportNumber").addConstraintViolation();
			return false;
		}

		BannedPassenger existing = this.repository.findOneByPassportNumber(passportNumber);

		if (existing != null && existing.getId() != bannedPassenger.getId()) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{acme.validation.banned.passportNumber.unique}").addPropertyNode("passportNumber").addConstraintViolation();
			return false;
		}

		return true;
	}
}
