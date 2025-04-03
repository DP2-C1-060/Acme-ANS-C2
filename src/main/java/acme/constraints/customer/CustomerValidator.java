
package acme.constraints.customer;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.features.authenticated.customer.CustomerRepository;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Autowired
	private CustomerRepository repository;


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {

		// Recuperar y limpiar el identificador del customer
		String identifier = customer.getCustomerIdentifier();
		if (identifier == null || identifier.trim().isEmpty()) {
			super.state(context, false, "customerIdentifier", "{acme.validation.identifier.nullornotpattern.message}");
			return false;
		}
		identifier = identifier.trim();

		// Validar que la identidad del usuario tenga nombre
		DefaultUserIdentity identity = customer.getUserAccount().getIdentity();
		if (identity.getName() == null || identity.getName().trim().isEmpty()) {
			super.state(context, false, "customerIdentifier", "{acme.validation.identifier.invalidIdentity.message}");
			return false;
		}

		// Obtener la inicial del nombre
		String nameInitial = String.valueOf(identity.getName().trim().charAt(0)).toUpperCase();
		String expectedInitials;

		// Si el apellido (surname) existe y no está vacío, se usa su inicial; de lo contrario, solo la del nombre.
		if (identity.getSurname() != null && !identity.getSurname().trim().isEmpty()) {
			String surnameInitial = String.valueOf(identity.getSurname().trim().charAt(0)).toUpperCase();
			expectedInitials = nameInitial + surnameInitial;
		} else
			expectedInitials = nameInitial;

		// Determinar la longitud requerida según las iniciales esperadas
		int expectedLength = expectedInitials.length();

		// Verificar que el identificador tenga la longitud mínima necesaria
		if (identifier.length() < expectedLength) {
			super.state(context, false, "customerIdentifier", "{acme.validation.identifier.tooShort.message}");
			return false;
		}

		// Extraer la subcadena de 'identifier' y comparar con las iniciales esperadas
		String identifierInitials = identifier.substring(0, expectedLength);
		if (!expectedInitials.equals(identifierInitials)) {
			super.state(context, false, "customerIdentifier", "{acme.validation.identifier.notInitials.message}");
			return false;
		}

		// Verificar que no exista otro customer con el mismo identificador
		Customer customerWithSameIdentifier = this.repository.findCustomerByIdentifier(identifier);
		if (customerWithSameIdentifier != null && customerWithSameIdentifier.getId() != customer.getId()) {
			super.state(context, false, "customerIdentifier", "{acme.validation.identifier.repeated.message}: " + identifier);
			return false;
		}
		return true;
	}
}
