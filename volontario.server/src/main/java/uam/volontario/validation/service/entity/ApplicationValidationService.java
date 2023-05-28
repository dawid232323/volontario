package uam.volontario.validation.service.entity;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.validation.service.AbstractValidationService;

/**
 * Validation service for {@linkplain Application}.
 */
@Service
@RequestScope
public class ApplicationValidationService extends AbstractValidationService< Application >
{
    @Override
    protected void validateEntityByCustomConstraints( final Application aApplication )
    {
        isUserReferencedInApplicationAVolunteer( aApplication.getVolunteer() );
    }

    private void isUserReferencedInApplicationAVolunteer( final User aUser )
    {
        if( !aUser.hasUserRole( UserRole.VOLUNTEER ) )
        {
            validationViolations.put( "volunteer", "User of id " + aUser.getId() + " is not a Volunteer" );
        }
    }

    @Override
    protected void postProcessValidation( final Application aApplication )
    {
        // no post process validation for Application entity.
    }
}
