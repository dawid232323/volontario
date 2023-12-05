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

    EXPIRING( "Wygasające" ),

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
                    case "Wygasające" -> EXPIRING;
                    case "Wygasłe" -> EXPIRED;
                    case "Zamknięte" -> CLOSED;
                    default -> throw new IllegalArgumentException( aOfferState.getState() + " is not a defined offer state in the system." );
                };
    }


    /**
     * Maps OfferStateEnum to name of {@linkplain OfferState} entity.
     *
     * @param aOfferStateEnum offer state enum.
     *
     * @return name of offer state entity.
     */
    public static String mapOfferStateEnumToOfferStateName( final OfferStateEnum aOfferStateEnum )
    {
        return switch ( aOfferStateEnum )
        {
            case NEW -> "Nowe";
            case UNDER_VERIFICATION -> "W trakcie weryfikacji";
            case REJECTED -> "Odrzucone";
            case PUBLISHED -> "Opublikowane";
            case EXPIRING -> "Wygasające";
            case EXPIRED -> "Wygasłe";
            case CLOSED -> "Zamknięte";
        };
    }
}
