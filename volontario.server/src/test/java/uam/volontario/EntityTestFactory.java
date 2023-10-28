package uam.volontario;

import lombok.experimental.UtilityClass;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.configuration.ConfigurationEntry;
import uam.volontario.model.offer.impl.*;

import java.time.Instant;
import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * Utility class for easier creation of {@linkplain uam.volontario.model.common.VolontarioDomainElementIf} instances.
 */
@UtilityClass
public class EntityTestFactory
{
    /**
     * Creates {@linkplain User} instance of role {@linkplain UserRole#VOLUNTEER} with id and contact email address set.
     *
     * @param aVolunteerId id to be set.
     *
     * @param aContactEmail mail to be set.
     *
     * @return volunteer.
     */
    public User createVolunteer( final long aVolunteerId, final String aContactEmail )
    {
        final User volunteerSpy = spy( User.builder()
                .id( aVolunteerId )
                .contactEmailAddress( aContactEmail )
                .build() );

        doReturn( Collections.singletonList( UserRole.VOLUNTEER ) ).when( volunteerSpy )
                .getUserRoles();

        return volunteerSpy;
    }

    /**
     * Creates {@linkplain Offer} of given {@linkplain OfferTypeEnum} with id, boundary dates
     * and contact person's contact email.
     *
     * @param aOfferId id to be set.
     *
     * @param aOfferTypeEnum offer type to be set.
     *
     * @param aOfferStartDate start date to be set.
     *
     * @param aOfferEndDate end date to be set.
     *
     * @param aContactPersonEmail contact person contact email to be set.
     *
     * @return offer.
     */
    public Offer createOffer( final long aOfferId, final OfferTypeEnum aOfferTypeEnum,
                              final Instant aOfferStartDate, final Instant aOfferEndDate,
                              final String aContactPersonEmail )
    {
        final Offer offerSpy = spy( Offer.builder()
                .id( aOfferId )
                .startDate( aOfferStartDate )
                .endDate( aOfferEndDate )
                .contactPerson( User.builder()
                        .contactEmailAddress( aContactPersonEmail )
                        .build() )
                .build() );

        doReturn( aOfferTypeEnum ).when( offerSpy )
                .getOfferTypeAsEnum();

        return offerSpy;
    }

    /**
     * Creates {@linkplain VoluntaryPresence} of state {@linkplain VoluntaryPresenceStateEnum#UNRESOLVED} for
     * both Institution and Volunteer. Presence has id, volunteer, offer and institution/volunteer state fields set.
     *
     * @param aPresenceId id to be set.
     *
     * @param aVolunteer volunteer to be set.
     *
     * @param aOffer offer to be set.
     *
     * @param aWasInstitutionReminded institution reminded flag to be set.
     *
     * @param aInstitutionReminderCount institution reminder count to be set.
     *
     * @param aInstitutionReminderDate institution reminder date to be set.
     *
     * @param aWasVolunteerReminded institution reminded flag to be set.
     *
     * @param aVolunteerReminderCount institution reminder count to be set.
     *
     * @param aVolunteerReminderDate institution reminded flag to be set.
     *
     * @return voluntary presence.
     */
    public VoluntaryPresence createUnresolvedVoluntaryPresence( final long aPresenceId, final User aVolunteer,
                                                                final Offer aOffer, final boolean aWasInstitutionReminded,
                                                                final int aInstitutionReminderCount, final Instant aInstitutionReminderDate,
                                                                final boolean aWasVolunteerReminded, final int aVolunteerReminderCount,
                                                                final Instant aVolunteerReminderDate )
    {
        return createVoluntaryPresence( aPresenceId, aVolunteer, aOffer, aWasInstitutionReminded,
                aInstitutionReminderCount, aInstitutionReminderDate, VoluntaryPresenceStateEnum.UNRESOLVED, null,
                aWasVolunteerReminded, aVolunteerReminderCount, aVolunteerReminderDate,
                VoluntaryPresenceStateEnum.UNRESOLVED, null );
    }

