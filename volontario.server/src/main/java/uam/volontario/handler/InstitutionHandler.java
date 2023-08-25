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
    public ResponseEntity< ? > updateInstitutionData( final Long aInstitutionId,
                                                      final InstitutionDto aInstitutionDto )
    {
        final Optional< Institution > institutionOptional = this.institutionService
                .tryLoadEntity( aInstitutionId );
        if( institutionOptional.isEmpty() )
        {
            return ResponseEntity.badRequest()
                    .body( "Institution with given id does not exist" );
        }
        Institution institution = institutionOptional.get();
        institution = institution.toBuilder()
                .name( aInstitutionDto.getName() )
                .krsNumber( aInstitutionDto.getKrsNumber() )
                .headquarters( aInstitutionDto.getHeadquartersAddress() )
                .institutionTags( String.join( ",", aInstitutionDto.getTags() ) )
                .description( aInstitutionDto.getDescription() )
                .localization( aInstitutionDto.getLocalization() )
                .build();
        institution = this.institutionService.saveOrUpdate( institution );
        return ResponseEntity.ok( this.dtoService
                .getDtoFromInstitution( institution ) );
    }
}
