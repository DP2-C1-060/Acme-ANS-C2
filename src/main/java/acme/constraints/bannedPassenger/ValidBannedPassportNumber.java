
package acme.constraints.bannedPassenger;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Valida que el passportNumber de BannedPassenger cumpla el patrón
 * y sea único en la base de datos.
 */
@Documented
@Constraint(validatedBy = {
	ValidBannedPassportNumberValidator.class
})
@Target({
	TYPE
})
@Retention(RUNTIME)
public @interface ValidBannedPassportNumber {

	String message() default "{acme.validation.banned.passportNumber.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
