package uam.volontario.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uam.volontario.validation.annotation.Password;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Validator for {@linkplain Password} annotation.
 */
public class PasswordValidator implements ConstraintValidator< Password, String >
{
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" +
            "(?=.*[!@#&()_–[{}]:;',?/*~$^+=<>]).{8,30}$";

    @Override
    public boolean isValid( final String aPassword, final ConstraintValidatorContext aValidatorContext )
    {
        if( !Objects.isNull( aPassword ) && !aPassword.isBlank() )
        {
            final Pattern passwordPattern = Pattern.compile( PASSWORD_PATTERN );

            return passwordPattern.matcher( aPassword )
                    .matches();
        }

        return false;
    }
}
