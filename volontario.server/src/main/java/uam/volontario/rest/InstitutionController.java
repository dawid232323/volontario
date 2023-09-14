package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.Institution.InstitutionDto;
import uam.volontario.handler.InstitutionHandler;
import uam.volontario.model.common.UserRole;

/**
 * Controller for {@linkplain uam.volontario.model.institution.impl.Institution} business logic.
 */
@RestController
@RequestMapping( value = "/api/institution",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class InstitutionController
{

    private final InstitutionHandler institutionHandler;

    /**
     * CDI constructor.
     *
     * @param aInstitutionHandler institution handler.
     */
    @Autowired
    public InstitutionController( final InstitutionHandler aInstitutionHandler )
    {
        institutionHandler = aInstitutionHandler;
    }

    /**
     * Returns institution details
     *
     * @param aInstitutionId institution id to retrieve
     *
     * @return Response with status 400 if institution with given id does not exist
     *          or response with status 200 with institution data
     */
    @GetMapping( "/{institution_id}" )
    public ResponseEntity< ? > getInstitutionDetails( @PathVariable( "institution_id" ) final Long aInstitutionId )
    {
        return institutionHandler.getInstitutionDetails( aInstitutionId );
    }

    /**
     * Updates institution data.
     *
     * @param aInstitutionId primary key of institution to be updated
     *
     * @param aInstitutionDto dto with all necessary information
     *
     * @return response with status 200 and institution data body if everything works fine,
     *          status 400 if institution with given id does not exist or status 500 in any other error
     */
    @PutMapping( "/{institution_id}" )
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionAdministrators( authentication.principal )" )
    public ResponseEntity< ? > updateInstitutionData( @PathVariable( "institution_id" ) final Long aInstitutionId,
                                                      @RequestBody final InstitutionDto aInstitutionDto )
    {
        return institutionHandler.updateInstitutionData( aInstitutionId, aInstitutionDto );
    }

    /**
     * Changes role of Institution Worker to {@linkplain UserRole#INSTITUTION_ADMIN}.
     *
     * @param aInstitutionId id of Institution.
     *
     * @param aInstitutionWorkerId id of Institution worker.
     *
     *
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 400 if:
     *                   - Institution or Institution Worker with provided ids do not exist.
     *                   - Institution is not active.
     *                   - Institution worker is Institution's {@linkplain uam.volontario.model.institution.impl.InstitutionContactPerson}
     *                     (owner in other words).
     *                   - Institution Worker with given id does not belong to Institution with given id.
     *         - Response Entity with code 500 if unexpected server side error occurred.
     */
    @PatchMapping( "/{institution_id}/{institution_worker_id}/mark-as-admin" )
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionAdministrators( authentication.principal )" )
    public ResponseEntity< ? > giveAdminPrivilegesToInstitutionWorker( @PathVariable( "institution_id" ) final Long aInstitutionId,
                                                                       @PathVariable( "institution_worker_id" ) final Long aInstitutionWorkerId )
    {
        return institutionHandler.changeRoleOfInstitutionWorker( aInstitutionId, aInstitutionWorkerId, UserRole.INSTITUTION_ADMIN );
    }

    /**
     * Changes role of Institution Worker to {@linkplain UserRole#INSTITUTION_EMPLOYEE}.
     *
     * @param aInstitutionId id of Institution.
     *
     * @param aInstitutionWorkerId id of Institution worker.
     *
     *
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 400 if:
     *                   - Institution or Institution Worker with provided ids do not exist.
     *                   - Institution is not active.
     *                   - Institution worker is Institution's {@linkplain uam.volontario.model.institution.impl.InstitutionContactPerson}
     *                     (owner in other words).
     *                   - Institution Worker with given id does not belong to Institution with given id.
     *         - Response Entity with code 500 if unexpected server side error occurred.
     */
    @PatchMapping( "/{institution_id}/{institution_worker_id}/mark-as-employee" )
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionAdministrators( authentication.principal )" )
    public ResponseEntity< ? > giveEmployeePrivilegesToInstitutionWorker( @PathVariable( "institution_id" ) final Long aInstitutionId,
                                                                          @PathVariable( "institution_worker_id" ) final Long aInstitutionWorkerId )
    {
        return institutionHandler.changeRoleOfInstitutionWorker( aInstitutionId, aInstitutionWorkerId, UserRole.INSTITUTION_EMPLOYEE );
    }

    /**
     * Returns list of institution workers.
     *
     * @param aInstitutionId id of institution that workers should be assigned to
     *
     * @return Response entity with list of given institution workers
     */
    @GetMapping( "/workers/{institution_id}" )
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionAdministrators( authentication.principal )" )
    public ResponseEntity< ? > getInstitutionWorkers( @PathVariable( "institution_id" ) final Long aInstitutionId )
    {
        return this.institutionHandler.getInstitutionWorkers( aInstitutionId );
    }

    /**
     * Retrieves list of all users that are assigned to any institution. Endpoint restricted for administrators only.
     *
     * @return Response entity with list of institution workers
     */
    @GetMapping( "/workers" )
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    public ResponseEntity< ? > getAllInstitutionWorkers()
    {
        return this.institutionHandler.getAllInstitutionWorkers();
    }
}
