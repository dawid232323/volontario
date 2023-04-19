package uam.volontario.validation.service;

import uam.volontario.validation.ValidationResult;

/**
 * Template for entity validation services.
 *
 * @param <T> entity type.
 */
public interface ValidationService< T >
{
    /**
     * Validates entity.
     *
     * @param aEntity entity to validate.
     *
     * @return result of entity validation.
     */
    ValidationResult validateEntity( final T aEntity );

    /**
     * Validates property of given entity.
     *
     * @param aEntity entity to validate.
     *
     * @param aPropertyName
     *
     * @return result of entity validation.
     */
    ValidationResult validateProperty( final T aEntity, final String aPropertyName );
}
