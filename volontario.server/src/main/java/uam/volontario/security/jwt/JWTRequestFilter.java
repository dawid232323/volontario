package uam.volontario.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uam.volontario.crud.service.UserService;

import java.io.IOException;
import java.util.Optional;

/**
 * Filter for JWT requests.
 */
@Component
public class JWTRequestFilter extends OncePerRequestFilter
{
    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal( final HttpServletRequest aRequest, final HttpServletResponse aResponse,
                                     final FilterChain aFilterChain ) throws ServletException, IOException
    {
        final String authenticationHeader = aRequest.getHeader( HttpHeaders.AUTHORIZATION );
        if( authenticationHeader == null || !authenticationHeader.startsWith( "Bearer" ) )
        {
            aFilterChain.doFilter( aRequest, aResponse );
            return;
        }

        final String jwt = jwtService.getTokenFromAuthorizationHeader( authenticationHeader );
        final Optional< String > domainEmail = jwtService.readDomainEmailAddressFromJWT( jwt );

        if( domainEmail.isPresent() && jwtService.validateToken( jwt )
                && SecurityContextHolder.getContext().getAuthentication() == null )
        {
            final UserDetails userDetails = userService.loadUserByUsername( domainEmail.get() );

            final UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken( userDetails.getUsername(), userDetails.getPassword(),
                            userDetails.getAuthorities() );

            authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( aRequest ) );
            SecurityContextHolder.getContext().setAuthentication( authenticationToken );
        }

        aFilterChain.doFilter( aRequest, aResponse );
    }
}
