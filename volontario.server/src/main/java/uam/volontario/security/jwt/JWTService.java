package uam.volontario.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.security.SignatureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Service for Json Web Token functionalities.
 */
@Component
public class JWTService
{
    private final UserService userService;

    /**
     * CDI constructor.
     *
     * @param aUserService user service.
     */
    @Autowired
    public JWTService( final UserService aUserService )
    {
        userService = aUserService;
    }

    private static final Logger LOGGER = LogManager.getLogger( JWTService.class );

    private static final SecretKey SECRET_KEY = MacProvider.generateKey( SignatureAlgorithm.HS512 );

    /**
     * Creates two JWTs - the main one lasting 5 minutes and refresh one lasting 5 hours. Both of have user's id
     * and role. And they are signed with HS512 algorithm.
     *
     * @param aUser user.
     *
     * @return map containing JWT and refresh JWT.
     */
    public Map< String, String > createMainTokenAndRefreshToken( final User aUser )
    {
        return Map.ofEntries(
                Map.entry( "token", createToken( aUser, 5 ) ), // 5 minutes
                Map.entry( "refresh_token", createToken( aUser, 60 * 5 ) ) // 5 hours.
        );
    }

    /**
     * Validates token by checking whether it has existing user and still valid expiration date.
     *
     * @param aJWT jwt.
     *
     * @return false, if jwt is expired or when it has no existing user. True otherwise.
     */
    public boolean validateToken( final String aJWT )
    {
        return readUserFromJWT( aJWT )
                .isPresent();
    }

    /**
     * Reads contact email address from jwt.
     *
     * @param aJWT jwt.
     *
     * @return jwt's contact email address or null if jwt is expired.
     */
    public Optional< String > readContactEmailAddressFromJWT( final String aJWT )
    {
        final Optional< Claims > optionalParsedToken = parseToken( aJWT );

        if( optionalParsedToken.isPresent() )
        {
            final Claims parsedToken = optionalParsedToken.get();
            final Object contactEmail = parsedToken.get( "contactEmail" );

            if( contactEmail != null )
            {
                return Optional.of( (String)contactEmail );
            }
        }

        return Optional.empty();
    }

    /**
     * Reads user from jwt.
     *
     * @param aJWT jwt.
     *
     * @return jwt's user or null if jwt is expired.
     */
    public Optional< User > readUserFromJWT( final String aJWT )
    {
        final Optional< Claims > optionalParsedToken = parseToken( aJWT );

        if( optionalParsedToken.isPresent() )
        {
            final Claims parsedToken = optionalParsedToken.get();
            return userService.tryLoadEntity( parsedToken.get( "id", Long.class ) );
        }

        return Optional.empty();
    }

    /**
     * Reads token from 'Authorization' header.
     *
     * @param aAuthorizationHeader 'Authorization' header.
     *
     * @return jwt.
     */
    public String getTokenFromAuthorizationHeader( final String aAuthorizationHeader )
    {
        return aAuthorizationHeader.substring( 7 );
    }

    /**
     * Retrieves current user email based on token principal
     *
     * @return email String value
     */
    public String getCurrentUserEmail()
    {
        final Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        return String.valueOf( authentication.getPrincipal() );
    }

    private String createToken( final User aUser, final int aExpirationOffsetFromNowInMinutes )
    {
        final Instant now = Instant.now();

        return Jwts.builder()
                .setClaims( Map.of( "id", aUser.getId(),
                        "roles", aUser.getRoles().stream()
                                        .map( Role::getName )
                                        .toList(),
                        "contactEmail", aUser.getContactEmailAddress() ) )
                .setIssuedAt( Date.from( now ) )
                .setExpiration( Date.from( now.plus( Duration.ofMinutes( aExpirationOffsetFromNowInMinutes ) ) ) )
                .signWith( SECRET_KEY )
                .compact();
    }

    private Optional< Claims > parseToken( final String aJWT )
    {
        try
        {
            return Optional.of( Jwts.parserBuilder()
                    .setSigningKey( SECRET_KEY )
                    .build()
                    .parseClaimsJws( aJWT )
                    .getBody() );
        }
        catch ( ExpiredJwtException aE )
        {
            LOGGER.debug( "Parsing JWT failed: JWT token has expired" );
            return Optional.empty();
        }
        catch ( SignatureException aE )
        {
            LOGGER.debug( "Parsing JWT failed, signature error: {}", aE.getMessage() );
            return Optional.empty();
        }
        catch ( MalformedJwtException | UnsupportedJwtException aE )
        {
            LOGGER.debug( "Parsing JWT failed, reason: {}", aE.getMessage() );
            return Optional.empty();
        }
        catch ( IllegalArgumentException aE )
        {
            LOGGER.debug( "Parsing JWT failed: {}", aE.getMessage() );
            return Optional.empty();
        }
    }
}
