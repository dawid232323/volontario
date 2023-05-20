package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.ApplicationService;
import uam.volontario.crud.service.ApplicationStateService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.UserService;
import uam.volontario.dto.ApplicationDto;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.ApplicationState;
import uam.volontario.model.offer.impl.ApplicationStateEnum;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.security.mail.MailService;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.ApplicationValidationService;

import java.util.Optional;

/**
 * Handler class for {@linkplain uam.volontario.model.offer.impl.Application} processing operations.
 */
@Service
public class ApplicationProcessingHandler
{
    private final ApplicationService applicationService;

    private final ApplicationStateService applicationStateService;

    private final ApplicationValidationService applicationValidationService;

    private final UserService userService;

    private final OfferService offerService;

    private final MailService mailService;

    /**
     * CDI constructor.
     *
     * @param aApplicationService application service.
     *
     * @param aOfferService offer service.
     *
     * @param aUserService user service.
     *
     * @param aApplicationStateService application state service.
     *
     * @param aApplicationValidationService application validation service.
     */
    @Autowired
    public ApplicationProcessingHandler( final ApplicationService aApplicationService, final OfferService aOfferService,
                                         final UserService aUserService,
                                         final ApplicationStateService aApplicationStateService,
                                         final ApplicationValidationService aApplicationValidationService,
                                         final MailService aMailService )
    {
        applicationService = aApplicationService;
        offerService = aOfferService;
        userService = aUserService;
        applicationStateService = aApplicationStateService;
        applicationValidationService = aApplicationValidationService;
        mailService = aMailService;
    }

    /**
     * Creates Application instance.
     *
     * @param aDto dto with data to create Application.
     *
     * @return
     *        - Response Entity with code 201 and newly created Application, if everything went as expected.
     *        - Response Entity with code 400 when User and/or Offer could not be found based on DTO's data, or when
     *          newly created Application did not pass validation.
     *        - Response Entity with code 500, if there was an unexpected server-side error.
     */
    public ResponseEntity< ? > createApplication( final ApplicationDto aDto )
    {
        try
        {
            final Optional< User > optionalUser = userService.tryLoadEntity( aDto.getVolunteerId() );
            final Optional< Offer > optionalOffer = offerService.tryLoadEntity( aDto.getOfferId() );

            if( optionalUser.isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( "User of id " + aDto.getVolunteerId() + " not found." );
            }
            else if( optionalOffer.isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( "Offer of id " + aDto.getOfferId() + " not found." );
            }

            final Application application = Application.builder()
                    .volunteer( optionalUser.get() )
                    .offer( optionalOffer.get() )
                    .isStarred( false )
                    .participationMotivation( aDto.getParticipationMotivation() )
                    .state( getAwaitingApplicationState() )
                    .build();

            final ValidationResult validationResult = applicationValidationService.validateEntity( application );
            if( validationResult.isValidated() )
            {
                applicationService.saveOrUpdate( application );
                mailService.sendApplicationCreatedMailToVolunteer( application );
                return ResponseEntity.status( HttpStatus.CREATED )
                        .body( application );
            }
            else
            {
                return ResponseEntity.badRequest()
                        .body( validationResult.getValidationViolations() );
            }
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .build();
        }
    }

    /**
     * Updates 'starred' property of Application.
     *
     * @param aApplicationId id of Application to perform update on.
     *
     * @param aStar indication whether we star or unstar Application.
     *
     * @return
     *          - Response Entity with modified Application and code 200, if update was successful.
     *          - Response Entity with code 400, if there was no Application with given id.
     *          - Response Entity with code 501, if there was an unexpected server side error.
     */
    public ResponseEntity< ? > updateStarOfApplication( final Long aApplicationId, final boolean aStar )
    {
        try
        {
            final Optional< Application > optionalApplication = applicationService.tryLoadEntity( aApplicationId );
            if( optionalApplication.isPresent() )
            {
                final Application application = optionalApplication.get();
                application.setStarred( aStar );

                applicationService.saveOrUpdate( application );

                return ResponseEntity.ok( application );
            }
            else
            {
                return ResponseEntity.badRequest()
                        .body( "Application with id " + aApplicationId + " was not found." );
            }
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .build();
        }
    }

    private ApplicationState getAwaitingApplicationState()
    {
        return applicationStateService.tryLoadByName( ApplicationStateEnum
                        .mapApplicationStateEnumToApplicationStateName( ApplicationStateEnum.AWAITING ) )
                .orElseThrow();
    }
}
