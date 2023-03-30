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
    public void addCorsMappings( CorsRegistry aRegistry ) {
        aRegistry.addMapping( "/**" ).allowedMethods( "*" );
    }
}
