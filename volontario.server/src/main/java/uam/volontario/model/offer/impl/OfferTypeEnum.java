package uam.volontario.model.offer.impl;

/**
 * Definition of offer type in the system. All the {@linkplain OfferType} instances should be mapped to their
 * corresponding OfferTypeEnums instances as they are easier to work with.
 */
public enum OfferTypeEnum
{
    ONE_TIME,

    REGULAR,

    CYCLE;

    /**
     * Maps entity of {@linkplain OfferType} type to more flexible OfferTypeEnum instance.
     *
     * @param aOfferType offer type entity.
     *
     * @return corresponding offer type enum.
     */
    public static OfferTypeEnum mapOfferTypeToOfferTypeEnum( final OfferType aOfferType )
    {
        return switch ( aOfferType.getName() )
                {
                    case "Jednorazowy" -> ONE_TIME;
                    case "Cykliczny" -> CYCLE;
                    case "Ciągły" -> REGULAR;
                    default -> throw new IllegalArgumentException( aOfferType.getName() + " is not a defined offer type in the system." );
                };
    }

    /**
     * Maps OfferTypeEnum to name of {@linkplain OfferType} entity.
     *
     * @param aOfferTypeEnum offer type enum.
     *
     * @return name of offer type entity.
     */
    public static String mapOfferTypeEnumToOfferTypeName( final OfferTypeEnum aOfferTypeEnum )
    {
        return switch ( aOfferTypeEnum )
                {
                    case ONE_TIME -> "Jednorazowy";
                    case CYCLE -> "Cykliczny";
                    case REGULAR -> "Ciągły";
                };
    }
}
