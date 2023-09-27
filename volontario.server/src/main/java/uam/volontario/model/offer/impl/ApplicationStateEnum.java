package uam.volontario.model.offer.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Definition of application state in the system. All the {@linkplain ApplicationState} instances should be mapped to their
 * corresponding ApplicationStateEnums instances as they are easier to work with.
 */
@AllArgsConstructor
@Getter
public enum ApplicationStateEnum
{
    AWAITING( "Oczekująca" ),

    ACCEPTED( "Zaakceptowana" ),

    DECLINED( "Odrzucona" ),

    RESERVE_LIST( "Lista rezerwowa" );

    private final String translatedState;

    /**
     * Maps entity of {@linkplain uam.volontario.model.offer.impl.ApplicationState} type to more flexible ApplicationTypeEnum instance.
     *
     * @param aApplicationState application state entity.
     *
     * @return corresponding application state enum.
     */
    public static ApplicationStateEnum mapApplicationStateToApplicationStateEnum( final ApplicationState aApplicationState )
    {
        return switch ( aApplicationState.getName() )
                {
                    case "Oczekująca" -> AWAITING;
                    case "Zaakceptowana" -> ACCEPTED;
                    case "Odrzucona" -> DECLINED;
                    case "Lista rezerwowa" -> RESERVE_LIST;
                    default -> throw new IllegalArgumentException( aApplicationState.getName() + " is not a " +
                            "defined application state in the system." );
                };
    }
}
