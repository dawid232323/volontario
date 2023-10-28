package uam.volontario.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import uam.volontario.EntityTestFactory;
import uam.volontario.configuration.ConfigurationEntryKeySet;
import uam.volontario.crud.service.ConfigurationEntryService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.UserService;
import uam.volontario.dto.presence.VoluntaryPresenceInstitutionDataDto;
import uam.volontario.dto.presence.VoluntaryPresenceVolunteerDataDto;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.configuration.ConfigurationEntry;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.OfferTypeEnum;
import uam.volontario.model.offer.impl.VoluntaryPresence;
import uam.volontario.model.offer.impl.VoluntaryPresenceStateEnum;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;

/**
 * Test class for {@linkplain OfferPresenceHandler}.
 */
@RunWith( MockitoJUnitRunner.Silent.class )
public class OfferPresenceHandlerTest
{
    @Mock
    private OfferService offerService;

    @Mock
    private ConfigurationEntryService configurationEntryService;

    @Mock
    private UserService userService;

    @InjectMocks
    private OfferPresenceHandler offerPresenceHandler;

    private final Instant now = Instant.now();

    @Test
    public void offerShouldBeReadyToConfirmPresencesIfItsOneTimeOfferAndDefinedBufferHasAlreadyPassedSinceOfferEndTime()
    {
        // given
        final Instant offerStartDate = offsetFromNowInDays( -20 );
        final Instant offerEndDate = offsetFromNowInDays( -10 );

        // offer starts at day -20 and ends at day -10.
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME,
                offerStartDate, offerEndDate, "anyEmail@wp.pl" );

        // we set buffer for one time offer for 168 hours (7 days).
        final ConfigurationEntry configurationEntry = EntityTestFactory.createConfigurationEntry(
                1L, ConfigurationEntryKeySet.APPLICATION_ONE_TIME_CONFIRMATION_BUFFER.getKeyName(), "168" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingConfigurationKeyFromConfigurationKeyService( configurationEntry );

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.isOfferReadyToConfirmPresences( offer.getId() );

        // then
        // Offer of type ONE_TIME is ready to confirm presences since time defined in the
        // VOL.APPLICATION.ONE_TIME_CONFIRMATION_BUFFER Configuration Key passed since Offer's end date.
        // In this case, Offer's End date is day -10 so Offer is ready since day -3.
        assertValueFromIsOfferReadyToConfirmPresences( response, true );
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
    }

    @Test
    public void offerShouldNOTBeReadyToConfirmPresencesIfItsOneTimeOfferAndDefinedBufferHasNOTYePassedSinceOfferEndTime()
    {
        // given
        final Instant offerStartDate = offsetFromNowInDays( -20 );
        final Instant offerEndDate = offsetFromNowInDays( -5 );

        // offer starts at day -20 and ends at day -5.
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME,
                offerStartDate, offerEndDate, "anyEmail@wp.pl" );

        // we set buffer for one time offer for 168 hours (7 days).
        final ConfigurationEntry configurationEntry = EntityTestFactory.createConfigurationEntry(
                1L, ConfigurationEntryKeySet.APPLICATION_ONE_TIME_CONFIRMATION_BUFFER.getKeyName(), "168" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingConfigurationKeyFromConfigurationKeyService( configurationEntry );

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.isOfferReadyToConfirmPresences( offer.getId() );

        // then
        // Offer of type ONE_TIME is ready to confirm presences since time defined in the
        // VOL.APPLICATION.ONE_TIME_CONFIRMATION_BUFFER Configuration Key passed since Offer's end date.
        // In this case, Offer's End date is day -5 so Offer will be ready on day +2.
        assertValueFromIsOfferReadyToConfirmPresences( response, false );
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
    }

    @Test
    public void offerShouldBeReadyToConfirmPresencesIfItsRegularOfferAndDefinedBufferHasAlreadyPassedSinceOfferStartTime()
    {
        // given
        final Instant offerStartDate = offsetFromNowInDays( -9 );
        final Instant offerEndDate = offsetFromNowInDays( 100 );

        // offer starts at day -9 and ends at day 100.
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.REGULAR,
                offerStartDate, offerEndDate, "anyEmail@wp.pl" );

