package uam.volontario.rest.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uam.volontario.crud.service.UserService;
import uam.volontario.dto.LoginDto;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.common.impl.User;
import uam.volontario.security.jwt.JWTService;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.UserValidationService;

import java.util.Map;
import java.util.Optional;

// TODO: I think that we should separate logic from Controller classes, look at InstitutionRegistrationController class.
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
     * @param aUserValidationService volunteer validation service.
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

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( SecurityController.class );

    /**
     * Registers volunteer.
     *
     * @param aDto dto containing registration data.
     *
     * @return if volunteer passes validation, then ResponseEntity with 201 status and volunteer. If volunteer did not
     *         pass validation then ResponseEntity with 400 status and constraints violated. If there was an error,
     *         then ResponseEntity with 500 status and error message.
     */
    @PostMapping( value = "/volunteer/register" )
    public ResponseEntity< ? > registerVolunteer( @RequestBody final VolunteerDto aDto )
    {
        try
        {
            final User user = dtoService.createVolunteerFromDto( aDto );
            final ValidationResult validationResult = userValidationService.validateEntity( user );

            if( validationResult.isValidated() )
            {
                user.setHashedPassword( passwordEncoder.encode( user.getPassword() ) );
                userService.saveOrUpdate( user );

                LOGGER.debug( "A new user has been registered for contact email {}, userId: {}",
                        user.getContactEmailAddress(), user.getId() );

                return ResponseEntity.status( HttpStatus.CREATED )
                        .body( validationResult.getValidatedEntity() );
            }

            LOGGER.debug( "Validation failed for new user with contact email {}, violations: {}", aDto.getContactEmail(),
                    validationResult.getValidationViolations().values() );

            return ResponseEntity.badRequest()
                    .body( validationResult.getValidationViolations() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Exception occurred during registration of volunteer: {}", aE.getMessage(), aE );
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
     *         give correct logging data then ResponseEntity with 400 status and reason. If there was an error,
     *         then ResponseEntity with 500 status and error message.
     */
    @PostMapping( value = "/login" )
    public ResponseEntity< ? > login( @RequestBody final LoginDto aDto )
    {
        try
        {
            final Optional< User > user = userService.tryToLoadByLogin( aDto.getLogin() );

            if( user.isEmpty() )
            {
                return ResponseEntity.badRequest()
                        .body( "No user with phone number/contact email " + aDto.getLogin()
                                + " is registered in the system." );
            }

            if( !passwordEncoder.matches( aDto.getPassword(), user.get().getHashedPassword() ) )
            {
                return ResponseEntity.badRequest()
                        .body( "Wrong password for user registered with phone number/contact email "
                                + aDto.getLogin() + "." );
            }
            LOGGER.debug( "User with phone number/contact email {} has logged on", aDto.getLogin() );
            return ResponseEntity.ok( jwtService.createMainTokenAndRefreshToken( user.get() ) );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error when processing user phone number/contact email {}, exception: {}",
                    aDto.getLogin(), aE.getMessage(), aE);
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Refreshes pair of JWTs.
     *
     * @param aRefreshToken refresh token.
     *
     * @return if token is valid, then Response Entity with 200 code and a pair of new JWTs. If token is invalid,
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
                final Optional< String > userContactEmail = jwtService.readContactEmailAddressFromJWT( refreshJWT );
                if( userContactEmail.isPresent() )
                {
                    final Optional< User > user = userService.tryToLoadByContactEmail( userContactEmail.get() );
                    if( user.isPresent() )
                    {
                        LOGGER.debug( "Refresh token renewed for user with contact email {}",
                                user.get().getContactEmailAddress() );
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
}
