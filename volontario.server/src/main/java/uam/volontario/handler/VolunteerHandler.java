package uam.volontario.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uam.volontario.configuration.ConfigurationEntryKeySet;
import uam.volontario.configuration.ConfigurationEntryReader;
import uam.volontario.crud.service.*;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.dto.VolunteerPatchInfoDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.VoluntaryPresence;
import uam.volontario.model.offer.impl.VoluntaryPresenceStateEnum;
import uam.volontario.model.offer.impl.VoluntaryRating;
import uam.volontario.model.utils.ModelUtils;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.rest.VolunteerController;
import uam.volontario.security.mail.MailService;
import uam.volontario.security.util.VolontarioBase64Coder;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.UserValidationService;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for handling operations related to {@linkplain uam.volontario.model.common.impl.User}s of type
 * {@linkplain uam.volontario.model.common.UserRole#VOLUNTEER}.
 */
@Service
public class VolunteerHandler
{
    private final DtoService dtoService;

    private final UserService userService;

    private final UserValidationService userValidationService;

    private final InterestCategoryService interestCategoryService;

    private final ExperienceLevelService experienceLevelService;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final VoluntaryPresenceStateService voluntaryPresenceStateService;

    private final ConfigurationEntryService configurationEntryService;

    private final OfferService offerService;
    private final VoluntaryRatingService voluntaryRatingService;

    /**
     * CDI constructor.
     *
     * @param aUserValidationService user validation service.
     *
     * @param aDtoService dto service.
     *
     * @param aUserService user service.
     *
     * @param aPasswordEncoder password encoder.
     *
     * @param aInterestCategoryService interest category service.
     *
     * @param aExperienceLevelService experience level service.
     *
     * @param aMailService mail service.
     *
     * @param aVoluntaryPresenceStateService voluntary presence state service.
     *
     * @param aOfferService offer service.
     *
     * @param aConfigurationEntryService configuration entry service.
     */
    @Autowired
    public VolunteerHandler( final UserValidationService aUserValidationService, final DtoService aDtoService,
                             final UserService aUserService, final PasswordEncoder aPasswordEncoder,
                             final InterestCategoryService aInterestCategoryService,
                             final ExperienceLevelService aExperienceLevelService,
                             final MailService aMailService,
                             final VoluntaryPresenceStateService aVoluntaryPresenceStateService,
                             final ConfigurationEntryService aConfigurationEntryService,
                             final OfferService aOfferService,
                             final VoluntaryRatingService aVoluntaryRatingService )
    {
        dtoService = aDtoService;
        userService = aUserService;
        passwordEncoder = aPasswordEncoder;
        userValidationService = aUserValidationService;
        interestCategoryService = aInterestCategoryService;
        experienceLevelService = aExperienceLevelService;
        mailService = aMailService;
        voluntaryPresenceStateService = aVoluntaryPresenceStateService;
        configurationEntryService = aConfigurationEntryService;
        offerService = aOfferService;
        voluntaryRatingService = aVoluntaryRatingService;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( VolunteerController.class );

    /**
     * Registers volunteer.
     *
     * @param aDto dto containing registration data.
     *
     * @return if volunteer passes validation, then ResponseEntity with 201 status and volunteer. If volunteer did not
     *         pass validation then ResponseEntity with 400 status and constraints violated. If there was an error,
     *         then ResponseEntity with 500 status and error message.
     */
    public ResponseEntity< ? > registerVolunteer( final VolunteerDto aDto )
    {
        try
        {
            final User user = dtoService.createVolunteerFromDto( aDto );
            final ValidationResult validationResult = userValidationService.validateEntity( user );

            if( validationResult.isValidated() )
            {
                user.setHashedPassword( passwordEncoder.encode( user.getPassword() ) );

                userService.saveOrUpdate( user );

                mailService.sendMailToVolunteerAboutRegistrationConfirmation( user );

                LOGGER.debug( "A new user has been registered for contact email {}, userId: {}",
                        user.getContactEmailAddress(), user.getId() );

                return ResponseEntity.status( HttpStatus.CREATED )
                        .body( validationResult.getValidatedEntity() );
            }

            LOGGER.debug( "Validation failed for new user with contact email {}, violations: {}", aDto.getContactEmail(),
                    validationResult.getValidationViolations().values() );

            return ResponseEntity.badRequest()
                    .body( validationResult.getValidationViolations() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Exception occurred during registration of volunteer: {}", aE.getMessage(), aE );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Confirms registration of Volunteer.
     *
     * @param aVolunteerId id of Volunteer to confirm registration.
     *
     * @param aToken token containing encoded domain email passes in registration.
     *
     * @return
     *        - Response Entity with status 200 if everything went as expected.
     *        - Response Entity with status 400 if User of provided id was not found or was not volunteer, or if
     *          provided token is not equal to Volunteer's domain email after decoding, or is Volunteer was already confirmed.
     *        - Response Entity with code 501 in case of any unexpected server side error.
     */
    public ResponseEntity< ? > confirmVolunteerRegistration( final Long aVolunteerId, final String aToken )
    {
        try
        {
            final User volunteer = ModelUtils.resolveVolunteer( aVolunteerId, userService );

            if( !volunteer.getVolunteerData().getDomainEmailAddress()
                    .equals( VolontarioBase64Coder.decode( aToken ) ) )
            {
                return ResponseEntity.badRequest()
                        .body( String.format( "Illegal access to confirm registration of volunteer: %s",
                                volunteer.getUsername() ) );
            }

            if( volunteer.isVerified() )
            {
                return ResponseEntity.status( HttpStatus.ALREADY_REPORTED )
                        .body( String.format( "Volunteer %s has already confirmed registration",
                                volunteer.getUsername() ) );
            }

            volunteer.setVerified( true );

            userService.saveOrUpdate( volunteer );

            return ResponseEntity.ok()
                    .build();

        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Updates Volunteer's contact data, experience level, interest categories and participation motivation. If
     * some of mentioned data is not provided in dto then update on those properties is not performed.
     *
     * @param aVolunteerId volunteer id.
     *
     * @param aPatchDto volunteer patch info dto.
     *
     * @return
     *        - Response Entity with code 200 and patched Volunteer if everything went as expected.
     *        - Response Entity with code 400 if:
     *             1. There is no Volunteer with provided id found.
     *             2. Provided id belongs to User who is not Volunteer.
     *             3. Volunteer does not pass validation after patch (in this case also validation violations
     *                                                                are provided within the Response)
     *
     *        - Response Entity with code 500 when unexpected server-side error occurs.
     */
    public ResponseEntity< ? > updateVolunteerInformation( final Long aVolunteerId,
                                                           final VolunteerPatchInfoDto aPatchDto )
    {
        try
        {
            final User volunteer = ModelUtils.resolveVolunteer( aVolunteerId, userService );
            if( volunteer.hasUserRole( UserRole.VOLUNTEER ) )
            {
                final VolunteerData volunteerData = volunteer.getVolunteerData();

                Optional.ofNullable( aPatchDto.getContactEmailAddress() )
                        .ifPresent( volunteer::setContactEmailAddress );
                Optional.ofNullable( aPatchDto.getPhoneNumber() )
                        .ifPresent( volunteer::setPhoneNumber );
                Optional.ofNullable( aPatchDto.getParticipationMotivation() )
                        .ifPresent( volunteerData::setParticipationMotivation );

                Optional.ofNullable( aPatchDto.getInterestCategoriesIds() )
                        .ifPresent( idList -> volunteerData.setInterestCategories( idList.stream()
                                .map( interestCategoryService::loadEntity )
                                .collect( Collectors.toList() ) ) );
                Optional.ofNullable( aPatchDto.getExperienceId() )
                        .ifPresent( id -> volunteerData.setExperience( experienceLevelService.loadEntity( id ) ) );

                final ValidationResult validationResult = userValidationService.validateEntity( volunteer );
                if( validationResult.isValidated() )
                {
                    userService.saveOrUpdate( volunteer );
                    return ResponseEntity.ok( validationResult.getValidatedEntity() );
                }
                else
                {
                    return ResponseEntity.badRequest()
                            .body( validationResult.getValidationViolations() );
                }

            }

            return ResponseEntity.badRequest()
                    .body( MessageGenerator.getUserNotFoundMessage( aVolunteerId ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Edits volunteer interests.
     *
     * @param aUserId id of volunteer to edit
     *
     * @param interests string with interests description
     *
     * @return response entity with status 200 if everything is saved correctly
     */
    public ResponseEntity< ? > patchVolunteerInterests( final Long aUserId, final String interests )
    {
        final User user = ModelUtils.resolveVolunteer( aUserId, userService );
        user.getVolunteerData().setInterests( interests );

        this.userService.saveOrUpdate( user );
        return ResponseEntity.ok( user );
    }

    /**
     * Edits volunteer experience description.
     *
     * @param aUserId id of volunteer to edit
     *
     * @param experienceDescription string with experience description
     *
     * @return response entity with status 200 if everything is saved correctly
     */
    public ResponseEntity< ? > patchVolunteerExperienceDescription( final Long aUserId,
                                                                    final String experienceDescription )
    {
        final User user = ModelUtils.resolveVolunteer( aUserId, userService );
        user.getVolunteerData().setExperienceDescription( experienceDescription );

        this.userService.saveOrUpdate( user );
        return ResponseEntity.ok( user );
    }

    /**
     * Resolves all {@linkplain uam.volontario.model.offer.impl.Offer}s in which Volunteer of provided id participated.
     *
     * @param aVolunteerId Volunteer id.
     *
     * @return
     *        - Response Entity with code 200 and List of Offers in which Volunteer participated
     *        - Response Entity with code 401 if no user of given id was found or if user was found, but was not volunteer.
     *        - Response Entity with code 500 in case of any unexpected server side error.
     */
    public ResponseEntity< ? > resolveAllPresencesOfVolunteer( final Long aVolunteerId )
    {
        try
        {
            final List< Offer > offersInWhichUserParticipated = ModelUtils.resolveVolunteer( aVolunteerId, userService )
                    .getVoluntaryPresences().stream()
                    .filter( voluntaryPresence -> voluntaryPresence.getVolunteerReportedPresenceStateAsEnum().equals( VoluntaryPresenceStateEnum.CONFIRMED )
                                && voluntaryPresence.getInstitutionReportedPresenceStateAsEnum().equals( VoluntaryPresenceStateEnum.CONFIRMED ) )
                    .map( VoluntaryPresence::getOffer )
                    .distinct()
                    .toList();

            return ResponseEntity.ok( offersInWhichUserParticipated.stream()
                    .map( dtoService::createBaseInfoDtoOfOffer )
                    .toList() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Changes volunteer reported presence state to either CONFIRMED or DENIED.
     *
     * @param aVolunteerId id of Volunteer to change volunteer reported state.
     *
     * @param aOfferId id of Offer.
     *
     * @param aVoluntaryPresenceState voluntary presence state to be applied.
     *
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 401 if User/Offer of given id was not found or if Application for Offer was not accepted.
     *        - Response Entity with code 500 in case of any unexpected server side error.
     */
    public ResponseEntity< ? > changeVolunteerReportedPresenceState( final Long aVolunteerId, final Long aOfferId,
                                                                     final VoluntaryPresenceStateEnum aVoluntaryPresenceState )
    {
        try
        {
            final User volunteer = ModelUtils.resolveVolunteer( aVolunteerId, userService );
            final Offer offer = ModelUtils.resolveOffer( aOfferId, offerService );

            final Optional< VoluntaryPresence > optionalVoluntaryPresence = volunteer.getVoluntaryPresences().stream()
                    .filter( voluntaryPresence -> voluntaryPresence.getOffer()
                            .equals( offer ) )
                    .findAny();

            if( optionalVoluntaryPresence.isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( String.format( "Volunteer %s can not confirm/deny his presence on Offer %s because his Application was not accepted",
                                volunteer.getUsername(), offer.getTitle() ) );
            }

            final Duration changeDecisionBuffer = ConfigurationEntryReader.readValueAsDurationOrDefault(
                    ConfigurationEntryKeySet.VOL_VOLUNTARY_PRESENCE_DECISION_CHANGE_BUFFER, ChronoUnit.HOURS,
                    Duration.ofDays( 14 ), configurationEntryService );

            final VoluntaryPresence voluntaryPresence = optionalVoluntaryPresence.get();

            if( !canPresenceStateBeSet( voluntaryPresence.getVolunteerReportedPresenceStateAsEnum(),
                    voluntaryPresence.getVolunteerDecisionDate(), changeDecisionBuffer ) )
            {
                return ResponseEntity.badRequest()
                        .body( String.format( "Time to change decision has expired at: %s",
                                voluntaryPresence.getVolunteerDecisionDate().plus( changeDecisionBuffer ) ) );
            }

            switch ( aVoluntaryPresenceState )
            {
                case CONFIRMED, DENIED ->
                {
                    voluntaryPresence.setVolunteerReportedPresenceState(
                            ModelUtils.resolveVoluntaryPresenceState( aVoluntaryPresenceState, voluntaryPresenceStateService ) );
                    voluntaryPresence.setVolunteerDecisionDate( Instant.now() );
                }
                case UNRESOLVED -> throw new UnsupportedOperationException(
                        "Voluntary Presence State 'Unresolved' is initial state and can not be set again." );
            }

            if( voluntaryPresence.isPresenceConfirmed() )
            {
                createOfferRating( voluntaryPresence.getOffer().getInstitution(), voluntaryPresence.getOffer(), volunteer );
                mailService.sendMailToVolunteerAboutPossibilityToRateInstitution( voluntaryPresence );
                mailService.sendMailToInstitutionAboutPossibilityToRateVolunteer( voluntaryPresence );
            }

            userService.saveOrUpdate( volunteer );

            return ResponseEntity.ok()
                    .build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Postpones Voluntary Presence Confirmation on Volunteer's side by 7 days.
     *
     * @param aVolunteerId id of Volunteer to postpone presence confirmation.
     *
     * @param aOfferId id of Offer.
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 401 if User/Offer of given id was not found or if Application for Offer was not accepted.
     *        - Response Entity with code 500 in case of an unexpected server side error.
     */
    public ResponseEntity< ? > postponePresenceConfirmation( final Long aVolunteerId, final Long aOfferId )
    {
        try
        {
            final User volunteer = ModelUtils.resolveVolunteer( aVolunteerId, userService );
            final Offer offer = ModelUtils.resolveOffer( aOfferId, offerService );

            final Optional< VoluntaryPresence > optionalVoluntaryPresence = volunteer.getVoluntaryPresences().stream()
                    .filter( voluntaryPresence -> voluntaryPresence.getOffer()
                            .equals( offer ) )
                    .findAny();

            if( optionalVoluntaryPresence.isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( String.format( "Volunteer %s can not postpone confirmation of his presence on Offer %s because his Application was not accepted",
                                volunteer.getUsername(), offer.getTitle() ) );
            }

            final VoluntaryPresence voluntaryPresence = optionalVoluntaryPresence.get();

            if( voluntaryPresence.getVolunteerLeftReminderCount() <= 0 )
            {
                return ResponseEntity.badRequest()
                        .body( String.format( "Volunteer %s has already reached the maximum allowed numbers of postponing Offer %s.",
                                volunteer.getUsername(),
                                offer.getTitle() ) );
            }

            final Duration reminderPostponeTime = ConfigurationEntryReader.readValueAsDurationOrDefault(
                    ConfigurationEntryKeySet.VOLUNTARY_PRESENCE_POSTPONE_CONFIRMATION_TIME, ChronoUnit.HOURS,
                    Duration.ofDays( 7 ), configurationEntryService );

            voluntaryPresence.setVolunteerLeftReminderCount( voluntaryPresence.getVolunteerLeftReminderCount() - 1 );
            voluntaryPresence.setVolunteerReminderDate( voluntaryPresence.getVolunteerReminderDate()
                    .plus( reminderPostponeTime ) );
            voluntaryPresence.setWasVolunteerReminded( false );

            userService.saveOrUpdate( volunteer );

            return ResponseEntity.ok()
                    .build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    private boolean canDecisionBeChanged( final @Nullable Instant aVolunteerDecisionDate,
                                          final Duration aChangeDecisionBuffer )
    {
        return aVolunteerDecisionDate != null && Duration.between( aVolunteerDecisionDate, Instant.now() )
                        .compareTo( aChangeDecisionBuffer ) <= 0;
    }

    private boolean canPresenceStateBeSet( final VoluntaryPresenceStateEnum aPresenceState,
                                           final @Nullable Instant aDecisionDate,
                                           final Duration aChangeDecisionBuffer )
    {
        return aPresenceState.equals( VoluntaryPresenceStateEnum.UNRESOLVED )
                || canDecisionBeChanged( aDecisionDate,
                aChangeDecisionBuffer );
    }

    private void createOfferRating( final Institution aInstitution, final Offer aOffer, final User aVolunteer )
    {
        final VoluntaryRating voluntaryRating = VoluntaryRating.builder()
                .institution( aInstitution )
                .volunteer( aVolunteer )
                .offer( aOffer )
                .build();
        this.voluntaryRatingService.saveOrUpdate( voluntaryRating );
    }
}
