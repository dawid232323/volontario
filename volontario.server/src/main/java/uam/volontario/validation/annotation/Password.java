package uam.volontario.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uam.volontario.validation.validator.PasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation annotation for validating password property.
 */
@Constraint( validatedBy = PasswordValidator.class )
@Target( {  ElementType.FIELD } )
@Retention( RetentionPolicy.RUNTIME )
public @interface Password
{
    String message() default "Password must be 8-30 characters long and must contain at least one: lowercase letter," +
            " uppercase letter, digit and special character";

    Class< ? >[] groups() default {};

    Class< ? extends Payload >[] payload() default {};
}
