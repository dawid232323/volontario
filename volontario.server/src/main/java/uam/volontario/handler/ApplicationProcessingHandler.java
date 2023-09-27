package uam.volontario.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.ApplicationService;
import uam.volontario.crud.service.ApplicationStateService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.UserService;
import uam.volontario.crud.specification.ApplicationSpecification;
import uam.volontario.dto.Application.ApplicationDto;
import uam.volontario.dto.Application.ApplicationStateCheckDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.*;
import uam.volontario.security.mail.MailService;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.ApplicationValidationService;

import java.util.Map;
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
    private final DtoService dtoService;

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
                                         final MailService aMailService, final DtoService aDtoService )
    {
        applicationService = aApplicationService;
        offerService = aOfferService;
        userService = aUserService;
        applicationStateService = aApplicationStateService;
        applicationValidationService = aApplicationValidationService;
        mailService = aMailService;
        dtoService = aDtoService;
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
                        .body( MessageGenerator.getUserNotFoundMessage( aDto ) );
            }
            else if( optionalOffer.isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( MessageGenerator.getOfferNotFoundMessage( aDto ) );
            }

            final Application application = Application.builder()
                    .volunteer( optionalUser.get() )
                    .offer( optionalOffer.get() )
                    .isStarred( false )
                    .participationMotivation( aDto.getParticipationMotivation() )
                    .state( getApplicationState( ApplicationStateEnum.AWAITING ) )
                    .build();

            final ValidationResult validationResult = applicationValidationService.validateEntity( application );
            if( validationResult.isValidated() )
            {
                applicationService.saveOrUpdate( application );
                if( mailService.sendApplicationCreatedMailToVolunteer( application ) )
                {
                    return ResponseEntity.status( HttpStatus.CREATED )
                            .body( application );
                }
                else
                {
                    return ResponseEntity.status( HttpStatus.MULTI_STATUS )
                            .body( "Application was created, but email was not send to volunteer." );
                }
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
                    .body( aE.getMessage() );
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
                        .body( MessageGenerator.getApplicationNotFoundMessage( aApplicationId ) );
            }
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .build();
        }
    }

    /**
     * Resolves Application's final state to either {@linkplain ApplicationStateEnum#ACCEPTED}
     * or {@linkplain ApplicationStateEnum#DECLINED}. After Application is resolved, Volunteer is informed
     * via his contact email about Institution's decision.
     *
     * @param aApplicationId id of Application to resolve.
     *
     * @param aDecisionReasonOptionalMap Optional with map that should contain application decline reason
     *
     * @param aDecision acceptance or decline.
     *
     * @return
     *          - Response Entity with modified Application's state and 200 code, if everything went well.
     *          - Response Entity with code 400, if there is no Application with given id,
     *            or there is no decline reason given
     *          - Response Entity with code 500, if aDecision is {@linkplain ApplicationStateEnum#AWAITING} or any
     *            other unexpected error occurs.
     */
    public ResponseEntity< ? > resolveApplication( final Long aApplicationId, final ApplicationStateEnum aDecision,
                                                   final Optional< Map<String, String> > aDecisionReasonOptionalMap )
    {
        try
        {
            final Optional< Application > optionalApplication = applicationService.tryLoadEntity( aApplicationId );
            if( optionalApplication.isPresent() )
            {
                final Application application = optionalApplication.get();

                final Offer offer = application.getOffer();
                final User volunteer = application.getVolunteer();

                switch ( aDecision )
                {
                    case AWAITING -> throw new IllegalArgumentException( "Awaiting state is not a proper resolve." );
                    case ACCEPTED ->
                    {
                        application.setState( getApplicationState( ApplicationStateEnum.ACCEPTED ) );
                        applicationService.saveOrUpdate( application );
                        mailService.sendEmailAboutApplicationBeingAccepted( volunteer.getContactEmailAddress(),
                                offer.getTitle() );
                    }
                    case RESERVE_LIST ->
                    {
                        application.setState( getApplicationState( ApplicationStateEnum.RESERVE_LIST ) );
                        applicationService.saveOrUpdate( application );
                        mailService.sendEmailAboutApplicationBeingMovedToReserveList( volunteer.getContactEmailAddress(),
                                offer.getTitle() );
                    }
                    case DECLINED ->
                    {
                        if( aDecisionReasonOptionalMap.isEmpty() )
                        {
                            return ResponseEntity.badRequest().body( "Decline reason should be present" );
                        }
                        final Map< String, String > reasonMap = aDecisionReasonOptionalMap.get();
                        final String decisionReason = reasonMap.get( "decisionReason" );
                        application.setState( getApplicationState( ApplicationStateEnum.DECLINED ) );
                        application.setDecisionReason( decisionReason );
                        applicationService.saveOrUpdate( application );
                        mailService.sendEmailAboutApplicationBeingDeclined( volunteer.getContactEmailAddress(),
                                offer.getTitle(), decisionReason );
                    }
                }
                return ResponseEntity.ok( application );
            }

            return ResponseEntity.badRequest()
                    .body( MessageGenerator.getApplicationNotFoundMessage( aApplicationId ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    private ApplicationState getApplicationState( final ApplicationStateEnum aApplicationStateEnum )
    {
        return applicationStateService.tryLoadByName( aApplicationStateEnum.getTranslatedState() )
                .orElseThrow();
    }


    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( CrudOfferDataHandler.class );

    public ResponseEntity< ? > loadApplicationInfoFiltered( String state, Boolean isStarred, Long offerId,
                                                            Long volunteerId, Long institutionId, Pageable aPageable,
                                                            boolean withDetails )
    {
        ApplicationSearchQuery query = new ApplicationSearchQuery( state, isStarred, offerId, volunteerId, institutionId );
        ApplicationSpecification specification = new ApplicationSpecification( query );

        try
        {
            if ( withDetails )
            {
                return ResponseEntity.ok( applicationService.findFiltered( specification, aPageable )
                        .map( dtoService::createApplicationDetailsDto ) );
            }
            return ResponseEntity.ok( applicationService.findFiltered( specification, aPageable )
                    .map( dtoService::createApplicationBaseInfosDto ) );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on loading applications: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    public ResponseEntity< ? > checkState( Long offerId, Long volunteerId )
    {
        try {
            Optional< Offer > offer = offerService.tryLoadEntity(offerId);
            if ( offer.isEmpty())
            {
                return ResponseEntity.badRequest()
                        .body( MessageGenerator.getOfferNotFoundMessage( offerId ) );
            }
            Optional< User > volunteer = userService.tryLoadEntity( volunteerId );
            if ( volunteer.isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( MessageGenerator.getVolunteerNotFoundMessage( volunteerId ) );
            }

            ApplicationSearchQuery query = new ApplicationSearchQuery( null, null, offerId, volunteerId, null );
            ApplicationSpecification specification = new ApplicationSpecification( query );

            Optional<Application> application = applicationService.findFiltered(specification, Pageable.unpaged()).get().findFirst();

            if ( application.isEmpty() )
            {
                return ResponseEntity.ok( new ApplicationStateCheckDto( null, false, null ) );
            }

            return application.map( value -> ResponseEntity.ok(
                    new ApplicationStateCheckDto( application.get().getId(), true, value.getState().getName() ) ) )
                    .orElseGet( () -> ResponseEntity.ok(new ApplicationStateCheckDto( application.get().getId(), false, null) ) );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on checking state: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    public ResponseEntity<?> loadApplicationDetails( Long aApplicationId )
    {
        try {
            Optional<Application> application = applicationService.tryLoadEntity( aApplicationId );
            if ( application.isPresent() )
            {
                return ResponseEntity.ok( dtoService.createApplicationDetailsDto( application.get() ) );
            }
            return ResponseEntity.status( HttpStatus.NOT_FOUND )
                    .body( MessageGenerator.getApplicationNotFoundMessage( aApplicationId ) );
        }
        catch ( Exception aE)
        {
            LOGGER.error( "Error on loading offer types: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
