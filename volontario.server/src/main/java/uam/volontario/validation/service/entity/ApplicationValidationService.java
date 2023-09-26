package uam.volontario.validation.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uam.volontario.crud.service.ApplicationService;
import uam.volontario.crud.specification.ApplicationSpecification;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.ApplicationSearchQuery;
import uam.volontario.validation.service.AbstractValidationService;

/**
 * Validation service for {@linkplain Application}.
 */
@Service
@RequestScope
public class ApplicationValidationService extends AbstractValidationService< Application >
{
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationValidationService( final ApplicationService aApplicationService )
    {
        this.applicationService = aApplicationService;
    }

    @Override
    protected void validateEntityByCustomConstraints( final Application aApplication )
    {
        isUserReferencedInApplicationAVolunteer( aApplication.getVolunteer() );
        hasUserAlreadyApplied( aApplication );
    }

    private void isUserReferencedInApplicationAVolunteer( final User aUser )
    {
        if( !aUser.hasUserRole( UserRole.VOLUNTEER ) )
        {
            validationViolations.put( "volunteer", "User of id " + aUser.getId() + " is not a Volunteer" );
        }
    }

    private void hasUserAlreadyApplied( final Application aApplication )
    {
        final ApplicationSearchQuery searchQuery = new ApplicationSearchQuery( null, null,
                aApplication.getOffer().getId(), aApplication.getVolunteer().getId(), null );
        final Page< Application > existingApplications = this.applicationService
                .findFiltered( new ApplicationSpecification( searchQuery ), Pageable.unpaged() );
        if( existingApplications.isEmpty() )
        {
            return;
        }
        validationViolations.put( "existing", "You have already applied for this offer" );
    }

    @Override
    protected void postProcessValidation( final Application aApplication )
    {
        // no post process validation for Application entity.
    }
}
