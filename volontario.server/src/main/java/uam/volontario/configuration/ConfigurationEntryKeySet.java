package uam.volontario.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Set of initial {@linkplain uam.volontario.model.configuration.ConfigurationEntry} key names.
 */
@AllArgsConstructor
@Getter
public enum ConfigurationEntryKeySet
{
    OFFER_EXPIRATION_BUFFER( "VOL.OFFER.EXPIRATION_BUFFER" ),

    APPLICATION_ONE_TIME_CONFIRMATION_BUFFER( "VOL.APPLICATION.ONE_TIME_CONFIRMATION_BUFFER" ),

    APPLICATION_MULTI_TIME_CONFIRMATION_BUFFER( "VOL.APPLICATION.MULTI_TIME_CONFIRMATION_BUFFER" ),

    VOLUNTARY_PRESENCE_POSTPONE_CONFIRMATION_TIME( "VOL.VOLUNTARY_PRESENCE.POSTPONE_CONFIRMATION_TIME" ),

    VOLUNTARY_PRESENCE_MAX_REMINDER_COUNT( "VOL.VOLUNTARY_PRESENCE.MAX_REMINDER_COUNT" ),

    VOLUNTARY_PRESENCE_CONFIRMATION_TIME_WINDOW_LENGTH( "VOL.VOLUNTARY_PRESENCE.CONFIRMATION_TIME_WINDOW_LENGTH" ),

    VOL_VOLUNTARY_PRESENCE_DECISION_CHANGE_BUFFER( "VOL.VOLUNTARY_PRESENCE.DECISION_CHANGE_BUFFER" );

    private final String keyName;
}