        // we set buffer for multi time offer for 168 hours (7 days).
        final ConfigurationEntry configurationEntry = EntityTestFactory.createConfigurationEntry(
                1L, ConfigurationEntryKeySet.APPLICATION_MULTI_TIME_CONFIRMATION_BUFFER.getKeyName(), "168" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingConfigurationKeyFromConfigurationKeyService( configurationEntry );

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.isOfferReadyToConfirmPresences( offer.getId() );

        // then
        // Offer of type REGULAR is ready to confirm presences since time defined in the
        // VOL.APPLICATION.MULTI_TIME_CONFIRMATION_BUFFER Configuration Key passed since Offer's start date.
        // In this case, Offer's Start date is day -9 so Offer is ready since day -2.
        assertValueFromIsOfferReadyToConfirmPresences( response, true );
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
    }

    @Test
    public void offerShouldNOTBeReadyToConfirmPresencesIfItsRegularOfferAndDefinedBufferHasNOTYePassedSinceOfferStartTime()
    {
        // given
        final Instant offerStartDate = offsetFromNowInDays( -5 );
        final Instant offerEndDate = offsetFromNowInDays( 100 );

        // offer starts at day -5 and ends at day 100.
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.REGULAR,
                offerStartDate, offerEndDate, "anyEmail@wp.pl" );

        // we set buffer for one time offer for 168 hours (7 days).
        final ConfigurationEntry configurationEntry = EntityTestFactory.createConfigurationEntry(
                1L, ConfigurationEntryKeySet.APPLICATION_MULTI_TIME_CONFIRMATION_BUFFER.getKeyName(), "168" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingConfigurationKeyFromConfigurationKeyService( configurationEntry );

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.isOfferReadyToConfirmPresences( offer.getId() );

        // then
        // Offer of type REGULAR is ready to confirm presences since time defined in the
        // VOL.APPLICATION.MULTI_TIME_CONFIRMATION_BUFFER Configuration Key passed since Offer's stat date.
        // In this case, Offer's Start date is day -5 so Offer will be ready on day +2.
        assertValueFromIsOfferReadyToConfirmPresences( response, false );
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
    }

    @Test
    public void offerShouldBeReadyToConfirmPresencesIfItsCyclicOfferAndDefinedBufferHasAlreadyPassedSinceOfferStartTime()
    {
        // given
        final Instant offerStartDate = offsetFromNowInDays( -9 );
        final Instant offerEndDate = offsetFromNowInDays( 100 );

        // offer starts at day -9 and ends at day 100.
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.CYCLE,
                offerStartDate, offerEndDate, "anyEmail@wp.pl" );

        // we set buffer for multi time offer for 168 hours (7 days).
        final ConfigurationEntry configurationEntry = EntityTestFactory.createConfigurationEntry(
                1L, ConfigurationEntryKeySet.APPLICATION_MULTI_TIME_CONFIRMATION_BUFFER.getKeyName(), "168" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingConfigurationKeyFromConfigurationKeyService( configurationEntry );

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.isOfferReadyToConfirmPresences( offer.getId() );

        // then
        // Offer of type CYCLE is ready to confirm presences since time defined in the
        // VOL.APPLICATION.MULTI_TIME_CONFIRMATION_BUFFER Configuration Key passed since Offer's start date.
        // In this case, Offer's Start date is day -9 so Offer is ready since day -2.
        assertValueFromIsOfferReadyToConfirmPresences( response, true );
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
    }

    @Test
    public void offerShouldNOTBeReadyToConfirmPresencesIfItsCyclicOfferAndDefinedBufferHasNOTYePassedSinceOfferStartTime()
    {
        // given
        final Instant offerStartDate = offsetFromNowInDays( -5 );
        final Instant offerEndDate = offsetFromNowInDays( 100 );

        // offer starts at day -5 and ends at day 100.
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.CYCLE,
                offerStartDate, offerEndDate, "anyEmail@wp.pl" );

