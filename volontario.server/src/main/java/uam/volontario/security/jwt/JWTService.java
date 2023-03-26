package uam.volontario.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.impl.User;

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

    //TODO: temporary key, for testing.
    private final static String KEY = "AmoULwYGnw9BaYbb0ZkkjFZuUJi0pfGNN8RuuKmWwVP5mvkcGmouqBqd" +
            "4IZqMPSVyAxe8gljmEyGVlmVXDYzeMlLyKs6gWYMYQMVgjH7Qoc58KJWAUtUyeOAMADMHZikMw5dIpJ6H" +
            "aNRqxIoO8tBbKRrkbqFMqWKYUaoLJbqw09Jj0B13Okj3t5fhwQ60ZSAGSZTsASdFIiz0UkOz9AiynJGI1abm6" +
            "a8nr42DNRi1AXwSLaGOKDVY3C7oSu1cjl2EiwWC96kuffRXMZrMHZyIEgcS6t2SArueFw7QtO41QjBsrnexETrB7" +
            "XICTO8KhGrcu0iPmXKo4UevPLXhB9hjTihopSbDWodAmI8QFad54O96dxyV1m0hJghKIbENwyGkXg2goZvxWwFw2J" +
            "EHO5BArEhxIeJWauR9t00A0Ua1AKxKxRHYSC83gBXVEBekkrB9r6UkjKbv9VOvHPmcITvr8qruZX2VlywGOOAZK30c" +
            "D8MvrvqHUdLKHupwyP3j2BnSHFhI5WG6KyMdd5zrrIXdPI5zbRg53yGEzz7jikKduJZ4nJtqgCzPZQoLnuNPAryNe9qtCbgeO0V" +
            "WW3lDSvd4jZ;";

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
                Map.entry( "refresh_token", createToken( aUser, 5 * 12 * 5 ) ) // 5 hours.
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
                    .setSigningKey( KEY )
                    .build()
                    .parseClaimsJws( aJWT )
                    .getBody();

            final Long userId = (long) (int)jwtClaims.get( "id" );
            return userService.tryLoadEntity( userId ).isPresent();
        }
        catch ( ExpiredJwtException aE )
        {
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
                    .setSigningKey( KEY )
                    .build()
                    .parseClaimsJws( aJWT )
                    .getBody();

            return (String)jwtClaims.get( "domainEmail" );
        }
        catch ( ExpiredJwtException aE )
        {
            // TODO: add logger
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
                .signWith( SignatureAlgorithm.HS512, KEY )
                .compact();
    }
}
