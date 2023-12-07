package uam.volontario.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uam.volontario.crud.service.UserService;
import uam.volontario.security.jwt.JWTAuthenticationEntryPoint;
import uam.volontario.security.jwt.JWTRequestFilter;

import java.util.List;

/**
 * Configuration class for Spring Security.
 */
@EnableWebSecurity
@EnableScheduling
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration
{
    private final UserService userService;

    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JWTRequestFilter jwtRequestFilter;

    /**
     * CDI constructor.
     *
     * @param aUserService user service.
     *
     * @param aJWTAuthEntryPoint jwt authentication entry point.
     *
     * @param aJWTRequestFilter jwt request filter.
     */
    @Autowired
    public SecurityConfiguration( final UserService aUserService, final JWTAuthenticationEntryPoint aJWTAuthEntryPoint,
                                  final JWTRequestFilter aJWTRequestFilter )
    {
        userService = aUserService;
        jwtAuthenticationEntryPoint = aJWTAuthEntryPoint;
        jwtRequestFilter = aJWTRequestFilter;
    }

    /**
     * Configures web behaviour across application.
     *
     * @param aHttp security builder.
     *
     * @return security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain( final HttpSecurity aHttp ) throws Exception
    {
        aHttp.csrf().disable().httpBasic();

        // TODO: do not forget to remove some of those URLs when their authentication is resolved.
        excludeUrlsFromJWTAuthentication( aHttp, List.of(
                "/api/volunteer/register",
                "/api/login",
                "/api/interestCategories",
                "/api/experienceLevels",
                "/api/refresh/token",
                "/api/institution/register",
                "/api/institution/accept",
                "/api/institution/reject",
                "/api/institution/register-contact-person",
                "/api/institution/{institution_id:[0-9]+}/register-employee/set-password",
                "/api/institution/register-contact-person",
                "api/report",
                "/api/report",
                "/api/report/with-attachments",
                "/api/configuration/landingPage",
                "/api/volunteer/{volunteer_id:[0-9]+}/confirm-registration") );

        aHttp.cors();
        aHttp.csrf().disable();
        aHttp.authorizeHttpRequests().anyRequest().authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint( jwtAuthenticationEntryPoint )
            .and()
            .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS );

        aHttp.addFilterBefore( jwtRequestFilter, UsernamePasswordAuthenticationFilter.class );
        return aHttp.build();
    }

    /**
     * Defines password encoder for Volontario system.
     *
     * @return bcrypt password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures authentication across application.
     *
     * @param aAuth authentication manager.
     *
     * @throws Exception on failure during authentication.
     */
    @Autowired
    public void configureGlobal( final AuthenticationManagerBuilder aAuth ) throws Exception
    {
        aAuth.userDetailsService( userService )
                .passwordEncoder( new BCryptPasswordEncoder() );
    }

    private HttpSecurity excludeUrlsFromJWTAuthentication( final HttpSecurity aHttpSecurity, final List< String > aUrls )
            throws Exception
    {
        for( final String url : aUrls )
        {
            excludeUrlFromJwtAuthentication( aHttpSecurity, url );
        }

        return aHttpSecurity;
    }

    private void excludeUrlFromJwtAuthentication( final HttpSecurity aHttpSecurity, final String aUrl )
            throws Exception
    {
        aHttpSecurity.authorizeHttpRequests().requestMatchers( aUrl ).permitAll()
                .and();
    }
}