        // we set buffer for one time offer for 168 hours (7 days).
        final ConfigurationEntry configurationEntry = EntityTestFactory.createConfigurationEntry(
                1L, ConfigurationEntryKeySet.APPLICATION_MULTI_TIME_CONFIRMATION_BUFFER.getKeyName(), "168" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingConfigurationKeyFromConfigurationKeyService( configurationEntry );

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.isOfferReadyToConfirmPresences( offer.getId() );

        // then
        // Offer of type CYCLE is ready to confirm presences since time defined in the
        // VOL.APPLICATION.MULTI_TIME_CONFIRMATION_BUFFER Configuration Key passed since Offer's stat date.
        // In this case, Offer's Start date is day -5 so Offer will be ready on day +2.
        assertValueFromIsOfferReadyToConfirmPresences( response, false );
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
    }

    @Test
    public void loadingVoluntaryPresenceStateOfVolunteerWhoDidNotHaveHisApplicationAcceptedForGivenOfferShouldResultIn400()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "vol@wp.pl" );
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME, offsetFromNowInDays( -14 ),
                offsetFromNowInDays( -7 ), "anyEmail@wp.pl" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingUserFromUserService( volunteer );

        // we assume that volunteer does not have any accepted applications.
        doReturn( Collections.emptyList() ).when( volunteer ).getVoluntaryPresences();

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.loadVoluntaryPresenceStateOfVolunteer( volunteer.getId(),
                offer.getId() );

        // then
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 400 ) );
    }

    @Test
    public void loadingVoluntaryPresenceStateOfVolunteerWhoHadHisApplicationAcceptedForGivenOfferShouldReturnDtoWithCorrectDataWhenDecisionWasNOTMade()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "vol@wp.pl" );
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME, offsetFromNowInDays( -14 ),
                offsetFromNowInDays( -7 ), "anyEmail@wp.pl" );

        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createUnresolvedVoluntaryPresence(
                1L, volunteer, offer,
                false, 3, null,
                false, 3, null );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingUserFromUserService( volunteer );

        // we assume that volunteer does have accepted application for given offer.
        doReturn( Collections.singletonList( voluntaryPresence ) ).when( volunteer ).getVoluntaryPresences();


        // when
        final ResponseEntity< ? > response = offerPresenceHandler.loadVoluntaryPresenceStateOfVolunteer( volunteer.getId(),
                offer.getId() );

        // then

        // since voluntary presence is unresolved, there is no buffer for decision change since there never was a decision
        // and therefore naturally, no deadline for that change.no
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
        assertValueFromLoadVoluntaryPresenceStateOfVolunteer( response, VoluntaryPresenceStateEnum.UNRESOLVED,
                false, null );
    }

    @Test
    public void loadingVoluntaryPresenceStateOfVolunteerWhoHadHisApplicationAcceptedForGivenOfferShouldReturnDtoWithCorrectDataWhenDecisionWasMade()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "vol@wp.pl" );
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME, offsetFromNowInDays( -14 ),
                offsetFromNowInDays( -7 ), "anyEmail@wp.pl" );

        // presence was confirmed by volunteer at day -1.
        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createVoluntaryPresence(
                1, volunteer, offer, false, 0, now, VoluntaryPresenceStateEnum.UNRESOLVED,
                null, false, 3, now, VoluntaryPresenceStateEnum.CONFIRMED,
                offsetFromNowInDays( -1 ) );

        // we set decision change buffer for one time offer for 168 hours (7 days).
        final ConfigurationEntry configurationEntry = EntityTestFactory.createConfigurationEntry(
                1L, ConfigurationEntryKeySet.VOL_VOLUNTARY_PRESENCE_DECISION_CHANGE_BUFFER.getKeyName(), "168" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingUserFromUserService( volunteer );
        mockLoadingConfigurationKeyFromConfigurationKeyService( configurationEntry );

        // we assume that volunteer does have accepted application for given offer.
        doReturn( Collections.singletonList( voluntaryPresence ) ).when( volunteer ).getVoluntaryPresences();

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.loadVoluntaryPresenceStateOfVolunteer( volunteer.getId(),
                offer.getId() );

        // then

        // voluntary presence was confirmed at day -1, buffer to make decision is 7 days (since making decision) so
        // deadline decision is decision date + buffer.
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
        assertValueFromLoadVoluntaryPresenceStateOfVolunteer( response, VoluntaryPresenceStateEnum.CONFIRMED,
                true, voluntaryPresence.getVolunteerDecisionDate().plus( 168, ChronoUnit.HOURS ) );
    }

    @Test
    public void loadingVoluntaryPresenceStateOfVolunteerWhoHadHisApplicationAcceptedForGivenOfferShouldReturnDtoWithCorrectDataWhenDecisionWasMade2()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "vol@wp.pl" );
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME, offsetFromNowInDays( -14 ),
                offsetFromNowInDays( -7 ), "anyEmail@wp.pl" );

        // presence was confirmed by volunteer at day -1.
        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createVoluntaryPresence(
                1, volunteer, offer, false, 0, now, VoluntaryPresenceStateEnum.UNRESOLVED,
                null, false, 3, now, VoluntaryPresenceStateEnum.CONFIRMED,
                offsetFromNowInDays( -1 ) );

        // we set decision change buffer for one time offer for 1 hour.
        final ConfigurationEntry configurationEntry = EntityTestFactory.createConfigurationEntry(
                1L, ConfigurationEntryKeySet.VOL_VOLUNTARY_PRESENCE_DECISION_CHANGE_BUFFER.getKeyName(), "1" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingUserFromUserService( volunteer );
        mockLoadingConfigurationKeyFromConfigurationKeyService( configurationEntry );

        // we assume that volunteer does have accepted application for given offer.
        doReturn( Collections.singletonList( voluntaryPresence ) ).when( volunteer ).getVoluntaryPresences();

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.loadVoluntaryPresenceStateOfVolunteer( volunteer.getId(),
                offer.getId() );

        // then

        // voluntary presence was confirmed at day -1, buffer to make decision is 1 hour (since making decision) so
        // deadline decision is decision date + buffer.
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
        assertValueFromLoadVoluntaryPresenceStateOfVolunteer( response, VoluntaryPresenceStateEnum.CONFIRMED,
                false, null );
    }

    @Test
    public void loadingVoluntaryPresenceStateOfInstitutionWhichHasHadVolunteerApplicationAcceptedForGivenOfferShouldReturnDtoWithCorrectDataWhenDecisionWasNOTMade()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "vol@wp.pl" );
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME, offsetFromNowInDays( -14 ),
                offsetFromNowInDays( -7 ), "anyEmail@wp.pl" );

        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createUnresolvedVoluntaryPresence(
                1L, volunteer, offer,
                false, 3, null,
                false, 3, null );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingUserFromUserService( volunteer );

        // we assume that institution does have accepted application for given offer.
        doReturn( Collections.singletonList( voluntaryPresence ) ).when( offer ).getVoluntaryPresences();


        // when
        final ResponseEntity< ? > response = offerPresenceHandler.loadVoluntaryPresenceStateOfInstitution( offer.getId() );

        // then

        // since voluntary presence is unresolved, there is no buffer for decision change since there never was a decision
        // and therefore naturally, no deadline for that change.no
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
        assertValueFromLoadVoluntaryPresenceStateOfInstitution( response, false,
                false, null );
    }

    @Test
    public void loadingVoluntaryPresenceStateOfInstitutionWhichHasHadVolunteerApplicationAcceptedForGivenOfferShouldReturnDtoWithCorrectDataWhenDecisionWasMade()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "vol@wp.pl" );
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME, offsetFromNowInDays( -14 ),
                offsetFromNowInDays( -7 ), "anyEmail@wp.pl" );

        // presence was confirmed by both institution at day -1.
        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createVoluntaryPresence(
                1, volunteer, offer, false, 3, now, VoluntaryPresenceStateEnum.CONFIRMED,
                offsetFromNowInDays( -1 ), false, 0, now, VoluntaryPresenceStateEnum.UNRESOLVED,
                null );

        // we set decision change buffer for one time offer for 168 hours (7 days).
        final ConfigurationEntry configurationEntry = EntityTestFactory.createConfigurationEntry(
                1L, ConfigurationEntryKeySet.VOL_VOLUNTARY_PRESENCE_DECISION_CHANGE_BUFFER.getKeyName(), "168" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingUserFromUserService( volunteer );
        mockLoadingConfigurationKeyFromConfigurationKeyService( configurationEntry );

        // we assume that institution does have accepted application for given offer.
        doReturn( Collections.singletonList( voluntaryPresence ) ).when( offer ).getVoluntaryPresences();

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.loadVoluntaryPresenceStateOfInstitution( offer.getId() );

        // then

        // voluntary presence was confirmed at day -1, buffer to make decision is 7 days (since making decision) so
        // deadline decision is decision date + buffer.
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
        assertValueFromLoadVoluntaryPresenceStateOfInstitution( response, true,
                true, voluntaryPresence.getInstitutionDecisionDate().plus( 168, ChronoUnit.HOURS ) );
    }

    @Test
    public void loadingVoluntaryPresenceStateOfInstitutionWhichHasHadVolunteerApplicationAcceptedForGivenOfferShouldReturnDtoWithCorrectDataWhenDecisionWasMade2()
    {
        // given
        final User volunteer = EntityTestFactory.createVolunteer( 1, "vol@wp.pl" );
        final Offer offer = EntityTestFactory.createOffer( 1, OfferTypeEnum.ONE_TIME, offsetFromNowInDays( -14 ),
                offsetFromNowInDays( -7 ), "anyEmail@wp.pl" );

        // presence was confirmed by both institution at day -1.
        final VoluntaryPresence voluntaryPresence = EntityTestFactory.createVoluntaryPresence(
                1, volunteer, offer, false, 3, now, VoluntaryPresenceStateEnum.CONFIRMED,
                offsetFromNowInDays( -1 ), false, 0, now, VoluntaryPresenceStateEnum.UNRESOLVED,
                null );

        // we set decision change buffer for one time offer for 1 hour.
        final ConfigurationEntry configurationEntry = EntityTestFactory.createConfigurationEntry(
                1L, ConfigurationEntryKeySet.VOL_VOLUNTARY_PRESENCE_DECISION_CHANGE_BUFFER.getKeyName(), "1" );

        mockLoadingOfferFromOfferService( offer );
        mockLoadingUserFromUserService( volunteer );
        mockLoadingConfigurationKeyFromConfigurationKeyService( configurationEntry );

        // we assume that institution does have accepted application for given offer.
        doReturn( Collections.singletonList( voluntaryPresence ) ).when( offer ).getVoluntaryPresences();

        // when
        final ResponseEntity< ? > response = offerPresenceHandler.loadVoluntaryPresenceStateOfInstitution( offer.getId() );

        // then

        // voluntary presence was confirmed at day -1, buffer to make decision is 1 hour (since making decision) so
        // deadline decision is decision date + buffer.
        assertEquals( response.getStatusCode(), HttpStatusCode.valueOf( 200 ) );
        assertValueFromLoadVoluntaryPresenceStateOfInstitution( response, true,
                false, null );
    }

    private void mockLoadingOfferFromOfferService( final Offer aOffer )
    {
        // to avoid UnfinishedStubbingException.
        final Long offerId = aOffer.getId();

        doReturn( Optional.of( aOffer ) ).when( offerService ).tryLoadEntity( offerId );
    }

    private void mockLoadingUserFromUserService( final User aUser )
    {
        // to avoid UnfinishedStubbingException.
        final Long userId = aUser.getId();

        doReturn( Optional.of( aUser ) ).when( userService ).tryToFindById( userId );
    }

    private void mockLoadingConfigurationKeyFromConfigurationKeyService( final ConfigurationEntry aConfigurationEntry )
    {
        // to avoid UnfinishedStubbingException.
        final String configurationEntryKeyName = aConfigurationEntry.getKey();

        doReturn( Optional.of( aConfigurationEntry ) ).when( configurationEntryService ).findByKey( configurationEntryKeyName );
    }

    private Instant offsetFromNowInDays( final int aDaysOffset )
    {
        return now.plus( aDaysOffset, ChronoUnit.DAYS );
    }

    private void assertValueFromIsOfferReadyToConfirmPresences( final ResponseEntity< ? > aResponse, final boolean aExpectedValue )
    {
        if( !(aResponse.getBody() instanceof Map jsonMap &&
                jsonMap.get( "isOfferReadyToConfirmPresences" ) instanceof Boolean value && value == aExpectedValue ) )
        {
            fail( String.format( "Calculated value: %b. Expected value: %b", !aExpectedValue, aExpectedValue ) );
        }
    }

    private void assertValueFromLoadVoluntaryPresenceStateOfVolunteer( final ResponseEntity< ? > aResponse,
                                                                       final VoluntaryPresenceStateEnum aExpectedPresenceState,
                                                                       final Boolean aExpectedCanDecisionBeChanged,
                                                                       final Instant aExpectedDeadlineDecisionDate )
    {
        if( aResponse.getBody() instanceof VoluntaryPresenceVolunteerDataDto dto )
        {
            if( aExpectedPresenceState != dto.getConfirmationState()  )
            {
                fail( String.format( "Expected value: %s. Calculated value: %s.", aExpectedPresenceState.getTranslatedState(),
                        dto.getConfirmationState() == null ?
                                "null" : dto.getConfirmationState().getTranslatedState() ) );
            }

            if( aExpectedCanDecisionBeChanged != dto.isCanDecisionBeChanged() )
            {
                fail( String.format( "Expected value: %b. Calculated value: %b.", aExpectedCanDecisionBeChanged,
                        dto.getConfirmationState() ) );
            }

            if( !((aExpectedDeadlineDecisionDate != null && aExpectedDeadlineDecisionDate.equals( dto.getDecisionChangeDeadlineDate() ))
                    || (dto.getDecisionChangeDeadlineDate() != null && dto.getDecisionChangeDeadlineDate().equals( aExpectedDeadlineDecisionDate ))
                    || (aExpectedDeadlineDecisionDate == null & dto.getDecisionChangeDeadlineDate() == null)) )
            {
                fail( String.format( "Expected value: %s. Calculated value: %s.", aExpectedDeadlineDecisionDate,
                        dto.getConfirmationState() == null ?
                                "null" : dto.getDecisionChangeDeadlineDate() ) );
            }

            return;
        }

        fail( String.format( "Body is not type of %s", VoluntaryPresenceVolunteerDataDto.class.getSimpleName() ) );
    }

    private void assertValueFromLoadVoluntaryPresenceStateOfInstitution( final ResponseEntity< ? > aResponse,
                                                                        final Boolean aExpectedWasPresenceConfirmed,
                                                                        final Boolean aExpectedCanDecisionBeChanged,
                                                                        final Instant aExpectedDeadlineDecisionDate )
    {
        if( aResponse.getBody() instanceof VoluntaryPresenceInstitutionDataDto dto )
        {
            if( aExpectedWasPresenceConfirmed != dto.isWasPresenceConfirmed()  )
            {
                fail( String.format( "Expected value: %b. Calculated value: %b.", aExpectedWasPresenceConfirmed,
                        dto.isWasPresenceConfirmed() ) );
            }

            if( aExpectedCanDecisionBeChanged != dto.isCanDecisionBeChanged() )
            {
                fail( String.format( "Expected value: %b. Calculated value: %b.", aExpectedCanDecisionBeChanged,
                        dto.isCanDecisionBeChanged()) );
            }

            if( !((aExpectedDeadlineDecisionDate != null && aExpectedDeadlineDecisionDate.equals( dto.getDecisionChangeDeadlineDate() ))
                    || (dto.getDecisionChangeDeadlineDate() != null && dto.getDecisionChangeDeadlineDate().equals( aExpectedDeadlineDecisionDate ))
                    || (aExpectedDeadlineDecisionDate == null & dto.getDecisionChangeDeadlineDate() == null)) )
            {
                fail( String.format( "Expected value: %s. Calculated value: %s.", aExpectedDeadlineDecisionDate,
                        dto.getDecisionChangeDeadlineDate() == null ?
                                "null" : dto.getDecisionChangeDeadlineDate() ) );
            }

            return;
        }

        fail( String.format( "Body is not type of %s", VoluntaryPresenceInstitutionDataDto.class.getSimpleName() ) );
    }
}