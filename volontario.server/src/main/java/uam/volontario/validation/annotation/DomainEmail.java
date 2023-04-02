package uam.volontario.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uam.volontario.validation.validator.DomainEmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation annotation for validating domain email property.
 */
@Constraint( validatedBy = DomainEmailValidator.class )
@Target( {  ElementType.FIELD } )
@Retention( RetentionPolicy.RUNTIME )
public @interface DomainEmail
{
    String message() default "Domain email has to end with 'amu.edu.pl'";

    Class< ? >[] groups() default {};

    Class< ? extends Payload >[] payload() default {};
}
