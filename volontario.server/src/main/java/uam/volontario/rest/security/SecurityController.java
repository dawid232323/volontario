package uam.volontario.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.crud.service.UserService;
import uam.volontario.dto.LoginDto;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.dto.convert.DtoConverter;
import uam.volontario.model.common.impl.User;
import uam.volontario.security.jwt.JWTService;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.UserValidationService;

/**
 * Controller for login, registration and other strict-security related functionalities.
 */
@RestController
@RequestMapping( value = "/api",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class SecurityController
{
    @Autowired
    private UserValidationService userValidationService;

    @Autowired
    private InterestCategoryService interestCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public ResponseEntity< ? > registerVolunteer( @RequestBody VolunteerDto aDto )
    {
        try
        {
            final User user = DtoConverter.createVolunteerFromDto( aDto, interestCategoryService::findByIds,
                    passwordEncoder::encode );
            final ValidationResult validationResult = userValidationService.validateVolunteerUser( user );

            if( validationResult.isValidated() )
            {
                userService.saveOrUpdate( user );
                return ResponseEntity.status( HttpStatus.CREATED )
                        .body( validationResult.getValidatedEntity() );
            }

            return ResponseEntity.badRequest()
                    .body( validationResult.getValidationViolations() );
        }
        catch ( Exception aE )
        {
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
    public ResponseEntity< ? > login( @RequestBody LoginDto aDto )
    {
        try
        {
            final User user = userService.tryToLoadByDomainEmail( aDto.getDomainEmailAddress() )
                    .orElse( null );

            if( user == null )
            {
                return ResponseEntity.badRequest()
                        .body( "No user with domain email " + aDto.getDomainEmailAddress()
                                + " is registered in the system." );
            }

            if( !passwordEncoder.matches( aDto.getPassword(), user.getHashedPassword() ) )
            {
                return ResponseEntity.badRequest()
                        .body( "Wrong password for user registered with domain email "
                                + aDto.getDomainEmailAddress() + "." );
            }

            return ResponseEntity.ok( jwtService.createMainTokenAndRefreshToken( user ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    @PostMapping( value = "/refresh/token" )
    public ResponseEntity< ? > refreshJWT( @RequestBody String aRefreshToken )
    {
        try
        {
            if( jwtService.validateToken( aRefreshToken ) )
            {
                final String userDomainEmail = jwtService.readDomainEmailAddressFromJWT( aRefreshToken );
                final User user = userService.tryToLoadByDomainEmail( userDomainEmail ).orElseThrow();

                return ResponseEntity.status( HttpStatus.CREATED )
                        .body( jwtService.createMainTokenAndRefreshToken( user ) );
            }

            return ResponseEntity.badRequest()
                    .body( "Refresh JWT is expired." );
        }
        catch ( Exception aE )
        {
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
