package uam.volontario.scheduler;

import com.google.common.collect.*;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uam.volontario.EntityTestFactory;
import uam.volontario.configuration.ConfigurationEntryKeySet;
import uam.volontario.crud.service.ConfigurationEntryService;
import uam.volontario.crud.service.VoluntaryPresenceService;
import uam.volontario.crud.service.VoluntaryPresenceStateService;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.configuration.ConfigurationEntry;
import uam.volontario.model.offer.impl.*;
import uam.volontario.security.mail.MailService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

/**
 * Tests for {@linkplain PresenceScheduler} class.
 */
@RunWith( MockitoJUnitRunner.Silent.class )
public class PresenceSchedulerTest
{
    /**
     * Mocked services.
     */
    @Mock
    private MailService mailServiceMock;

    @Mock
    private VoluntaryPresenceService voluntaryPresenceServiceMock;

    @Mock
    private VoluntaryPresenceStateService voluntaryPresenceStateServiceMock;

    @Mock
    private ConfigurationEntryService configurationEntryServiceMock;

    /**
     * Collections to monitor state of presence scheduler.
     */
    private final Map< Long, VoluntaryPresence > persistedPresencesAfterVolunteerChanges = Maps.newHashMap();

    private final Map< Long, VoluntaryPresence > persistedPresencesAfterInstitutionChanges = Maps.newHashMap();

    private final Multimap< String, VoluntaryPresence > mailsSentAboutPresencesToContactEmail = ArrayListMultimap.create();

    private final List< VoluntaryPresence > presencesCache = Lists.newArrayList();

    /**
     * Temporary test case fields.
     */
    private Instant now;

    private PresenceScheduler presenceScheduler;

    @Before
    public void prepareTestCase()
    {
        persistedPresencesAfterVolunteerChanges.clear();
        persistedPresencesAfterInstitutionChanges.clear();
        mailsSentAboutPresencesToContactEmail.clear();
        presencesCache.clear();

        mockConfigurationEntryService();
        mockVoluntaryPresenceStateService();
        mockVoluntaryPresenceServicePersisting();
        mockMailService();
        mockVoluntaryPresenceServiceFetching();

        now = Instant.now();
        presenceScheduler = new PresenceScheduler( mailServiceMock, voluntaryPresenceServiceMock,
                configurationEntryServiceMock, voluntaryPresenceStateServiceMock );
    }

