
package acme.constraints.passenger;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Valida que el campo 'passportNumber' de un Passenger cumpla:
 * - Un patrón de 6 a 9 caracteres alfanuméricos (A-Z, 0-9).
 * - No existan duplicados en la base de datos, exceptuando el propio Passenger (edición).
 * 
 * Se anota a nivel de clase (TYPE) para acceder al 'id' de la entidad.
 */
@Documented
@Constraint(validatedBy = {
	ValidPassportNumberValidator.class
})
@Target({
	TYPE
})
@Retention(RUNTIME)
public @interface ValidPassportNumber {

	String message() default "{acme.validation.passenger.passportNumber.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
