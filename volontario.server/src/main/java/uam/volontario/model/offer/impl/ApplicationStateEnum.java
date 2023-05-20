package uam.volontario.model.offer.impl;

/**
 * Definition of application state in the system. All the {@linkplain ApplicationState} instances should be mapped to their
 * corresponding ApplicationStateEnums instances as they are easier to work with.
 */
public enum ApplicationStateEnum
{
    AWAITING,

    ACCEPTED,

    DECLINED;

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
                    default -> throw new IllegalArgumentException( aApplicationState.getName() + " is not a " +
                            "defined application state in the system." );
                };
    }

    /**
     * Maps ApplicationStateEnum to name of {@linkplain ApplicationState} entity.
     *
     * @param aApplicationStateEnum application state enum.
     *
     * @return name of application state entity.
     */
    public static String mapApplicationStateEnumToApplicationStateName( final ApplicationStateEnum aApplicationStateEnum )
    {
        return switch ( aApplicationStateEnum )
                {
                    case ACCEPTED -> "Zaakceptowana";
                    case AWAITING -> "Oczekująca";
                    case DECLINED -> "Odrzucona";
                };
    }
}