    @Test
    @SneakyThrows
    public void neitherVolunteerAndInstitutionShouldBeInformedAboutPresenceIfItsReminderDateIsBeforeNow()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "volunteer@email.test" );

        // Offer starts at day 7 and ends at day 14. Voluntary Presence's reminder date should be day 28, because
        // it should be offer end date + 2 weeks (or other buffer, up to configuration).
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME,
                offsetFromNowInDays( 7 ), offsetFromNowInDays( 14 ),
                "institution@email.test" );

        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createUnresolvedVoluntaryPresence(
                1, volunteer, offer, false, 3,
                offsetFromNowInDays( 28 ), false, 3,
                offsetFromNowInDays( 28 ) );

        putToCache( voluntaryPresence );

        // when
        presenceScheduler.handlePresences();

        // then
        assertTrue( persistedPresencesAfterInstitutionChanges.isEmpty() );
        assertTrue( persistedPresencesAfterVolunteerChanges.isEmpty() );
        assertTrue( mailsSentAboutPresencesToContactEmail.isEmpty() );
        assertEquals(1, voluntaryPresenceServiceMock.loadAllEntities().size() );
        assertEquals( voluntaryPresence, Iterables.getLast( voluntaryPresenceServiceMock.loadAllEntities() ) );

        assertFalse( voluntaryPresence.isWasInstitutionReminded() );
        assertFalse( voluntaryPresence.isWasVolunteerReminded() );

        assertEquals( VoluntaryPresenceStateEnum.UNRESOLVED,
                voluntaryPresence.getInstitutionReportedPresenceStateAsEnum() );
        assertEquals( VoluntaryPresenceStateEnum.UNRESOLVED,
                voluntaryPresence.getVolunteerReportedPresenceStateAsEnum() );
    }

    @Test
    @SneakyThrows
    public void bothVolunteerAndInstitutionShouldBeInformedAboutPresenceIfItsReminderDateIsAfterNow()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "volunteer@email.test" );

        // Offer starts at day -22 and ends at day -15. Voluntary Presence's reminder date should be day -1, because
        // it should be offer end date + 2 weeks (or other buffer, up to configuration).
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME,
                offsetFromNowInDays( -22 ), offsetFromNowInDays( -15 ),
                "institution@email.test" );

        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createUnresolvedVoluntaryPresence(
                1, volunteer, offer, false, 3,
                offsetFromNowInDays( -1 ), false, 3,
                offsetFromNowInDays( -1 ) );

        putToCache( voluntaryPresence );

        // when
        presenceScheduler.handlePresences();

        // then
        assertEquals( 1, persistedPresencesAfterInstitutionChanges.size() );
        assertEquals( 1, persistedPresencesAfterVolunteerChanges.size() );
        assertEquals( 2, mailsSentAboutPresencesToContactEmail.size() );

        assertEquals(1, voluntaryPresenceServiceMock.loadAllEntities().size() );
        assertEquals( voluntaryPresence, Iterables.getLast( voluntaryPresenceServiceMock.loadAllEntities() ) );

        assertEquals( voluntaryPresence, persistedPresencesAfterVolunteerChanges.get( voluntaryPresence.getId() ) );
        assertEquals( voluntaryPresence, persistedPresencesAfterInstitutionChanges.get( voluntaryPresence.getId() ) );

        assertEquals( Collections.singletonList( voluntaryPresence ),
                mailsSentAboutPresencesToContactEmail.get( volunteer.getContactEmailAddress() ) );
        assertEquals( Collections.singletonList( voluntaryPresence ),
                mailsSentAboutPresencesToContactEmail.get( offer.getContactPerson().getContactEmailAddress() ) );

        assertTrue( voluntaryPresence.isWasInstitutionReminded() );
        assertTrue( voluntaryPresence.isWasVolunteerReminded() );

        assertEquals( VoluntaryPresenceStateEnum.UNRESOLVED,
                voluntaryPresence.getInstitutionReportedPresenceStateAsEnum() );
        assertEquals( VoluntaryPresenceStateEnum.UNRESOLVED,
                voluntaryPresence.getVolunteerReportedPresenceStateAsEnum() );
    }

    @Test
    @SneakyThrows
    public void onlyVolunteerShouldBeInformedAboutPresenceIfInstitutionHasAlreadyPostponeConfirmationDecision()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "volunteer@email.test" );

        // Offer starts at day -22 and ends at day -15. Voluntary Presence's reminder date should be day -1, because
        // it should be offer end date + 2 weeks (or other buffer, up to configuration).
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME,
                offsetFromNowInDays( -22 ), offsetFromNowInDays( -15 ),
                "institution@email.test" );

        // we assume that Institution has postponed presence confirmation by week (default postpone duration).
        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createUnresolvedVoluntaryPresence(
                1, volunteer, offer, false, 2,
                offsetFromNowInDays( 6 ), false, 3,
                offsetFromNowInDays( -1 ) );

        putToCache( voluntaryPresence );

        // when
        presenceScheduler.handlePresences();

        // then
        assertTrue( persistedPresencesAfterInstitutionChanges.isEmpty() );
        assertEquals( 1, persistedPresencesAfterVolunteerChanges.size() );
        assertEquals( 1, mailsSentAboutPresencesToContactEmail.size() );

        assertEquals(1, voluntaryPresenceServiceMock.loadAllEntities().size() );
        assertEquals( voluntaryPresence, Iterables.getLast( voluntaryPresenceServiceMock.loadAllEntities() ) );

        assertEquals( voluntaryPresence, persistedPresencesAfterVolunteerChanges.get( voluntaryPresence.getId() ) );

        assertEquals( Collections.singletonList( voluntaryPresence ),
                mailsSentAboutPresencesToContactEmail.get( volunteer.getContactEmailAddress() ) );

        assertFalse( voluntaryPresence.isWasInstitutionReminded() );
        assertTrue( voluntaryPresence.isWasVolunteerReminded() );

        assertEquals( VoluntaryPresenceStateEnum.UNRESOLVED,
                voluntaryPresence.getInstitutionReportedPresenceStateAsEnum() );
        assertEquals( VoluntaryPresenceStateEnum.UNRESOLVED,
                voluntaryPresence.getVolunteerReportedPresenceStateAsEnum() );
    }

    @Test
    @SneakyThrows
    public void onlyInstitutionShouldBeInformedAboutPresenceIfVolunteerHasAlreadyPostponeConfirmationDecision()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "volunteer@email.test" );

        // Offer starts at day -22 and ends at day -15. Voluntary Presence's reminder date should be day -1, because
        // it should be offer end date + 2 weeks (or other buffer, up to configuration).
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME,
                offsetFromNowInDays( -22 ), offsetFromNowInDays( -15 ),
                "institution@email.test" );

        // we assume that Volunteer has postponed presence confirmation by week (default postpone duration).
        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createUnresolvedVoluntaryPresence(
                1, volunteer, offer, false, 3,
                offsetFromNowInDays( -1 ), false, 2,
                offsetFromNowInDays( 6 ) );

        putToCache( voluntaryPresence );

        // when
        presenceScheduler.handlePresences();

        // then
        assertTrue( persistedPresencesAfterVolunteerChanges.isEmpty() );
        assertEquals( 1, persistedPresencesAfterInstitutionChanges.size() );
        assertEquals( 1, mailsSentAboutPresencesToContactEmail.size() );

        assertEquals(1, voluntaryPresenceServiceMock.loadAllEntities().size() );
        assertEquals( voluntaryPresence, Iterables.getLast( voluntaryPresenceServiceMock.loadAllEntities() ) );

        assertEquals( voluntaryPresence, persistedPresencesAfterInstitutionChanges.get( voluntaryPresence.getId() ) );

        assertEquals( Collections.singletonList( voluntaryPresence ),
                mailsSentAboutPresencesToContactEmail.get( offer.getContactPerson().getContactEmailAddress() ) );

        assertTrue( voluntaryPresence.isWasInstitutionReminded() );
        assertFalse( voluntaryPresence.isWasVolunteerReminded() );

        assertEquals( VoluntaryPresenceStateEnum.UNRESOLVED,
                voluntaryPresence.getInstitutionReportedPresenceStateAsEnum() );
        assertEquals( VoluntaryPresenceStateEnum.UNRESOLVED,
                voluntaryPresence.getVolunteerReportedPresenceStateAsEnum() );
    }

    @Test
    @SneakyThrows
    public void bothVolunteerAndInstitutionShouldSetDefaultPresenceStatesWhenDecisionTimeAfterReminderPasses()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "volunteer@email.test" );

        // Offer starts at day -29 and ends at day -22. Voluntary Presence's reminder date should be day -8, because
        // it should be offer end date + 2 weeks (or other buffer, up to configuration).
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME,
                offsetFromNowInDays( -29 ), offsetFromNowInDays( -22 ),
                "institution@email.test" );

        // we consider both Institution and Volunteer to be reminded at -8 day. By default, they both have 1 week
        // to make a decision and 8 days have passes till now so scheduler should set denied for volunteer and confirmed
        // for institution.
        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createUnresolvedVoluntaryPresence(
                1, volunteer, offer, true, 3,
                offsetFromNowInDays( -8 ), true, 3,
                offsetFromNowInDays( -8 ) );

        putToCache( voluntaryPresence );

        // when
        presenceScheduler.handlePresences();

        // then
        assertEquals( 1, persistedPresencesAfterInstitutionChanges.size() );
        assertEquals( 1, persistedPresencesAfterVolunteerChanges.size() );
        assertTrue( mailsSentAboutPresencesToContactEmail.isEmpty() );
        assertEquals(1, voluntaryPresenceServiceMock.loadAllEntities().size() );
        assertEquals( voluntaryPresence, Iterables.getLast( voluntaryPresenceServiceMock.loadAllEntities() ) );

        assertTrue( voluntaryPresence.isWasInstitutionReminded() );
        assertTrue( voluntaryPresence.isWasVolunteerReminded() );

        assertEquals( VoluntaryPresenceStateEnum.CONFIRMED,
                voluntaryPresence.getInstitutionReportedPresenceStateAsEnum() );
        assertEquals( VoluntaryPresenceStateEnum.DENIED,
                voluntaryPresence.getVolunteerReportedPresenceStateAsEnum() );
    }

    private void mockConfigurationEntryService()
    {
        doReturn( Optional.of( ConfigurationEntry.builder()
                .value( "168" )
                .build() ) ).when( configurationEntryServiceMock )
                .findByKey( ConfigurationEntryKeySet.VOLUNTARY_PRESENCE_CONFIRMATION_TIME_WINDOW_LENGTH.getKeyName() );
    }
    
    private void mockVoluntaryPresenceStateService()
    {
        doReturn( Optional.of( VoluntaryPresenceState.builder()
                .state( VoluntaryPresenceStateEnum.DENIED.getTranslatedState() )
                .build() ) )
                .when( voluntaryPresenceStateServiceMock )
                .tryLoadByState( VoluntaryPresenceStateEnum.DENIED.getTranslatedState() );

        doReturn( Optional.of( VoluntaryPresenceState.builder()
                .state( VoluntaryPresenceStateEnum.CONFIRMED.getTranslatedState() )
                .build() ) )
                .when( voluntaryPresenceStateServiceMock )
                .tryLoadByState( VoluntaryPresenceStateEnum.CONFIRMED.getTranslatedState() );
    }

    private void mockVoluntaryPresenceServiceFetching()
    {
        doReturn( presencesCache ).when( voluntaryPresenceServiceMock )
                .loadAllEntities();
    }

    private void mockVoluntaryPresenceServicePersisting()
    {
        doAnswer( invocation -> {
            final VoluntaryPresence voluntaryPresence = invocation.getArgument( 0 );
            return persistedPresencesAfterVolunteerChanges.put( voluntaryPresence.getId(), voluntaryPresence );
        } )
           .when( voluntaryPresenceServiceMock )
           .saveOrUpdate( any( VoluntaryPresence.class ) );

        doAnswer( invocation -> {
            final List< VoluntaryPresence > voluntaryPresences = invocation.getArgument( 0 );
            persistedPresencesAfterInstitutionChanges.putAll( voluntaryPresences.stream()
                    .collect( Collectors.toMap( VoluntaryPresence::getId, Function.identity() ) ) );

            return voluntaryPresences;
        } )
                .when( voluntaryPresenceServiceMock )
                .saveAll( anyList() );
    }

    @SneakyThrows
    private void mockMailService()
    {
        doAnswer( invocation -> {
            final List< VoluntaryPresence > voluntaryPresences = invocation.getArgument( 0 );
            final VoluntaryPresence voluntaryPresence = Iterables.getLast( voluntaryPresences );
            return mailsSentAboutPresencesToContactEmail.putAll(
                    voluntaryPresence.getOffer().getContactPerson().getContactEmailAddress(), voluntaryPresences );
        } )
                .when( mailServiceMock )
                .sendMailToOfferContactPersonAboutPresenceConfirmation( anyList(), any( Instant.class ) );

        doAnswer( invocation -> {
            final VoluntaryPresence voluntaryPresence = invocation.getArgument( 0 );
            return mailsSentAboutPresencesToContactEmail.put(
                    voluntaryPresence.getVolunteer().getContactEmailAddress(), voluntaryPresence );
        } )
                .when( mailServiceMock )
                .sendMailToVolunteerAboutPresenceConfirmation( any( VoluntaryPresence.class ), any( Instant.class ) );
    }

    private void putToCache( final VoluntaryPresence aVoluntaryPresence )
    {
        presencesCache.add( aVoluntaryPresence );
    }

    private Instant offsetFromNowInDays( final int aNumberOfDays )
    {
        return now.plus( aNumberOfDays, ChronoUnit.DAYS );
    }
}