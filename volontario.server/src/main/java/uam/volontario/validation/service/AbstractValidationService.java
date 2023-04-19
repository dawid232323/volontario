package uam.volontario.validation.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.validation.*;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.validator.internal.engine.path.PathImpl;
import uam.volontario.validation.ValidationResult;

import java.util.Set;

/**
 * Abstract validation service.
 *
 * @param <T> entity type
 */
public abstract class AbstractValidationService< T > implements ValidationService< T >
{
    protected final Multimap< String, String > validationViolations = ArrayListMultimap.create();

    @Override
    public ValidationResult validateEntity( final T aEntity )
    {
        validateEntityByAnnotations( aEntity );
        validateEntityByCustomConstraints( aEntity );
        return createResult( aEntity );
    }

    @Override
    public ValidationResult validateProperty( final T aEntity, final String aPropertyName )
    {
        return null;
    }

    /**
     * Validates given entity for customisable constraints that can specifically defined for entity type.
     *
     * @param aEntity entity to validate.
     */
    protected abstract void validateEntityByCustomConstraints( final T aEntity );

    private void validateEntityByAnnotations( final T aEntity )
    {
        try( ValidatorFactory factory = Validation.buildDefaultValidatorFactory() )
        {
            final Validator validator = factory.getValidator();
            final Set< ConstraintViolation< T > > annotationViolations = validator.validate( aEntity );
            mapAnnotationViolationsToPropertiesWithCauses( annotationViolations );
        }
        catch( Exception aE )
        {
            throw new ValidationException( "Error occurred during annotation validation", aE );
        }
    }

    private ValidationResult createResult( final T aEntity )
    {
        if( validationViolations.isEmpty() )
        {
            return ValidationResult.ok( aEntity );
        }

        return ValidationResult.error( validationViolations );
    }

    private void mapAnnotationViolationsToPropertiesWithCauses( final Set< ConstraintViolation< T > > aViolations )
    {
        aViolations.stream()
                .map( violation -> Pair.of( getPropertyName( violation ), violation.getMessage() ) )
                .forEach( pair -> validationViolations.put( pair.getLeft(), pair.getRight() ) );
    }

    private String getPropertyName( final ConstraintViolation< T > aViolation )
    {
        return ((PathImpl)aViolation.getPropertyPath()).getLeafNode().getName();
    }
}
