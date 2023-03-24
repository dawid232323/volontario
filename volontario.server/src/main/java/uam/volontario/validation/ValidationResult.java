package uam.volontario.validation;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Result of Validation.
 */
@AllArgsConstructor( access = AccessLevel.PRIVATE )
@Getter
@NoArgsConstructor
public class ValidationResult
{
    private Object validatedEntity;
    
    private Map< String, String > validationViolations;
    
    private boolean isValidated;

    /**
     * Creates result with validation passes.
     *
     * @param aValidatedEntity validated entity.
     *
     * @return validation result with accepted entity.
     */
    public static ValidationResult ok( final Object aValidatedEntity )
    {
        return new ValidationResult( aValidatedEntity, Maps.newHashMap(), true );
    }

    /**
     * Creates result with validation failed.
     *
     * @param aValidationViolations validations violations.
     *
     * @return validation result with violations.
     */
    public static ValidationResult error( final Multimap< String, String > aValidationViolations )
    {
        return new ValidationResult( null, createMergedValidationViolationsMap( aValidationViolations ), false );
    }

    private static Map< String, String > createMergedValidationViolationsMap( final Multimap< String, String > aValidationViolations )
    {
        return aValidationViolations.asMap()
                .entrySet()
                .stream()
                .collect( Collectors.toMap( Map.Entry::getKey, entry -> String.join( "\\.", entry.getValue() ) ) );
    }
}
