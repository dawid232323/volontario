package uam.volontario.scheduler;

import com.google.common.collect.Lists;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.*;
import uam.volontario.model.configuration.ConfigurationEntry;
import uam.volontario.model.offer.impl.*;
import uam.volontario.security.mail.MailService;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

    @Scheduled( cron = "${offerSchedulerInvocation.cron}" )
    public void handleOffers() throws MessagingException, IOException
    {
        final Instant now = Instant.now();
        final Duration expirationBuffer = resolveExpirationBuffer();

        LOGGER.info( "Offer maintenance invoked at: " + now );

        final List< Offer > expiringOffers = Lists.newArrayList();
        final List< Offer > expiredOffers = Lists.newArrayList();

        for( final Offer offer : offerService.loadAllEntities() )
        {
            if( offer.getEndDate()
                    .isAfter( now ) )
            {
                expiredOffers.add( offer );
            }
            else if( Duration.between( now, offer.getEndDate() )
                    .compareTo( expirationBuffer ) < 0 )
            {
                expiringOffers.add( offer );
            }
        }

        mailService.sendEmailsAboutOffersExpiringSoon( expiringOffers );
        handleExpiredOffers( expiredOffers );
    }

    private Duration resolveExpirationBuffer()
    {
        final Optional< ConfigurationEntry > optionalExpirationOfferBufferConfig
                = configurationEntryService.findByKey( "VOL.OFFER.EXPIRATION_BUFFER" );

        if( optionalExpirationOfferBufferConfig.isPresent() )
        {
            try
            {
                final long bufferInHours = Long.parseLong( optionalExpirationOfferBufferConfig.get().
                        getValue() );

                return Duration.ofHours( bufferInHours );
            }
            catch ( Exception aE )
            {
                LOGGER.error( "Value of VOL.OFFER.EXPIRATION_BUFFER was not read properly." +
                        " Buffer will be set to 1 week." );
            }
        }

        LOGGER.warn( "Value of VOL.OFFER.EXPIRATION_BUFFER is undefined. Buffer will be set to 1 week." );
        return Duration.ofDays( 7 );
    }

    private void handleExpiredOffers( final List< Offer > aExpiredOffers )
    {
        final OfferState expiredState = getExpiredOfferState();
        final ApplicationState rejectedState = getRejectedApplicationState();

        final List< Application > applications = applicationService.findAllByOffers( aExpiredOffers );

        aExpiredOffers.forEach( offer -> offer.setOfferState( expiredState ) );
        applications.stream()
                .filter( app -> app.getStateAsEnum() == ApplicationStateEnum.AWAITING )
                .forEach( application -> application.setState( rejectedState ) );

        offerService.saveAll( aExpiredOffers );
        applicationService.saveAll( applications );
    }

    private OfferState getExpiredOfferState()
    {
        return offerStateService.tryLoadByState( OfferStateEnum.EXPIRED.getTranslatedState() )
                .orElseThrow();
    }

    private ApplicationState getRejectedApplicationState()
    {
        return applicationStateService.tryLoadByName( ApplicationStateEnum.DECLINED.getTranslatedState() )
                .orElseThrow();
    }
}
