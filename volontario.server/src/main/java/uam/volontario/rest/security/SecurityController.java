package uam.volontario.rest.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uam.volontario.crud.service.UserService;
import uam.volontario.dto.LoginDto;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.common.impl.User;
import uam.volontario.security.jwt.JWTService;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.UserValidationService;

import java.util.Map;
import java.util.Optional;

/**
 * Controller for login, registration and other strict-security related functionalities.
 */
@RestController
@RequestMapping( value = "/api",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class SecurityController
{
    private final DtoService dtoService;

    private final UserValidationService userValidationService;

    private final UserService userService;

    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    /**
     * CDI constructor.
     *
     * @param aDtoService dto service.
     *
     * @param aUserValidationService user validation service.
     *
     * @param aUserService user service.
     *
     * @param aJwtService jwt service.
     *
     * @param aPasswordEncoder password encoder.
     */
    @Autowired
    public SecurityController( final DtoService aDtoService, final UserValidationService aUserValidationService,
                               final UserService aUserService, final JWTService aJwtService,
                               final PasswordEncoder aPasswordEncoder )
    {
        dtoService = aDtoService;
        userValidationService = aUserValidationService;
        userService = aUserService;
        jwtService = aJwtService;
        passwordEncoder = aPasswordEncoder;
    }

    private static final Logger LOGGER = LogManager.getLogger( SecurityController.class );

    /**
     * Registers volunteer.
     *
     * @param aDto dto containing registration data.
     *
     * @return if volunteer passes validation, then ResponseEntity with 201 status and volunteer. If volunteer did not
     *         pass validation then ResponseEntity with 401 status and constraints violated. If there was an error,
     *         then ResponseEntity with 501 status and error message.
     */
    @PostMapping( value = "/register" )
    public ResponseEntity< ? > registerVolunteer( @RequestBody final VolunteerDto aDto )
    {
        try
        {
            final User user = dtoService.createVolunteerFromDto( aDto );
            final ValidationResult validationResult = userValidationService.validateVolunteerUser( user );

            if( validationResult.isValidated() )
            {
                LOGGER.info( "A new user has been registered for email {}, userId: {}",
                        user.getDomainEmailAddress(), user.getId() );
                userService.saveOrUpdate( user );
                return ResponseEntity.status( HttpStatus.CREATED )
                        .body( validationResult.getValidatedEntity() );
            }
            LOGGER.debug( "Validation failed for new user with email {}, violations: {}", aDto.getDomainEmail(),
                    validationResult.getValidationViolations().values() );
            return ResponseEntity.badRequest()
                    .body( validationResult.getValidationViolations() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Exception occured during registration: {}", aE.getMessage(), aE );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Performs login operation on user.
     *
     * @param aDto dto containing logging data.
     *
     * @return if user gave correct logging data, then ResponseEntity with 200 status and JWTs. If user did not
     *         give correct logging data then ResponseEntity with 401 status and reason. If there was an error,
     *         then ResponseEntity with 501 status and error message.
     */
    @PostMapping( value = "/login" )
    public ResponseEntity< ? > login( @RequestBody final LoginDto aDto )
    {
        try
        {
            final Optional< User > user = userService.tryToLoadByDomainEmail( aDto.getDomainEmailAddress() );

            if( user.isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( "No user with domain email " + aDto.getDomainEmailAddress()
                                + " is registered in the system." );
            }

            if( !passwordEncoder.matches( aDto.getPassword(), user.get().getHashedPassword() ) )
            {
                return ResponseEntity.badRequest()
                        .body( "Wrong password for user registered with domain email "
                                + aDto.getDomainEmailAddress() + "." );
            }
            LOGGER.debug( "User with domain email {} has logged on", aDto.getDomainEmailAddress() );
            return ResponseEntity.ok( jwtService.createMainTokenAndRefreshToken( user.get() ) );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error when processing user login, email: {}, exception: {}",
                    aDto.getDomainEmailAddress(), aE.getMessage(), aE);
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Refreshes pair of JWTs.
     *
     * @param aRefreshToken refresh token.
     *
     * @return if token is valid, then Response Entity with 200 code and pair of new JWTs. If token is invalid,
     *         then Response Entity with 400 code. If unexpected error occurred, then Response Entity with 500 code.
     */
    @PostMapping( value = "/refresh/token" )
    public ResponseEntity< ? > refreshJWT( @RequestBody final Map< String, String > aRefreshToken )
    {
        try
        {
            final String refreshJWT = aRefreshToken.get( "refresh_token" );
            if( jwtService.validateToken( refreshJWT ) )
            {
                final Optional< String > userDomainEmail = jwtService.readDomainEmailAddressFromJWT( refreshJWT );
                if( userDomainEmail.isPresent() )
                {
                    final Optional< User > user = userService.tryToLoadByDomainEmail( userDomainEmail.get() );
                    if( user.isPresent() )
                    {
                        LOGGER.debug( "Refresh token renewed for user with email {}", user.get().getDomainEmailAddress() );
                        return ResponseEntity.status( HttpStatus.CREATED )
                                .body( jwtService.createMainTokenAndRefreshToken( user.get() ) );
                    }
                }
            }

            return ResponseEntity.badRequest()
                    .body( "Invalid token." );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error occurred when processing token refresh: {}", aE.getMessage(), aE );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    // TODO: test endpoint.
    @PostMapping( value = "/test" )
    public ResponseEntity< ? > test( @RequestBody String aString )
    {
        return ResponseEntity.ok( 200 );
    }
}
