package uam.volontario.validation.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simple test for {@linkplain DomainEmailValidator}.
 */
public class DomainEmailValidatorTest
{
    private final DomainEmailValidator domainEmailValidator = new DomainEmailValidator();

    @Test
    public void validationShouldPassForEmailWithAmuEduPlSuffix()
    {
        assertTrue( domainEmailValidator.isValid( "student@st.amu.edu.pl", null ) );
    }

    @Test
    public void validationShouldNotPassForEmailWithNoAmuEduPlSuffix()
    {
        assertFalse( domainEmailValidator.isValid( "student@wp.pl", null ) );
    }
}