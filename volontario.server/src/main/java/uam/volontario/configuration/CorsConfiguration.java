package uam.volontario.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for CORS.
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer
{
    private final String protocol;
    private final String volontarioHost;

    public CorsConfiguration( final @Value( "${schema}" ) String aProtocol,
                              final @Value( "${volontarioHost}" ) String aVolontarioHost )
    {
        this.protocol = aProtocol;
        this.volontarioHost = aVolontarioHost;
    }

    @Override
    public void addCorsMappings( CorsRegistry aRegistry )
    {
        String originPattern = protocol + "://" + volontarioHost + ":[*]";
        aRegistry.addMapping( "/**" )
                .allowedMethods( "POST", "GET", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD" )
                .allowedHeaders( "Authorization", "Cache-Control", "Content-Type" )
                .allowCredentials( true )
                .allowedOriginPatterns( originPattern );
    }
}
