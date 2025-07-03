
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({
	ElementType.FIELD, ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPhone.PhoneNumberValidatorImpl.class)
public @interface ValidPhone {

	String message() default "6–15 dig";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};


	class PhoneNumberValidatorImpl implements ConstraintValidator<ValidPhone, String> {

		@Override
		public void initialize(final ValidPhone constraintAnnotation) {
		}

		@Override
		public boolean isValid(final String value, final ConstraintValidatorContext context) {
			if (value == null)
				return true;
			return value.matches("^\\+?\\d{6,15}$");
		}
	}
}
