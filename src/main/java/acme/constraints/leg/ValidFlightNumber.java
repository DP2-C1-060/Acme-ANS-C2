
package acme.constraints.leg;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Documented
@Constraint(validatedBy = {})
@Target({
	ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@NotBlank(message = "{acme.validation.leg.flight.number.message}")
@Pattern(regexp = "^[A-Z]{3}\\d{4}$", message = "{acme.validation.leg.flight.number.message}")
public @interface ValidFlightNumber {

	String message() default "{acme.validation.leg.flight.number.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
