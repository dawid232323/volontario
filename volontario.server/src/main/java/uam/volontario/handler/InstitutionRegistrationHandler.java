package uam.volontario.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.InstitutionService;
import uam.volontario.crud.service.RoleService;
import uam.volontario.crud.service.UserService;
import uam.volontario.dto.InstitutionDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.security.mail.MailService;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.InstitutionValidationService;
import uam.volontario.validation.service.entity.UserValidationService;

import java.util.List;
import java.util.Optional;

/**
 * Handler class for {@linkplain Institution} related operations.
 */
@Service
public class InstitutionRegistrationHandler
{
    private final DtoService dtoService;

    private final MailService mailService;

    private final InstitutionService institutionService;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final UserValidationService userValidationService;

    private final InstitutionValidationService institutionValidationService;

    private final RoleService roleService;

    /**
     * CDI constructor.
     *
     * @param aDtoService dto service.
     *
     * @param aMailService mail service.
     *
     * @param aInstitutionService institution service.
     *
     * @param aPasswordEncoder password encoder.
     *
     * @param aUserService user service.
     *
     * @param aUserValidationService user validation service.
     *
     * @param aInstitutionValidationService institution validation service.
     *
     * @param aRoleService role service.
     */
    @Autowired
    public InstitutionRegistrationHandler(final DtoService aDtoService, final MailService aMailService,
                                          final InstitutionService aInstitutionService,
                                          final PasswordEncoder aPasswordEncoder, final UserService aUserService,
                                          final UserValidationService aUserValidationService,
                                          final InstitutionValidationService aInstitutionValidationService,
                                          final RoleService aRoleService )
    {
        dtoService = aDtoService;
        mailService = aMailService;
        institutionService = aInstitutionService;
        passwordEncoder = aPasswordEncoder;
        userService = aUserService;
        userValidationService = aUserValidationService;
        institutionValidationService = aInstitutionValidationService;
        roleService = aRoleService;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( InstitutionRegistrationHandler.class );

    /**
     * Registers Institution.
     *
     * @param aInstitutionDto dto containing Institution registration data.
     *
     * @return if Institution passes validation, then ResponseEntity with 201 status and institution. If institution did not
     *         pass validation then ResponseEntity with 401 status and constraints violated. If there was an error,
     *         then ResponseEntity with 500 status and error message.
     */
    public ResponseEntity< ? > registerInstitution( final InstitutionDto aInstitutionDto )
    {
        try
        {
            final Institution institution = dtoService.createInstitutionFromDto( aInstitutionDto );
            final ValidationResult validationResult = institutionValidationService.validateEntity( institution );

            if( validationResult.isValidated() )
            {
                institutionService.saveOrUpdate( institution );
                mailService.sendInstitutionVerificationMailToModerator( institution );

                return ResponseEntity.status( HttpStatus.CREATED )
                        .body( institution );
            }

            return ResponseEntity.badRequest()
                    .body( validationResult.getValidationViolations() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Exception occurred during registration of institution: {}", aE.getMessage(), aE );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Accepts Institution.
     *
     * @param aRegistrationToken Institution's registration token.
     *
     * @return Response Entity with code 200 if registration token was valid, Response Entity with code 400 if
     *         registration token was invalid, and if any error occurs then Response Entity with code 500 and
     *         error message.
     */
    public ResponseEntity< ? > acceptInstitution( final String aRegistrationToken )
    {
        // TODO: this endpoint should only by accessible for administrator. Adjust once role system is implemented.
        try
        {
            final Optional< Institution > optionalInstitution =
                    institutionService.loadByRegistrationToken( aRegistrationToken );

            if( optionalInstitution.isPresent() )
            {
                final Institution institution = optionalInstitution.get();
                institution.setActive( true );
                institutionService.saveOrUpdate( institution );

                mailService.sendInstitutionAcceptedMail( institution );

                return ResponseEntity.ok().
                        build();
            }

            return ResponseEntity.badRequest()
                    .build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Rejects and removes Institution from the system.
     *
     * @param aRegistrationToken Institution's registration token.
     *
     * @return Response Entity with code 200 if registration token was valid, Response Entity with code 400 if
     *         registration token was invalid, and if any error occurs then Response Entity with code 500 and
     *         error message.
     */
    public ResponseEntity< ? > rejectInstitution( final String aRegistrationToken )
    {
        // TODO: this endpoint should only by accessible for administrator. Adjust once role system is implemented.
        try
        {
            final Optional< Institution > optionalInstitution =
                    institutionService.loadByRegistrationToken( aRegistrationToken );

            if( optionalInstitution.isPresent() )
            {
                final Institution institution = optionalInstitution.get();
                institutionService.deleteEntity( institution.getId() );

                mailService.sendInstitutionRejectedMail( institution );

                return ResponseEntity.ok().
                        build();
            }

            return ResponseEntity.badRequest()
                    .build();
        }
        catch( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Registers Institution Contact Person user account.
     *
     * @param aRegistrationToken institution registration token.
     *
     * @param aPassword user password.
     *
     * @return ResponseEntity with newly created user of contact person and code 201 if provided password passed
     *         validation, or ResponseEntity with code 400 if registration token is invalid or provided password did not
     *         pass validation (in this case also the reason is provided in response body) and if any error occurs then
     *         Response Entity with code 500 and error message.
     *
     */
    public ResponseEntity< ? > registerInstitutionContactPerson( final String aRegistrationToken, final String aPassword )
    {
        try
        {
            final Optional< Institution > optionalInstitution =
                    institutionService.loadByRegistrationToken( aRegistrationToken );

            if( optionalInstitution.isPresent() )
            {
                final Institution institution = optionalInstitution.get();
                if( !institution.isActive() )
                {
                    return ResponseEntity.badRequest()
                            .body( "Institution " + institution.getName() + " (KRS: " + institution.getKrsNumber() +
                            ") is not yet accepted by system administrator." );
                }

                final InstitutionContactPerson institutionContactPerson = institution.getInstitutionContactPerson();

                final User contactPersonUser = User.builder().firstName( institutionContactPerson.getFirstName() )
                        .lastName( institutionContactPerson.getLastName() )
                        .contactEmailAddress( institutionContactPerson.getContactEmail() )
                        .phoneNumber( institutionContactPerson.getPhoneNumber() )
                        .isVerified( true )
                        .password( aPassword )
                        .institution( institution )
                        .roles( roleService.findByNameIn( UserRole
                                .mapUserRolesToRoleNames( List.of( UserRole.INSTITUTION_ADMIN ) ) ) )
                        .build();

                final ValidationResult userValidationResult =
                        userValidationService.validateEntity( contactPersonUser );
                if( userValidationResult.isValidated() )
                {
                    contactPersonUser.setHashedPassword( passwordEncoder.encode( aPassword ) );

                    institution.setRegistrationToken( null ); // it will not be needed anymore.
                    institution.getEmployees().add( contactPersonUser );

                    institutionService.saveOrUpdate( institution );
                    userService.saveOrUpdate( contactPersonUser );

                    return ResponseEntity.status( HttpStatus.CREATED )
                            .body( userValidationResult.getValidatedEntity() );
                }

                return ResponseEntity.badRequest()
                        .body( userValidationResult.getValidationViolations() );
            }

            return ResponseEntity.badRequest( )
                    .body( "No institution found." );
        }
        catch( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
