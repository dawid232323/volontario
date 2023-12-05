package uam.volontario.scheduler;

import com.google.common.collect.Lists;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uam.volontario.configuration.ConfigurationEntryKeySet;
import uam.volontario.configuration.ConfigurationEntryReader;
import uam.volontario.crud.service.*;
import uam.volontario.model.offer.impl.*;
import uam.volontario.model.utils.ModelUtils;
import uam.volontario.security.mail.MailService;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Scheduler for handling outdated {@linkplain uam.volontario.model.offer.impl.Offer}s.
 */
@Service
public class OfferScheduler
{
    private final OfferService offerService;

    private final OfferStateService offerStateService;

    private final ApplicationStateService applicationStateService;

    private final MailService mailService;

    private final ConfigurationEntryService configurationEntryService;

    private final ApplicationService applicationService;

    /**
     * CDI constructor.
     *
     * @param aOfferService offer service.
     *
     * @param aMailService mail service.
     *
     * @param aConfigurationEntryService configuration entry service.
     *
     * @param aOfferStateService offer state service.
     *
     * @param aApplicationStateService application state service.
     *
     * @param aApplicationService application service.
     */
    @Autowired
    public OfferScheduler( final OfferService aOfferService, final MailService aMailService,
                           final ConfigurationEntryService aConfigurationEntryService,
                           final OfferStateService aOfferStateService,
                           final ApplicationStateService aApplicationStateService,
                           final ApplicationService aApplicationService )
    {
        offerService = aOfferService;
        mailService = aMailService;
        configurationEntryService = aConfigurationEntryService;
        offerStateService = aOfferStateService;
        applicationStateService = aApplicationStateService;
        applicationService = aApplicationService;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( OfferScheduler.class );

    /**
     * Method invoked every day at 2AM to check whether Offers have expired or are expiring. Detailed mails are
     * sent to Institutions and Applications related to expired Offers are set as declined if they were "awaiting" before.
     *
     * @throws MessagingException on error when sending mail.
     *
     * @throws IOException on error when sending mail.
     */
    @Scheduled( cron = "${offerSchedulerInvocation.cron}" )
    public void handleOffers() throws MessagingException, IOException
    {
        final Instant now = Instant.now();
        final Duration expirationBuffer = ConfigurationEntryReader.readValueAsDurationOrDefault(
                ConfigurationEntryKeySet.OFFER_EXPIRATION_BUFFER, ChronoUnit.HOURS,
                Duration.ofDays( 7 ), configurationEntryService );

        LOGGER.info( "Offer maintenance invoked at: " + now );

        final List< Offer > expiringOffers = Lists.newArrayList();
        final List< Offer > expiredOffers = Lists.newArrayList();

        final List< Offer > notExpiredOffers = offerService.loadAllEntities().stream()
                .filter( offer -> !offer.getOfferStateAsEnum()
                        .equals( OfferStateEnum.EXPIRED ) )
                .toList();

        for( final Offer offer : notExpiredOffers )
        {
            if( offer.getExpirationDate()
                    .isBefore( now ) )
            {
                expiredOffers.add( offer );
            }
            else if( Duration.between( now, offer.getExpirationDate() )
                    .compareTo( expirationBuffer ) < 0 && !offer.getOfferStateAsEnum().equals( OfferStateEnum.EXPIRING ) )
            {
                offer.setOfferState( ModelUtils.resolveOfferState( OfferStateEnum.EXPIRING, offerStateService ) );
                expiringOffers.add( offer );
            }
        }

        mailService.sendEmailsAboutOffersExpiringSoon( expiringOffers );
        offerService.saveAll( expiringOffers );

        handleExpiredOffers( expiredOffers );
    }

    private void handleExpiredOffers( final List< Offer > aExpiredOffers )
    {
        final OfferState expiredState = ModelUtils.resolveOfferState( OfferStateEnum.EXPIRED, offerStateService );
        final ApplicationState rejectedState = ModelUtils.resolveApplicationState( ApplicationStateEnum.DECLINED,
                applicationStateService );

        final List< Application > applications = applicationService.findAllByOffers( aExpiredOffers );

        aExpiredOffers.forEach( offer -> { offer.setOfferState( expiredState ); offer.setIsHidden( true ); } );
        applications.stream()
                .filter( app -> app.getStateAsEnum() == ApplicationStateEnum.AWAITING )
                .forEach( application -> application.setState( rejectedState ) );

        offerService.saveAll( aExpiredOffers );
        applicationService.saveAll( applications );
    }
}
