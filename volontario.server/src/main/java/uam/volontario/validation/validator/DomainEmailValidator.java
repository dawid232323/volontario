package uam.volontario.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uam.volontario.validation.annotation.DomainEmail;

import java.util.Objects;

/**
 * Validator for {@linkplain DomainEmail} annotation.
 */
public class DomainEmailValidator implements ConstraintValidator< DomainEmail, String >
{
    @Override
    public boolean isValid( final String aDomainEmail, final ConstraintValidatorContext aValidatorContext )
    {
        if( !Objects.isNull( aDomainEmail ) )
        {
            return aDomainEmail.endsWith( "amu.edu.pl" );
        }

        return false;
    }
}
