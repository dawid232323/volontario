package uam.volontario.scheduler;

import jakarta.mail.MessagingException;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uam.volontario.configuration.ConfigurationEntryKeySet;
import uam.volontario.configuration.ConfigurationEntryReader;
import uam.volontario.crud.service.ConfigurationEntryService;
import uam.volontario.crud.service.VoluntaryPresenceService;
import uam.volontario.crud.service.VoluntaryPresenceStateService;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.VoluntaryPresence;
import uam.volontario.model.offer.impl.VoluntaryPresenceStateEnum;
import uam.volontario.model.utils.ModelUtils;
import uam.volontario.security.mail.MailService;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Scheduler for handling {@linkplain uam.volontario.model.offer.impl.VoluntaryPresence}s.
 */
@Service
public class PresenceScheduler
{
    private final MailService mailService;

    private final VoluntaryPresenceService voluntaryPresenceService;

    private final VoluntaryPresenceStateService voluntaryPresenceStateService;

    private final ConfigurationEntryService configurationEntryService;

    private Instant now;

    /**
     * CDI constructor.
     *
     * @param aMailService mail service.
     *
     * @param aVoluntaryPresenceService voluntary presence service.
     *
     * @param aVoluntaryPresenceStateService voluntary presence state service.
     *
     * @param aConfigurationEntryService configuration entry service.
     */
    @Autowired
    public PresenceScheduler( final MailService aMailService, final VoluntaryPresenceService aVoluntaryPresenceService,
                              final ConfigurationEntryService aConfigurationEntryService,
                              final VoluntaryPresenceStateService aVoluntaryPresenceStateService )
    {
        mailService = aMailService;
        voluntaryPresenceService = aVoluntaryPresenceService;
        configurationEntryService = aConfigurationEntryService;
        voluntaryPresenceStateService = aVoluntaryPresenceStateService;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( PresenceScheduler.class );

    /**
     * Handles {@linkplain VoluntaryPresence}s every day at 3AM. It checks whether reminding should be sent to Volunteers
     * and/or Offer's Contact People. It also checks whether maximum allowed time has passed since last reminding and if
     * it did, then it sets default states for presences.
     */
    @SneakyThrows
    @Scheduled( cron = "${presenceSchedulerInvocation.cron}" )
    public void handlePresences()
    {
        try
        {
            now = Instant.now();
            LOGGER.info( "Presence handler invoked." );

            final Duration timeToMakePresenceDecision = ConfigurationEntryReader.readValueAsDurationOrDefault(
                    ConfigurationEntryKeySet.VOLUNTARY_PRESENCE_CONFIRMATION_TIME_WINDOW_LENGTH, ChronoUnit.HOURS,
                    Duration.ofDays( 7 ), configurationEntryService );

            final Map< Offer, List< VoluntaryPresence > > voluntaryPresencesGroupedByOffer =
                    loadAllPresencesWhichWereNotResolved().stream()
                            .collect( Collectors.groupingBy( VoluntaryPresence::getOffer ) );

            for( final List< VoluntaryPresence > offerVoluntaryPresences : voluntaryPresencesGroupedByOffer.values() )
            {
                for( final VoluntaryPresence voluntaryPresence : offerVoluntaryPresences )
                {
                    if( voluntaryPresence.getVolunteerReportedPresenceStateAsEnum() == VoluntaryPresenceStateEnum.UNRESOLVED )
                    {
                        handlePresenceOnVolunteerSide( voluntaryPresence, timeToMakePresenceDecision );
                    }
                }

                if( offerVoluntaryPresences.stream().anyMatch( voluntaryPresence ->
                        voluntaryPresence.getInstitutionReportedPresenceStateAsEnum()
                                .equals( VoluntaryPresenceStateEnum.UNRESOLVED ) ) )
                {
                    handlePresencesOnInstitutionSide( offerVoluntaryPresences, timeToMakePresenceDecision );
                }
            }
        }
        catch ( Exception aE )
        {
            LOGGER.error( String.format( "Presence handler has encountered an error: %s", aE.getMessage() ) );
        }
    }

    private void handlePresenceOnVolunteerSide( final VoluntaryPresence aVoluntaryPresence,
                                                final Duration aTimeToMakePresenceDecision )
            throws IOException, MessagingException
    {
        final Instant volunteerRemindedDate = aVoluntaryPresence.getVolunteerReminderDate();
        if( aVoluntaryPresence.isWasVolunteerReminded() )
        {
            if( Duration.between( volunteerRemindedDate, now )
                    .compareTo( aTimeToMakePresenceDecision ) > 0 )
            {
                // if time defined in configuration has passed since date of last reminder of volunteer and volunteer
                // still has not made a decision on whether he was present or not, we mark his presence as denied.
                aVoluntaryPresence.setVolunteerReportedPresenceState(
                        ModelUtils.resolveVoluntaryPresenceState( VoluntaryPresenceStateEnum.DENIED,
                                voluntaryPresenceStateService ) );

                voluntaryPresenceService.saveOrUpdate( aVoluntaryPresence );
            }
        }
        else
        {
            if( now.isAfter( volunteerRemindedDate ) )
            {
                // send mail about presence confirmation to volunteer.
                aVoluntaryPresence.setWasVolunteerReminded( true );

                mailService.sendMailToVolunteerAboutPresenceConfirmation( aVoluntaryPresence, volunteerRemindedDate
                        .plus( aTimeToMakePresenceDecision ) );

                voluntaryPresenceService.saveOrUpdate( aVoluntaryPresence );
            }
        }
    }

    private void handlePresencesOnInstitutionSide( final List< VoluntaryPresence > aVoluntaryPresences,
                                                   final Duration aTimeToMakePresenceDecision )
            throws IOException, MessagingException
    {
        final VoluntaryPresence anyVoluntaryPresence = aVoluntaryPresences.stream()
                .findAny()
                .orElseThrow( IllegalStateException::new );

        final Instant institutionReminderDate = anyVoluntaryPresence.getInstitutionReminderDate();
        if( anyVoluntaryPresence.isWasInstitutionReminded() )
        {
            if( Duration.between( institutionReminderDate, now )
                    .compareTo( aTimeToMakePresenceDecision ) > 0 )
            {
                // if time defined in configuration has passed since date of last reminder of Institution and Offer
                // Contact Person still has not made a decision on whether Volunteer was present or not, we mark
                // Volunteer's presence as confirmed.
                aVoluntaryPresences.forEach( voluntaryPresence -> voluntaryPresence.setInstitutionReportedPresenceState(
                        ModelUtils.resolveVoluntaryPresenceState( VoluntaryPresenceStateEnum.CONFIRMED,
                                voluntaryPresenceStateService ) ) );

                voluntaryPresenceService.saveAll( aVoluntaryPresences );
            }
        }
        else
        {
            if( now.isAfter( institutionReminderDate ) )
            {
                // send mail about presence confirmation to Offer Contact Person.
                aVoluntaryPresences.forEach( voluntaryPresence -> voluntaryPresence.setWasInstitutionReminded( true ) );

                mailService.sendMailToOfferContactPersonAboutPresenceConfirmation( aVoluntaryPresences, institutionReminderDate
                        .plus( aTimeToMakePresenceDecision ) );

                voluntaryPresenceService.saveAll( aVoluntaryPresences );
            }
        }
    }

    private List< VoluntaryPresence > loadAllPresencesWhichWereNotResolved()
    {
        return voluntaryPresenceService.loadAllEntities().stream()
                .filter( Predicate.not( VoluntaryPresence::isPresenceResolved ) )
                .toList();
    }
}
