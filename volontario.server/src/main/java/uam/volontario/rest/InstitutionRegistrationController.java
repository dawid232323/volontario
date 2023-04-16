package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.InstitutionDto;
import uam.volontario.handler.InstitutionRegistrationHandler;

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
    @PostMapping( value = "/reject" )
    public ResponseEntity< ? > rejectInstitution( @RequestParam( "token" ) final String aRegistrationToken )
    {
        return institutionRegistrationHandler.rejectInstitution( aRegistrationToken );
    }
}