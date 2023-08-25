package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.Institution.InstitutionDto;
import uam.volontario.handler.InstitutionHandler;

@RestController
@RequestMapping( value = "/api/institution",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class InstitutionController
{

    private final InstitutionHandler institutionHandler;

    @Autowired
    public InstitutionController( final InstitutionHandler aInstitutionHandler )
    {
        this.institutionHandler = aInstitutionHandler;
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
        return this.institutionHandler.getInstitutionDetails( aInstitutionId );
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
        return this.institutionHandler.updateInstitutionData( aInstitutionId, aInstitutionDto );
    }
}
