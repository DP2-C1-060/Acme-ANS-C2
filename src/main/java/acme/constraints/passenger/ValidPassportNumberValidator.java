
package acme.constraints.passenger;

import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.booking.Passenger;
import acme.features.customer.passenger.CustomerPassengerRepository;

public class ValidPassportNumberValidator implements ConstraintValidator<ValidPassportNumber, Passenger> {

	private static final Pattern			PASSPORT_PATTERN	= Pattern.compile("^[A-Z0-9]{6,9}$");

	@Autowired
	protected CustomerPassengerRepository	passengerRepository;


	@Override
	public void initialize(final ValidPassportNumber annotation) {
		// Sin inicialización específica
	}

	@Override
	public boolean isValid(final Passenger passenger, final ConstraintValidatorContext context) {
		if (passenger == null)
			// Si la entidad es nula, dejamos que otras validaciones se encarguen
			return true;

		String passportNumber = passenger.getPassportNumber();
		if (passportNumber == null)
			// Si usas @Mandatory en la entidad, esa validación ya se encarga de 'null'.
			return true;

		// 1) Verificar el patrón
		if (!ValidPassportNumberValidator.PASSPORT_PATTERN.matcher(passportNumber).matches()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{acme.validation.passenger.passportNumber.pattern}").addPropertyNode("passportNumber") // Asocia el error al campo 'passportNumber'
				.addConstraintViolation();
			return false;
		}

		// 2) Verificar duplicados en la BD.
		//    findAllByPassportNumber(...) puede devolver varios Passenger si hay duplicados
		List<Passenger> matchingPassengers = this.passengerRepository.findAllByPassportNumber(passportNumber);

		// Filtrar la propia entidad (si está en edición).
		// Dejar sólo los que tienen un ID distinto.
		matchingPassengers.removeIf(p -> p.getId() == passenger.getId());

		// Tras remover el propio ID, si la lista NO está vacía => hay duplicados
		if (!matchingPassengers.isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{acme.validation.passenger.passportNumber.unique}").addPropertyNode("passportNumber").addConstraintViolation();
			return false;
		}

		// Pasa patrón y unicidad
		return true;
	}
}
