package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