    /**
     * Creates {@linkplain VoluntaryPresence} for both Institution and Volunteer. Presence has id,
     * volunteer, offer and institution/volunteer state fields set.
     *
     * @param aPresenceId id to be set.
     *
     * @param aVolunteer volunteer to be set.
     *
     * @param aOffer offer to be set.
     *
     * @param aWasInstitutionReminded institution reminded flag to be set.
     *
     * @param aInstitutionReminderCount institution reminder count to be set.
     *
     * @param aInstitutionReminderDate institution reminder date to be set.
     *
     * @param aInstitutionReportedPresenceState institution reported state to be set.
     *
     * @param aInstitutionDecisionDate institution decision date to be set.
     *
     * @param aWasVolunteerReminded institution reminded flag to be set.
     *
     * @param aVolunteerReminderCount institution reminder count to be set.
     *
     * @param aVolunteerReminderDate institution reminded flag to be set.
     *
     * @param aVolunteerReportedPresenceState volunteer reported state to be set.
     *
     * @param aVolunteerDecisionDate volunteer decision date to be set.
     *
     * @return voluntary presence.
     */
    public VoluntaryPresence createVoluntaryPresence( final long aPresenceId, final User aVolunteer,
                                                      final Offer aOffer, final boolean aWasInstitutionReminded,
                                                      final int aInstitutionReminderCount, final Instant aInstitutionReminderDate,
                                                      final VoluntaryPresenceStateEnum aInstitutionReportedPresenceState,
                                                      final Instant aInstitutionDecisionDate,
                                                      final boolean aWasVolunteerReminded, final int aVolunteerReminderCount,
                                                      final Instant aVolunteerReminderDate,
                                                      final VoluntaryPresenceStateEnum aVolunteerReportedPresenceState,
                                                      final Instant aVolunteerDecisionDate )
    {
        final VoluntaryPresence voluntaryPresenceSpy = spy( VoluntaryPresence.builder()
                .id( aPresenceId )
                .volunteer( aVolunteer )
                .offer( aOffer )
                .wasInstitutionReminded( aWasInstitutionReminded )
                .institutionLeftReminderCount( aInstitutionReminderCount )
                .institutionReminderDate( aInstitutionReminderDate )
                .institutionDecisionDate( aInstitutionDecisionDate )
                .wasVolunteerReminded( aWasVolunteerReminded )
                .volunteerLeftReminderCount( aVolunteerReminderCount )
                .volunteerReminderDate( aVolunteerReminderDate )
                .volunteerDecisionDate( aVolunteerDecisionDate )
                .build() );

        doReturn( aVolunteerReportedPresenceState ).when( voluntaryPresenceSpy )
                .getVolunteerReportedPresenceStateAsEnum();
        doReturn( aInstitutionReportedPresenceState ).when( voluntaryPresenceSpy )
                .getInstitutionReportedPresenceStateAsEnum();

        mockSettingInstitutionReportedPresenceState( voluntaryPresenceSpy );
        mockSettingVolunteerReportedPresenceState( voluntaryPresenceSpy );

        return voluntaryPresenceSpy;
    }

    /**
     * Creates instance of {@linkplain ConfigurationEntry} with given id, key name and value.
     *
     * @param aId id to be set.
     *
     * @param aKeyName key name to be set.
     *
     * @param aValueAsString value (in string format) to be set.
     *
     * @return Configuration Entry.
     */
    public ConfigurationEntry createConfigurationEntry( final Long aId, final String aKeyName, final String aValueAsString )
    {
        return ConfigurationEntry.builder()
                .id( aId )
                .value( aValueAsString )
                .key( aKeyName )
                .build();
    }

    private static void mockSettingInstitutionReportedPresenceState( final VoluntaryPresence aVoluntaryPresenceSpy )
    {
        doAnswer( invocation -> {
            final VoluntaryPresenceState voluntaryPresenceState = invocation.getArgument( 0 );

            if( voluntaryPresenceState.getState()
                    .equalsIgnoreCase( VoluntaryPresenceStateEnum.DENIED.getTranslatedState() ) )
            {
                doReturn( VoluntaryPresenceStateEnum.DENIED ).when( aVoluntaryPresenceSpy )
                        .getInstitutionReportedPresenceStateAsEnum();
            }

            if( voluntaryPresenceState.getState()
                    .equalsIgnoreCase( VoluntaryPresenceStateEnum.CONFIRMED.getTranslatedState() ) )
            {
                doReturn( VoluntaryPresenceStateEnum.CONFIRMED ).when( aVoluntaryPresenceSpy )
                        .getInstitutionReportedPresenceStateAsEnum();
            }

            return null;

        } )
                .when( aVoluntaryPresenceSpy )
                .setInstitutionReportedPresenceState( any( VoluntaryPresenceState.class ) );
    }

    private static void mockSettingVolunteerReportedPresenceState( final VoluntaryPresence aVoluntaryPresenceSpy )
    {
        doAnswer( invocation -> {
            final VoluntaryPresenceState voluntaryPresenceState = invocation.getArgument( 0 );

            if( voluntaryPresenceState.getState()
                    .equalsIgnoreCase( VoluntaryPresenceStateEnum.DENIED.getTranslatedState() ) )
            {
                doReturn( VoluntaryPresenceStateEnum.DENIED ).when( aVoluntaryPresenceSpy )
                        .getVolunteerReportedPresenceStateAsEnum();
            }

            if( voluntaryPresenceState.getState()
                    .equalsIgnoreCase( VoluntaryPresenceStateEnum.CONFIRMED.getTranslatedState() ) )
            {
                doReturn( VoluntaryPresenceStateEnum.CONFIRMED ).when( aVoluntaryPresenceSpy )
                        .getVolunteerReportedPresenceStateAsEnum();
            }

            return null;

        } )
                .when( aVoluntaryPresenceSpy )
                .setVolunteerReportedPresenceState( any( VoluntaryPresenceState.class ) );
    }
}
