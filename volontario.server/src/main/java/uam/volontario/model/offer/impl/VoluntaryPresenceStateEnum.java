package uam.volontario.model.offer.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representation of {@linkplain VoluntaryPresenceState}.
 */
@AllArgsConstructor
@Getter
public enum VoluntaryPresenceStateEnum
{
    UNRESOLVED( "Nierozstrzygnięta" ),

    CONFIRMED( "Potwierdzona" ),

    DENIED( "Zaprzeczona" );

    private final String translatedState;

    /**
     * Maps entity of {@linkplain VoluntaryPresenceState} type to more flexible VolunteerPresenceStateEnum instance.
     *
     * @param aPresenceState presence state entity.
     *
     * @return corresponding presence state entity enum.
     */
    public static VoluntaryPresenceStateEnum mapOfferTypeToOfferTypeEnum(final VoluntaryPresenceState aPresenceState )
    {
        return switch ( aPresenceState.getState() )
                {
                    case "Nierozstrzygnięta" -> UNRESOLVED;
                    case "Potwierdzona" -> CONFIRMED;
                    case "Zaprzeczona" -> DENIED;
                    default -> throw new IllegalArgumentException( aPresenceState.getState() + " is not a defined Presence State in the system." );
                };
    }
}
