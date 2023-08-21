package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.InstitutionService;
import uam.volontario.dto.Institution.InstitutionDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.institution.impl.Institution;

import java.util.Optional;

@Service
public class InstitutionHandler
{
    private final InstitutionService institutionService;
    private final DtoService dtoService;

    @Autowired
    public InstitutionHandler( final InstitutionService aInstitutionService,
                               final DtoService aDtoService )
    {
        this.institutionService = aInstitutionService;
        this.dtoService = aDtoService;
    }

    /**
     * Returns institution details
     *
     * @param aInstitutionId institution id to retrieve
     *
     * @return Response with status 400 if institution with given id does not exist
     *          or response with status 200 with institution data
     */
    public ResponseEntity< ? > getInstitutionDetails( final Long aInstitutionId )
    {
        final Optional< Institution > institutionOptional = this.institutionService
                .tryLoadEntity( aInstitutionId );
        if( institutionOptional.isEmpty() )
        {
            return ResponseEntity.badRequest()
                    .body( "Institution with given id does not exist" );
        }
        final Institution institution = institutionOptional.get();
        if( !institution.isActive() )
        {
            return ResponseEntity.badRequest()
                    .body( "Institution with given id does not exist" );
        }
        final InstitutionDto institutionDto = this.dtoService
                .getDtoFromInstitution( institution );
        return ResponseEntity.ok( institutionDto );
    }
}
