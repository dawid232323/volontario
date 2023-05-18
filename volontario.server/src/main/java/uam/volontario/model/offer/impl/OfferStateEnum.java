package uam.volontario.model.offer.impl;

/**
 *
 */
public enum OfferStateEnum
{
    NEW,

    UNDER_VERIFICATION,

    REJECTED,

    PUBLISHED,

    CLOSED;

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
                    case CLOSED -> "Zamknięte";
                };
    }
}
