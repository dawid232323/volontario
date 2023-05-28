package uam.volontario.validation.service.entity;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.validation.service.AbstractValidationService;

/**
 * Validation service for {@linkplain VolunteerData}.
 */
@Service
@RequestScope
public class VolunteerDataValidationService extends AbstractValidationService< VolunteerData >
{
    @Override
    protected void validateEntityByCustomConstraints( final VolunteerData aVolunteerData )
    {
        // no custom validations for volunteer data entity.
    }

    @Override
    protected void postProcessValidation( final VolunteerData aVolunteerData )
    {
        // no post process validation for volunteer data entity.
    }
}
