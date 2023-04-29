package uam.volontario.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**
 * Entry point for Json Web Token authentication. Its role is to catch requests which did not pass JWT authentication
 * and they end with 401 status.
 */
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable
{
    @Serial
    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence( final HttpServletRequest aRequest, final HttpServletResponse aResponse,
                          final AuthenticationException aAuthException ) throws IOException
    {
        aResponse.sendError( HttpStatus.UNAUTHORIZED.value(), aAuthException.getMessage() );
    }

}
