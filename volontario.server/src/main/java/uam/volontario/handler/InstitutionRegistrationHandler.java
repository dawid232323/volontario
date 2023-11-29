package uam.volontario.handler;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uam.volontario.crud.service.InstitutionService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.RoleService;
import uam.volontario.crud.service.UserService;
import uam.volontario.dto.Institution.InstitutionDto;
import uam.volontario.dto.Institution.InstitutionEmployeeDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.security.mail.MailService;
import uam.volontario.security.util.VolontarioBase64Coder;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.InstitutionValidationService;
import uam.volontario.validation.service.entity.UserValidationService;

import java.time.Duration;
import java.time.Instant;
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

    private final UserValidationService userValidationService;

    private final InstitutionValidationService institutionValidationService;

    private final RoleService roleService;

    private final UserService userService;

    private final OfferService offerService;

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
     * @param aUserValidationService user validation service.
     *
     * @param aInstitutionValidationService institution validation service.
     *
     * @param aRoleService role service.
     */
    @Autowired
    public InstitutionRegistrationHandler(final DtoService aDtoService, final MailService aMailService,
                                          final InstitutionService aInstitutionService,
                                          final PasswordEncoder aPasswordEncoder,
                                          final UserValidationService aUserValidationService,
                                          final InstitutionValidationService aInstitutionValidationService,
                                          final RoleService aRoleService, final UserService aUserService,
                                          final OfferService aOfferService )
    {
        dtoService = aDtoService;
        mailService = aMailService;
        institutionService = aInstitutionService;
        passwordEncoder = aPasswordEncoder;
        userValidationService = aUserValidationService;
        institutionValidationService = aInstitutionValidationService;
        roleService = aRoleService;
        userService = aUserService;
        offerService = aOfferService;
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
                mailService.sendInstitutionVerificationMailAndPasswordSetting( institution );

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
        try
        {
            final Optional< Institution > optionalInstitution =
                    institutionService.loadByRegistrationToken( aRegistrationToken );

            if( optionalInstitution.isPresent() )
            {
                final Institution institution = optionalInstitution.get();
                institution.setActive( true );
                List<Offer> institutionOffers = institution.getOffers();
                institutionOffers.forEach( offer -> offer.setIsHidden( false ) );

                if ( !institution.getEmployees().isEmpty() )
                {
                    institution.setRegistrationToken( null ); // it will not be needed anymore.
                }

                offerService.saveAll( institutionOffers );
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
    @Transactional
    public ResponseEntity< ? > rejectInstitution( final String aRegistrationToken )
    {
        try
        {
            final Optional< Institution > optionalInstitution =
                    institutionService.loadByRegistrationToken( aRegistrationToken );

            if( optionalInstitution.isPresent() )
            {
                final Institution institution = optionalInstitution.get();
                institution.getEmployees().stream().map( User::getId ).forEach( userService::deleteEntity );
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
                        .creationDate( Instant.now() )
                        .build();

                final ValidationResult userValidationResult =
                        userValidationService.validateEntity( contactPersonUser );
                if( userValidationResult.isValidated() )
                {
                    contactPersonUser.setHashedPassword( passwordEncoder.encode( aPassword ) );
                    if ( institution.isActive() )
                    {
                        institution.setRegistrationToken( null ); // it will not be needed anymore.
                    }
                    institution.getEmployees().add( contactPersonUser );

                    institutionService.saveOrUpdate( institution );

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

    /**
     * Registers account for employee of Institution. Account is created with a random password which is not passed to
     * the user, but the account is linked to the Institution and user receives an email about setting password for his account.
     * Once user does it, the account becomes verified and may be used.
     *
     * @param aInstitutionEmployeeDto dto containing institution employee data.
     *
     * @return
     *     - Response Entity with code 201 and employee account if everything went as expected.
     *     - Response Entity with code 401 and failure reason if:
     *             - institution was not found.
     *             - dto contained data which did not pass user validation.
     *     - Response Entity with code 500 and exception message in case server-side error occurred.
     */
    public ResponseEntity< ? > registerInstitutionEmployee( final InstitutionEmployeeDto aInstitutionEmployeeDto )
    {
        try
        {
            final Optional< Institution > optionalInstitution =
                    institutionService.tryLoadEntity( aInstitutionEmployeeDto.getInstitutionId() );

            if( optionalInstitution.isPresent() )
            {
                final Institution institution = optionalInstitution.get();
                final String randomGeneratedPassword = "X" +
                        RandomStringUtils.randomNumeric( 5 ) +
                        RandomStringUtils.randomAlphabetic( 5 ) +
                        "_";

                final User employee = User.builder()
                        .firstName( aInstitutionEmployeeDto.getFirstName() )
                        .lastName( aInstitutionEmployeeDto.getLastName() )
                        .contactEmailAddress( aInstitutionEmployeeDto.getContactEmail() )
                        .institution( institution )
                        .roles( roleService.findByNameIn( UserRole
                                .mapUserRolesToRoleNames( List.of( UserRole.INSTITUTION_EMPLOYEE ) ) ) )
                        .phoneNumber( aInstitutionEmployeeDto.getPhoneNumber() )
                        .password( randomGeneratedPassword )
                        .isVerified( false ) // will be verified once new password is set.
                        .creationDate( Instant.now() )
                        .build();

                final ValidationResult employeeValidationResult =
                        userValidationService.validateEntity( employee );

                if( employeeValidationResult.isValidated() )
                {
                    employee.setHashedPassword( passwordEncoder.encode( randomGeneratedPassword ) );
                    institution.getEmployees().add( employee );

                    userService.saveOrUpdate( employee );
                    institutionService.saveOrUpdate( institution );

                    if( mailService.sendMailAboutInstitutionEmployeeAccountBeingCreated( employee ) )
                    {
                        return ResponseEntity.status( HttpStatus.CREATED )
                                .body( employeeValidationResult.getValidatedEntity() );
                    }
                    else
                    {
                        return ResponseEntity.status( HttpStatus.MULTI_STATUS )
                                .body( "Institution Employee account was created, but mail was not send." );
                    }
                }
                else
                {
                    return ResponseEntity.badRequest()
                            .body( employeeValidationResult.getValidationViolations() );
                }
            }
            else
            {
                return ResponseEntity.badRequest( )
                        .body( MessageGenerator.getInstutionNotFoundMessage( aInstitutionEmployeeDto.getInstitutionId() ) );
            }
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Sets password for newly created institution employee account.
     *
     * @param aRegistrationToken registration token (encoded in Base64 format contact email address of employee)
     *
     * @param aPassword password to be set.
     *
     * @param aInstitutionId id of institution to which employee belongs.
     *
     * @return
     *     - Response Entity with code 200  if everything went as expected.
     *     - Response Entity with code 401 and failure reason if:
     *             - institution was not found.
     *             - employee was not found.
     *             - password chosen by user did not pass validation.
     *             - id of passed institution doesn't match id of institution resolved from employee.
     *             - one week has passed since creating employee account.
     *     - Response Entity with code 500 and exception message in case server-side error occurred.
     */
    public ResponseEntity< ? > setPasswordForNewlyCreatedInstitutionEmployeeUser( final String aRegistrationToken,
                                                                                  final String aPassword,
                                                                                  final Long aInstitutionId )
    {
        try
        {
            final Optional< Institution > optionalInstitution =
                    institutionService.tryLoadEntity( aInstitutionId );

            if( optionalInstitution.isPresent() )
            {
                final String decodedContactEmail = VolontarioBase64Coder.decode( aRegistrationToken );
                final Optional< User > optionalEmployee = userService.tryToLoadByContactEmail(
                        decodedContactEmail );

                if( optionalEmployee.isPresent() )
                {
                    final User employee = optionalEmployee.get();
                    final Optional< ResponseEntity< ? > > hasErrorOccurredDuringEmployeeConstraintsCheck
                            = checkConstraintWhenEmployeeSetsPasswordForTheFirstTime( employee, aInstitutionId );

                    if( hasErrorOccurredDuringEmployeeConstraintsCheck.isPresent() )
                    {
                        return hasErrorOccurredDuringEmployeeConstraintsCheck.get();
                    }

                    employee.setPassword( aPassword );
                    final ValidationResult employeeValidationResult =
                            userValidationService.validateEntity( employee );

                    if( employeeValidationResult.isValidated() )
                    {

                        employee.setHashedPassword( passwordEncoder.encode( aPassword ) );

                        employee.setVerified( true );

                        userService.saveOrUpdate( employee );

                        return ResponseEntity.ok()
                                .build();
                    }
                    else
                    {
                        return ResponseEntity.badRequest()
                                .body( employeeValidationResult.getValidationViolations() );
                    }

                }
                else
                {
                    return  ResponseEntity.badRequest()
                            .body( MessageGenerator.getEmployeeWithMailNotFound( decodedContactEmail ) );
                }
            }
            else
            {
                return ResponseEntity.badRequest( )
                        .body( MessageGenerator.getInstutionNotFoundMessage( aInstitutionId ) );
            }
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    private Optional< ResponseEntity< ? > > checkConstraintWhenEmployeeSetsPasswordForTheFirstTime( final User aEmployee, final long aInstitutionId )
    {
        if( aEmployee.isVerified() )
        {
            return Optional.of( ResponseEntity.badRequest()
                    .body( "Employee has already set his password." ) );
        }

        if( !aEmployee.getInstitution().getId().equals( aInstitutionId ) )
        {
            return Optional.of( ResponseEntity.badRequest()
                    .body( "Institution mismatch." ) );
        }

        if( Duration.between( aEmployee.getCreationDate(), Instant.now() ).compareTo( Duration.ofDays( 7 ) ) > 0 )
        {
            return Optional.of( ResponseEntity.badRequest()
                    .body( "Time to set password has expired." ) );
        }

        return Optional.empty();
    }
}
