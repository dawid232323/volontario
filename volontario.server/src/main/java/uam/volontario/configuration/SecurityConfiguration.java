package uam.volontario.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uam.volontario.crud.service.UserService;
import uam.volontario.security.jwt.JWTAuthenticationEntryPoint;
import uam.volontario.security.jwt.JWTRequestFilter;

/**
 * Configuration class for Spring Security.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration
{
    @Autowired
    private UserService userService;

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

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
        aHttp.csrf().disable()
                .httpBasic()
                .and()
                .authorizeHttpRequests().requestMatchers( "/api/register" ).permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers( "/api/login" ).permitAll()
                .and()
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint( jwtAuthenticationEntryPoint )
                .and()
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS );

        aHttp.addFilterBefore( jwtRequestFilter, UsernamePasswordAuthenticationFilter.class );
        return aHttp.build();
    }

    /**
     * Configures authentication across application.
     *
     * @param aAuth authentication manager.
     *
     * @throws Exception on failure during authentication.
     */
    @Autowired
    public void configureGlobal( AuthenticationManagerBuilder aAuth ) throws Exception
    {
        aAuth.userDetailsService( userService ).passwordEncoder( new BCryptPasswordEncoder() );
    }
}
