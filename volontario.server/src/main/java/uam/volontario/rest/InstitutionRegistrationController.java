package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.Institution.InstitutionDto;
import uam.volontario.dto.Institution.InstitutionEmployeeDto;
import uam.volontario.handler.InstitutionRegistrationHandler;

import java.util.Map;

/**
 * Controller for API related to registering {@linkplain uam.volontario.model.institution.impl.Institution}.
 */
@RestController
@RequestMapping( value = "/api/institution",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class InstitutionRegistrationController
{
    private final InstitutionRegistrationHandler institutionRegistrationHandler;

    /**
     * CDI constructor.
     *
     * @param aInstitutionRegistrationHandler institution registration handler.
     */
    @Autowired
    public InstitutionRegistrationController( final InstitutionRegistrationHandler aInstitutionRegistrationHandler )
    {
        institutionRegistrationHandler = aInstitutionRegistrationHandler;
    }

    /**
     * Registers Institution.
     *
     * @param aInstitutionDto dto containing Institution registration data.
     *
     * @return if Institution passes validation, then ResponseEntity with 201 status and institution. If institution did not
     *         pass validation then ResponseEntity with 401 status and constraints violated. If there was an error,
     *         then ResponseEntity with 500 status and error message.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    @PostMapping( value = "/register" )
    public ResponseEntity< ? > registerInstitution( @RequestBody final InstitutionDto aInstitutionDto )
    {
        return institutionRegistrationHandler.registerInstitution( aInstitutionDto );
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
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PostMapping( value = "/accept" )
    public ResponseEntity< ? > acceptInstitution( @RequestParam( "token" ) final String aRegistrationToken )
    {
        return institutionRegistrationHandler.acceptInstitution( aRegistrationToken );
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
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PostMapping( value = "/reject" )
    public ResponseEntity< ? > rejectInstitution( @RequestParam( "token" ) final String aRegistrationToken )
    {
        return institutionRegistrationHandler.rejectInstitution( aRegistrationToken );
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
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    @PostMapping( value = "/register-contact-person" )
    public ResponseEntity< ? > registerInstitutionContactPerson( @RequestParam( "t" ) final String aRegistrationToken,
                                                                 @RequestBody final Map< String, String > aPassword )
    {
        return institutionRegistrationHandler.registerInstitutionContactPerson( aRegistrationToken,
                aPassword.get( "password" ) );
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
     *             - institution is not active.
     *             - dto contained data which did not pass user validation.
     *     - Response Entity with code 500 and exception message in case server-side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionAdministrators( authentication.principal )" )
    @PostMapping( value = "/register-employee" )
    public ResponseEntity< ? > registerInstitutionEmployee( @RequestBody final InstitutionEmployeeDto aInstitutionEmployeeDto )
    {
        return institutionRegistrationHandler.registerInstitutionEmployee( aInstitutionEmployeeDto );
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
     *             - institution is not active.
     *             - password chosen by user did not pass validation.
     *             - id of passed institution doesn't match id of institution resolved from employee.
     *             - one week has passed since creating employee account.
     *     - Response Entity with code 500 and exception message in case server-side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    @PostMapping( value = "/{institution_id}/register-employee/set-password" )
    public ResponseEntity< ? > setPasswordForNewlyCreatedInstitutionEmployeeUser( @PathVariable( "institution_id" ) final Long aInstitutionId,
                                                                                  @RequestParam( "t" ) final String aRegistrationToken,
                                                                                  @RequestBody final Map< String, String > aPassword )
    {
        return institutionRegistrationHandler.setPasswordForNewlyCreatedInstitutionEmployeeUser( aRegistrationToken,
                aPassword.get( "password" ), aInstitutionId );
    }
}
