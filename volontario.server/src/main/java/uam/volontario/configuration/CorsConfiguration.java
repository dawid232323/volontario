package uam.volontario.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for CORS.
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer
{
    @Override
    public void addCorsMappings( CorsRegistry aRegistry )
    {
        // TODO: Do not forget to check it at some point.
        aRegistry.addMapping( "/**" )
                .allowedMethods( "POST", "GET", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD" )
                .allowedHeaders( "Authorization", "Cache-Control", "Content-Type" )
                .allowCredentials( true )
                .allowedOriginPatterns( "*" );
    }
}
