package uam.volontario.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.security.SignatureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.impl.User;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Service for Json Web Token functionalities.
 */
@Component
public class JWTService
{
    @Autowired
    private UserService userService;

    private static final SecretKey SECRET_KEY = MacProvider.generateKey( SignatureAlgorithm.HS512 );

    private static final Logger LOGGER = LogManager.getLogger( JWTService.class );

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
        try
        {
            final Claims jwtClaims = Jwts.parserBuilder()
                    .setSigningKey( SECRET_KEY )
                    .build()
                    .parseClaimsJws( aJWT )
                    .getBody();

            final Long userId = (long)(int)jwtClaims.get( "id" );
            return userService.tryLoadEntity( userId ).isPresent();
        } catch ( ExpiredJwtException aE )
        {
            LOGGER.warn( "JWT validation failed: JWT token has expired" );
            return false;
        } catch ( SignatureException aE )
        {
            LOGGER.warn( "JWT signature validation failed: {}", aE.getMessage(), aE );
            return false;
        } catch ( MalformedJwtException | UnsupportedJwtException aE )
        {
            LOGGER.warn( "JWT validation failed, reason: {}", aE.getMessage(), aE );
            return false;
        } catch ( IllegalArgumentException aE )
        {
            LOGGER.warn( "An empty or null String has been passed to JWT validation: {}", aE.getMessage(), aE );
            return false;
        }
    }

    /**
     * Reads domain email address from jwt.
     *
     * @param aJWT jwt.
     *
     * @return jwt's domain email address or null if jwt is expired.
     */
    public String readDomainEmailAddressFromJWT( final String aJWT )
    {
        try
        {
            final Claims jwtClaims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws( aJWT )
                    .getBody();

            return (String)jwtClaims.get( "domainEmail" );
        } catch ( ExpiredJwtException aE )
        {
            LOGGER.warn( "Retrieving email from JWT failed: JWT token has expired" );
            return null;
        } catch ( SignatureException aE )
        {
            LOGGER.warn( "Retrieving email from JWT failed, signature error: {}", aE.getMessage(), aE );
            return null;
        } catch ( MalformedJwtException | UnsupportedJwtException aE )
        {
            LOGGER.warn( "Retrieving email from JWT failed, reason: {}", aE.getMessage(), aE );
            return null;
        } catch ( IllegalArgumentException aE )
        {
            LOGGER.warn( "An empty or null String has been passed to JWT email retrieval: {}", aE.getMessage(), aE );
            return null;
        }
    }

    private String createToken( final User aUser, final int aExpirationOffsetFromNowInMinutes )
    {
        final Instant now = Instant.now();

        return Jwts.builder()
                .setClaims( Map.of( "id", aUser.getId(),
                        "role", aUser.getRole().getName(),
                        "domainEmail", aUser.getDomainEmailAddress() ) )
                .setIssuedAt( Date.from( now ) )
                .setExpiration( Date.from( now.plus( Duration.ofMinutes( aExpirationOffsetFromNowInMinutes ) ) ) )
                .signWith(SECRET_KEY)
                .compact();
    }
}
