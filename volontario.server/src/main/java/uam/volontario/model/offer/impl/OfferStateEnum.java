package uam.volontario.model.offer.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 */
@AllArgsConstructor
@Getter
public enum OfferStateEnum
{
    NEW ( "Nowe" ),

    UNDER_VERIFICATION( "W trakcie weryfikacji" ),

    REJECTED( "Odrzucone" ),

    PUBLISHED( "Opublikowane" ),

    EXPIRED( "Wygasłe" ),

    CLOSED( "Zamknięte" );

    private final String translatedState;

    /**
     * Maps entity of {@linkplain OfferState} type to more flexible OfferTypeEnum instance.
     *
     * @param aOfferState offer state entity.
     *
     * @return corresponding offer state enum.
     */
    public static OfferStateEnum mapOfferStateToOfferStateEnum( final OfferState aOfferState )
    {
        return switch ( aOfferState.getState() )
                {
                    case "Nowe" -> NEW;
                    case "W trakcie weryfikacji" -> UNDER_VERIFICATION;
                    case "Odrzucone" -> REJECTED;
                    case "Opublikowane" -> PUBLISHED;
                    case "Zamknięte" -> CLOSED;
                    case "Wygasłe" -> EXPIRED;
                    default -> throw new IllegalArgumentException( aOfferState.getState() + " is not a defined offer state in the system." );
                };
    }
}
