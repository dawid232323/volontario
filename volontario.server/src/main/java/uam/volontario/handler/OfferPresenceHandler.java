package uam.volontario.handler;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import uam.volontario.configuration.ConfigurationEntryKeySet;
import uam.volontario.configuration.ConfigurationEntryReader;
import uam.volontario.crud.service.ConfigurationEntryService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.UserService;
import uam.volontario.crud.service.VoluntaryPresenceStateService;
import uam.volontario.dto.presence.VoluntaryPresenceInstitutionDataDto;
import uam.volontario.dto.presence.VoluntaryPresenceVolunteerDataDto;
import uam.volontario.dto.user.VolunteerPresenceDto;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.*;
import uam.volontario.model.utils.ModelUtils;
import uam.volontario.security.mail.MailService;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handler related to presences of {@linkplain Offer}s.
 */
@Service
public class OfferPresenceHandler
{
    private final OfferService offerService;

    private final MailService mailService;

    private final ConfigurationEntryService configurationEntryService;

    private final UserService userService;

    private final VoluntaryPresenceStateService voluntaryPresenceStateService;

    /**
     * CDI constructor.
     *
     * @param aConfigurationEntryService configuration entry service.
     *
     * @param aOfferService offer service.
     *
     * @param aMailService mail service.
     *
     * @param aUserService user service.
     *
     * @param aVoluntaryPresenceStateService voluntary presence state service.
     */
    @Autowired
    public OfferPresenceHandler( final ConfigurationEntryService aConfigurationEntryService,
                                 final OfferService aOfferService,
                                 final MailService aMailService,
                                 final UserService aUserService,
                                 final VoluntaryPresenceStateService aVoluntaryPresenceStateService )
    {
        configurationEntryService = aConfigurationEntryService;
        offerService = aOfferService;
        mailService = aMailService;
        userService = aUserService;
        voluntaryPresenceStateService = aVoluntaryPresenceStateService;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( OfferPresenceHandler.class );

    /**
     * Resolves List of Volunteers which presence for Offer of given ID can be confirmed or negated.
     *
     * @param aOfferId id of context Offer.
     *
     * @return
     *        - Response Entity with code 200 and List of Volunteers which presence on given Offer can be confirmed.
     *        - Response Entity with code 401 when passed id does not match any existing Offer.
     */
    public ResponseEntity< ? > resolveAllVolunteersWhosePresenceCanBeConfirmed( final Long aOfferId )
    {
        final Offer offer = ModelUtils.resolveOffer( aOfferId, offerService );
        final boolean isOfferReadyToConfirmPresences = isOfferReadyToConfirmPresences( offer );

        if( isOfferReadyToConfirmPresences )
        {
            final List< User > volunteersWithConfirmablePresence = offer.getApplications()
                    .stream()
                    .filter( application -> application.getStateAsEnum().equals( ApplicationStateEnum.UNDER_RECRUITMENT ) )
                    .map( Application::getVolunteer )
                    .distinct()
                    .toList();

            return ResponseEntity.ok( volunteersWithConfirmablePresence );
        }

        return ResponseEntity.badRequest()
                .body( String.format( "No Volunteers have their presence confirmable for Offer: %s", offer.getTitle() ) );
    }

    /**
     * Checks whether Volunteers' presence on given Offer can be confirmed.
     *
     * @param aOfferId id of Offer.
     *
     * @return
     *        - Response Entity with code 200 and true/false which depends on whether presence can be confirmed.
     *        - Response Entity with code 401 when passed id does not match any existing Offer.
     */
    public ResponseEntity< ? > isOfferReadyToConfirmPresences( final Long aOfferId )
    {
        return ResponseEntity.ok( Map.of( "isOfferReadyToConfirmPresences", isOfferReadyToConfirmPresences(
                ModelUtils.resolveOffer( aOfferId, offerService ) ) ) );
    }

    /**
     * Loads state of {@linkplain VoluntaryPresence}.
     *
     * @param aVolunteerId volunteer id.
     *
     * @param aOfferId offer id.
     *
     * @return
     *        - Response Entity with code 200 and state as {@linkplain VoluntaryPresenceVolunteerDataDto}.
     *        - Response Entity with code 400 if Application for given Offer does not exist, or it was not accepted,
     *          or User of given ID does not exist or is not a Volunteer.
     *        - Response Entity with code 501 in case of unexpected server side error.
     */
    public ResponseEntity< ? > loadVoluntaryPresenceStateOfVolunteer( final Long aVolunteerId, final Long aOfferId )
    {
        try
        {
            final Offer offer = ModelUtils.resolveOffer( aOfferId, offerService );
            final User volunteer = ModelUtils.resolveVolunteer( aVolunteerId, userService );

            final Optional< VoluntaryPresence > optionalVoluntaryPresence = volunteer.getVoluntaryPresences().stream()
                    .filter( voluntaryPresence -> voluntaryPresence.getOffer()
                            .equals( offer ) )
                    .findAny();

            if( optionalVoluntaryPresence.isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( String.format(
                                "Presence data of Volunteer %s can not be loaded because his Application on Offer %s was never accepted",
                                volunteer.getUsername(), offer.getTitle() ) );
            }

            final VoluntaryPresence voluntaryPresence = optionalVoluntaryPresence.get();

            final Duration changeDecisionBuffer = ConfigurationEntryReader.readValueAsDurationOrDefault(
                    ConfigurationEntryKeySet.VOL_VOLUNTARY_PRESENCE_DECISION_CHANGE_BUFFER, ChronoUnit.HOURS,
                    Duration.ofDays( 14 ), configurationEntryService );

            final boolean canDecisionBeChanged = canDecisionBeChanged( voluntaryPresence.getVolunteerDecisionDate(),
                    changeDecisionBuffer );
            final boolean canPostponeReminder = voluntaryPresence.getVolunteerLeftReminderCount() > 0
                    && voluntaryPresence.isWasVolunteerReminded();

            final VoluntaryPresenceVolunteerDataDto dto = VoluntaryPresenceVolunteerDataDto.builder()
                    .confirmationState( voluntaryPresence.getVolunteerReportedPresenceStateAsEnum() )
                    .canDecisionBeChanged( canDecisionBeChanged )
                    .decisionChangeDeadlineDate( canDecisionBeChanged ? voluntaryPresence.getVolunteerDecisionDate()
                            .plus( changeDecisionBuffer ) : null )
                    .canPostponeReminder( canPostponeReminder )
                    .build();

            return ResponseEntity.ok( dto );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads state of {@linkplain VoluntaryPresence}.
     *
     * @param aOfferId offer id.
     *
     * @return
     *        - Response Entity with code 200 and state as {@linkplain VoluntaryPresenceVolunteerDataDto}.
     *        - Response Entity with code 400 if Offer does not exist.
     *        - Response Entity with code 501 in case of unexpected server side error.
     */
    public ResponseEntity< ? > loadVoluntaryPresenceStateOfInstitution( final Long aOfferId )
    {
        try
        {
            final Offer offer = ModelUtils.resolveOffer( aOfferId, offerService );

            if( offer.getVoluntaryPresences().isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( String.format(
                                "Presence data of Institution %s can not be loaded because Offer %s never had any accepted Applications",
                                offer.getInstitution().getName(), offer.getTitle() ) );
            }

            final VoluntaryPresence anyVoluntaryPresence = offer.getVoluntaryPresences().stream()
                    .findAny()
                    .orElseThrow( IllegalStateException::new );

            final Duration changeDecisionBuffer = ConfigurationEntryReader.readValueAsDurationOrDefault(
                    ConfigurationEntryKeySet.VOL_VOLUNTARY_PRESENCE_DECISION_CHANGE_BUFFER, ChronoUnit.HOURS,
                    Duration.ofDays( 14 ), configurationEntryService );

            final boolean canDecisionBeChanged = canDecisionBeChanged( anyVoluntaryPresence.getInstitutionDecisionDate(),
                    changeDecisionBuffer );
            final boolean canPostponeReminder = anyVoluntaryPresence.getInstitutionLeftReminderCount() > 0
                    && anyVoluntaryPresence.isWasInstitutionReminded();

            final VoluntaryPresenceInstitutionDataDto dto = VoluntaryPresenceInstitutionDataDto.builder()
                    .wasPresenceConfirmed( !anyVoluntaryPresence.getInstitutionReportedPresenceStateAsEnum()
                            .equals( VoluntaryPresenceStateEnum.UNRESOLVED ) )
                    .canDecisionBeChanged( canDecisionBeChanged )
                    .decisionChangeDeadlineDate( canDecisionBeChanged ? anyVoluntaryPresence.getInstitutionDecisionDate()
                            .plus( changeDecisionBuffer ) : null )
                    .canPostponeReminder( canPostponeReminder )
                    .build();

            return ResponseEntity.ok( dto );
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
     * @param aVolunteerPresencesDto List containing IDs of Volunteers and their presence states.
     *
     * @param aOfferId id of Offer.
     **
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 401 if User/Offer of given id was not found or if Application was not accepted.
     *        - Response Entity with code 500 in case of any unexpected server side error.
     */
    public ResponseEntity< ? > changeInstitutionReportedPresenceState( final List< VolunteerPresenceDto > aVolunteerPresencesDto,
                                                                       final Long aOfferId )
    {
        try
        {
            final List< User > volunteersToSave = Lists.newArrayList();
            final Offer offer = ModelUtils.resolveOffer( aOfferId, offerService );
            final Duration changeDecisionBuffer = ConfigurationEntryReader.readValueAsDurationOrDefault(
                    ConfigurationEntryKeySet.VOL_VOLUNTARY_PRESENCE_DECISION_CHANGE_BUFFER, ChronoUnit.HOURS,
                    Duration.ofDays( 14 ), configurationEntryService );

            for( final VolunteerPresenceDto volunteerPresenceDto : aVolunteerPresencesDto )
            {
                final User volunteer = ModelUtils.resolveVolunteer( volunteerPresenceDto.getVolunteerId(), userService );

                final Optional< VoluntaryPresence > optionalVoluntaryPresence = volunteer.getVoluntaryPresences().stream()
                        .filter( voluntaryPresence -> voluntaryPresence.getOffer()
                                .equals( offer ) )
                        .findAny();

                if( optionalVoluntaryPresence.isEmpty() )
                {
                    return ResponseEntity.badRequest()
                            .body( String.format( "Institution %s can not confirm/deny Volunteer %s presence on Offer %s because his Application was not accepted",
                                    offer.getInstitution().getName(), volunteer.getUsername(), offer.getTitle() ) );
                }

                final VoluntaryPresence voluntaryPresence = optionalVoluntaryPresence.get();

                if( !canPresenceStateBeSet( voluntaryPresence.getInstitutionReportedPresenceStateAsEnum(),
                        voluntaryPresence.getInstitutionDecisionDate(), changeDecisionBuffer ) )
                {
                    return ResponseEntity.badRequest()
                            .body( String.format( "Time to change decision has expired at: %s",
                                    voluntaryPresence.getInstitutionDecisionDate().plus( changeDecisionBuffer ) ) );
                }

                switch ( volunteerPresenceDto.getPresenceState() )
                {
                    case CONFIRMED, DENIED ->
                    {
                        voluntaryPresence.setInstitutionReportedPresenceState(
                                ModelUtils.resolveVoluntaryPresenceState( volunteerPresenceDto.getPresenceState(),
                                        voluntaryPresenceStateService ) );
                        voluntaryPresence.setInstitutionDecisionDate( Instant.now() );
                    }
                    case UNRESOLVED -> throw new UnsupportedOperationException(
                            "Voluntary Presence State 'Unresolved' is initial state and can not be set again." );
                }

                if( voluntaryPresence.isPresenceConfirmed() )
                {
                    mailService.sendMailToVolunteerAboutPossibilityToRateInstitution( voluntaryPresence );
                    mailService.sendMailToInstitutionAboutPossibilityToRateVolunteer( voluntaryPresence );
                }

                volunteersToSave.add( volunteer );
            }

            userService.saveOrUpdateAll( volunteersToSave );

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
     * @param aOfferId id of Offer to postpone presence confirmation.
     *
     *
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 401 if Offer was not found.
     *        - Response Entity with code 500 in case of an unexpected server side error.
     */
    public ResponseEntity< ? > postponePresenceConfirmation( final Long aOfferId )
    {
        try
        {
            final Offer offer = ModelUtils.resolveOffer( aOfferId, offerService );

            final List< VoluntaryPresence > voluntaryPresences = offer.getVoluntaryPresences();

            if( voluntaryPresences.isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( String.format( "Offer %s can not postpone confirmation because it never accepted any Volunteers",
                                offer.getTitle() ) );
            }

            final VoluntaryPresence anyVoluntaryPresence = voluntaryPresences.stream()
                    .findAny()
                    .orElseThrow( IllegalStateException::new );

            if( anyVoluntaryPresence.getInstitutionLeftReminderCount() <= 0 )
            {
                return ResponseEntity.badRequest()
                        .body( String.format( "Offer %s has already reached the maximum allowed numbers of postponing",
                                offer.getTitle() ) );
            }

            final Duration reminderPostponeTime = ConfigurationEntryReader.readValueAsDurationOrDefault(
                    ConfigurationEntryKeySet.VOLUNTARY_PRESENCE_POSTPONE_CONFIRMATION_TIME, ChronoUnit.HOURS,
                    Duration.ofDays( 7 ), configurationEntryService );

            voluntaryPresences.forEach( voluntaryPresence ->
            {
                voluntaryPresence.setInstitutionLeftReminderCount( voluntaryPresence.getInstitutionLeftReminderCount() - 1 );
                voluntaryPresence.setInstitutionReminderDate( voluntaryPresence.getInstitutionReminderDate()
                        .plus( reminderPostponeTime ) );
                voluntaryPresence.setWasInstitutionReminded( false );
            } );

            offerService.saveOrUpdate( offer );

            return ResponseEntity.ok()
                    .build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    private boolean isOfferReadyToConfirmPresences( final Offer aOffer )
    {
        final boolean isOneTimeOffer = aOffer.getOfferTypeAsEnum()
                .equals( OfferTypeEnum.ONE_TIME );

        final ConfigurationEntryKeySet appropriateConfigurationEntryKey = isOneTimeOffer ? ConfigurationEntryKeySet.APPLICATION_ONE_TIME_CONFIRMATION_BUFFER
                : ConfigurationEntryKeySet.APPLICATION_MULTI_TIME_CONFIRMATION_BUFFER;

        final Duration buffer = ConfigurationEntryReader.readValueAsDurationOrDefault(
                appropriateConfigurationEntryKey, ChronoUnit.HOURS, Duration.ofDays( 14 ), configurationEntryService );

        if( isOneTimeOffer )
        {
            return Duration.between( aOffer.getEndDate(), Instant.now() )
                    .compareTo( buffer ) >= 0;
        }
        else
        {
            return Duration.between( aOffer.getStartDate(), Instant.now() )
                    .compareTo( buffer ) >= 0;
        }
    }

    private boolean canDecisionBeChanged( final @Nullable Instant aDecisionDate,
                                          final Duration aChangeDecisionBuffer )
    {
        return aDecisionDate != null && Duration.between( aDecisionDate, Instant.now() )
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
}
