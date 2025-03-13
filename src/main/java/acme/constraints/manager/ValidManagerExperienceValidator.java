
package acme.constraints.manager;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Manager;

@Validator
class ValidManagerExperienceValidator extends AbstractValidator<ValidManagerExperience, Manager> {

	@Override
	protected void initialise(final ValidManagerExperience annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {

		assert context != null;

		if (manager == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		if (manager.getDateOfBirth() == null)
			return true;

		int edad = this.calcularEdad(manager.getDateOfBirth());

		// Suponiendo que la persona empieza a trabajar a los 16 años,
		// los años de experiencia deben ser menores o iguales a (edad - 16)
		int maxExperiencia = edad - 16;
		if (maxExperiencia < 0)
			maxExperiencia = 0;

		boolean valido = manager.getYearsOfExperience() <= maxExperiencia;
		super.state(context, valido, "yearsOfExperience", "acme.validation.manager.experience.message");

		return !super.hasErrors(context);
	}

	private int calcularEdad(final Date fechaNacimiento) {
		Calendar nacimiento = Calendar.getInstance();
		nacimiento.setTime(fechaNacimiento);
		Calendar hoy = Calendar.getInstance();
		int diff = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
		if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR))
			diff--;
		return diff;
	}
}
