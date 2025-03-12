
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
@Constraint(validatedBy = ValidYearsOfExperience.Validator.class)
public @interface ValidYearsOfExperience {

	String message() default "El valor debe estar entre 0 y 70.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	int min() default 0;
	int max() default 70;


	public static class Validator implements ConstraintValidator<ValidYearsOfExperience, Integer> {

		private int	min;
		private int	max;


		@Override
		public void initialize(final ValidYearsOfExperience constraintAnnotation) {
			this.min = constraintAnnotation.min();
			this.max = constraintAnnotation.max();
		}

		@Override
		public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
			if (value == null)
				return true;
			return value >= this.min && value <= this.max;
		}
	}
}
