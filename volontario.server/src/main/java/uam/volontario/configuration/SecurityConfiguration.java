package uam.volontario.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration
{
    /**
     * Configures web behaviour across application.
     *
     * @param aHttp security builder.
     *
     * @return security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity aHttp ) throws Exception
    {
        // TODO: for now all requests are allowed.
        aHttp.csrf().disable().httpBasic().and().authorizeHttpRequests().anyRequest().permitAll();
        return aHttp.build();
    }
}
