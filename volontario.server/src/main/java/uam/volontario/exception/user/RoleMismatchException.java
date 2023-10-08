package uam.volontario.exception.user;

/**
 * Exception thrown when certain operation cannot be performed on user with certain role.
 */
public class RoleMismatchException extends RuntimeException
{
    public RoleMismatchException( final String aMessage )
    {
        super( aMessage );
    }

    public RoleMismatchException()
    {
        super( "User does not have suitable role" );
    }

}
