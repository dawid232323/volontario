package uam.volontario.handler;

import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.configuration.ConfigurationEntryKeySet;
import uam.volontario.configuration.ConfigurationEntryReader;
import uam.volontario.crud.service.*;
import uam.volontario.crud.specification.ApplicationSpecification;
import uam.volontario.dto.Application.ApplicationDto;
import uam.volontario.dto.Application.ApplicationStateCheckDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.*;
import uam.volontario.model.utils.ModelUtils;
import uam.volontario.security.mail.MailService;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.ApplicationValidationService;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private final VoluntaryPresenceStateService voluntaryPresenceStateService;

    private final ConfigurationEntryService configurationEntryService;

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
     *
     * @param aVoluntaryPresenceStateService voluntary presence state service.
     *
     * @param aConfigurationEntryService configuration entry service.
     *
     * @param aMailService mail service.
     *
     * @param aDtoService dto service.
     *
     */
    @Autowired
    public ApplicationProcessingHandler( final ApplicationService aApplicationService, final OfferService aOfferService,
                                         final UserService aUserService,
                                         final ApplicationStateService aApplicationStateService,
                                         final ApplicationValidationService aApplicationValidationService,
                                         final MailService aMailService, final DtoService aDtoService,
                                         final VoluntaryPresenceStateService aVoluntaryPresenceStateService,
                                         final ConfigurationEntryService aConfigurationEntryService )
    {
        applicationService = aApplicationService;
        offerService = aOfferService;
        userService = aUserService;
        applicationStateService = aApplicationStateService;
        applicationValidationService = aApplicationValidationService;
        mailService = aMailService;
        dtoService = aDtoService;
        voluntaryPresenceStateService = aVoluntaryPresenceStateService;
        configurationEntryService = aConfigurationEntryService;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( ApplicationProcessingHandler.class );

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
            final User volunteer  = ModelUtils.resolveVolunteer( aDto.getVolunteerId(), userService );
            final Offer offer = ModelUtils.resolveOffer( aDto.getOfferId(), offerService );

            final Application application = Application.builder()
                    .volunteer( volunteer )
                    .offer( offer )
                    .isStarred( false )
                    .participationMotivation( aDto.getParticipationMotivation() )
                    .state( ModelUtils.resolveApplicationState( ApplicationStateEnum.AWAITING, applicationStateService ) )
                    .build();

            final ValidationResult validationResult = applicationValidationService.validateEntity( application );
            if( validationResult.isValidated() )
            {
                applicationService.saveOrUpdate( application );
                if( mailService.sendApplicationCreatedMail( application ) )
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
            final Application application = ModelUtils.resolveApplication( aApplicationId, applicationService );

            application.setStarred( aStar );
            applicationService.saveOrUpdate( application );

            return ResponseEntity.ok( application );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .build();
        }
    }

    /**
     * Resolves Application's final state to either {@linkplain ApplicationStateEnum#UNDER_RECRUITMENT}
     * or {@linkplain ApplicationStateEnum#DECLINED}. After Application is resolved, Volunteer is informed
     * via his contact email about Institution's decision.
     *
     * @param aApplicationId id of Application to resolve.
     *
     * @param aDecisionReasonOptionalMap Optional with map that should contain application decline reason
     *
     * @param aDecision move under recruitment or decline.
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
            final Application application = ModelUtils.resolveApplication( aApplicationId, applicationService );

            final Offer offer = application.getOffer();
            final User volunteer = application.getVolunteer();

            switch ( aDecision )
            {
                case AWAITING -> throw new IllegalArgumentException( "Awaiting state is not a proper resolve." );
                case UNDER_RECRUITMENT -> handleUnderRecruitmentApplication( volunteer, application, offer );
                case RESERVE_LIST -> handleReserveListApplication( volunteer, application, offer );
                case DECLINED ->
                {
                    if( aDecisionReasonOptionalMap.isEmpty() )
                    {
                        return ResponseEntity.badRequest()
                                .body( "Decline reason should be present" );
                    }

                    handleDeclinedApplication( volunteer, application, offer, aDecisionReasonOptionalMap.get()
                            .get( "decisionReason" ) );
                }
            }

            return ResponseEntity.ok( application );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    // TODO: mikmum add details javadoc.
    public ResponseEntity< ? > loadApplicationInfoFiltered( final String aState, final Boolean aIsStarred, final Long aOfferId,
                                                            final Long aVolunteerId, final Long aInstitutionId, final Pageable aPageable,
                                                            final boolean aWithDetails )
    {
        try
        {
            final ApplicationSpecification specification = new ApplicationSpecification( new ApplicationSearchQuery(
                    aState, aIsStarred, aOfferId, aVolunteerId, aInstitutionId ) );

            if ( aWithDetails )
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

    // TODO: mikmum add details javadoc.
    public ResponseEntity< ? > checkState( final Long aOfferId, final Long aVolunteerId )
    {
        try
        {
            // check existence of Offer and Volunteer.
            ModelUtils.resolveOffer( aOfferId, offerService );
            ModelUtils.resolveVolunteer( aVolunteerId, userService );

            final ApplicationSpecification specification = new ApplicationSpecification( new ApplicationSearchQuery(
                    null, null, aOfferId, aVolunteerId, null ));

            final Optional< Application > optionalApplication = applicationService.findFiltered( specification, Pageable.unpaged() )
                    .get()
                    .findFirst();

            if ( optionalApplication.isEmpty() )
            {
                return ResponseEntity.ok( new ApplicationStateCheckDto( null, false, null ) );
            }

            return optionalApplication.map( value -> ResponseEntity.ok(
                    new ApplicationStateCheckDto( optionalApplication.get().getId(), true, value.getState().getName() ) ) )
                    .orElseGet( () -> ResponseEntity.ok(new ApplicationStateCheckDto( optionalApplication.get().getId(), false, null) ) );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on checking state: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    // TODO: mikmum add details javadoc.
    public ResponseEntity< ? > loadApplicationDetails( final Long aApplicationId )
    {
        try
        {
            return ResponseEntity.ok( ModelUtils.resolveApplication( aApplicationId, applicationService ) );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on loading offer types: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    private void handleUnderRecruitmentApplication( final User aVolunteer, final Application aApplication, final Offer aOffer )
            throws MessagingException, IOException
    {

        aApplication.setState( ModelUtils.resolveApplicationState( ApplicationStateEnum.UNDER_RECRUITMENT,
                applicationStateService ) );

        final VoluntaryPresenceState unresolvedPresenceState = ModelUtils.resolveVoluntaryPresenceState(
                VoluntaryPresenceStateEnum.UNRESOLVED, voluntaryPresenceStateService );

        final int leftReminderCount = ConfigurationEntryReader.readValueAsInteger(
                ConfigurationEntryKeySet.VOLUNTARY_PRESENCE_MAX_REMINDER_COUNT, configurationEntryService );

        final ConfigurationEntryKeySet appropriateConfigurationEntryKeyN = aOffer.getOfferTypeAsEnum().equals( OfferTypeEnum.ONE_TIME )
                ? ConfigurationEntryKeySet.APPLICATION_ONE_TIME_CONFIRMATION_BUFFER
                : ConfigurationEntryKeySet.APPLICATION_MULTI_TIME_CONFIRMATION_BUFFER;

        final Duration buffer = ConfigurationEntryReader.readValueAsDurationOrDefault(
                appropriateConfigurationEntryKeyN, ChronoUnit.HOURS, Duration.ofDays( 14 ), configurationEntryService );

        final Instant reminderDate = aOffer.getOfferTypeAsEnum().equals( OfferTypeEnum.ONE_TIME )
                ? aOffer.getEndDate().plus( buffer )
                :  aOffer.getStartDate().plus( buffer );

        final VoluntaryPresence voluntaryPresence = VoluntaryPresence.builder()
                .volunteer( aVolunteer )
                .offer( aOffer )
                .volunteerReportedPresenceState( unresolvedPresenceState )
                .institutionReportedPresenceState( unresolvedPresenceState )
                .wasVolunteerReminded( false )
                .wasInstitutionReminded( false )
                .volunteerLeftReminderCount( leftReminderCount )
                .institutionLeftReminderCount( leftReminderCount )
                .volunteerReminderDate( reminderDate )
                .institutionReminderDate( reminderDate )
                .build();

        aOffer.getVoluntaryPresences().add( voluntaryPresence );

        applicationService.saveOrUpdate( aApplication );
        offerService.saveOrUpdate( aOffer );

        mailService.sendEmailAboutApplicationUnderRecruitment( aVolunteer.getContactEmailAddress(),
                aOffer.getTitle() );
    }

    private void handleReserveListApplication( final User aVolunteer, final Application aApplication, final Offer aOffer )
            throws MessagingException, IOException
    {
        aApplication.setState( ModelUtils.resolveApplicationState( ApplicationStateEnum.RESERVE_LIST,
                applicationStateService) );

        applicationService.saveOrUpdate( aApplication );
        mailService.sendEmailAboutApplicationBeingMovedToReserveList( aVolunteer.getContactEmailAddress(),
                aOffer.getTitle() );
    }

    private void handleDeclinedApplication( final User aVolunteer, final Application aApplication, final Offer aOffer,
                                            final String aDecisionReason ) throws MessagingException, IOException
    {
        aApplication.setState( ModelUtils.resolveApplicationState( ApplicationStateEnum.DECLINED,
                applicationStateService ) );
        aApplication.setDecisionReason( aDecisionReason );

        applicationService.saveOrUpdate( aApplication );
        mailService.sendEmailAboutApplicationBeingDeclined( aVolunteer.getContactEmailAddress(),
                aOffer.getTitle(), aDecisionReason );
    }
}
