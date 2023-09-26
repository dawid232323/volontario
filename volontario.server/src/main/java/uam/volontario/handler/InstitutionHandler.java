package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.InstitutionService;
import uam.volontario.crud.service.RoleService;
import uam.volontario.crud.service.UserService;
import uam.volontario.dto.Institution.InstitutionDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.dto.user.InstitutionWorkerDto;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;

import java.util.List;
import java.util.Optional;

/**
 * Handler class for {@linkplain Institution} business logic.
 */
@Service
public class InstitutionHandler
{
    private final InstitutionService institutionService;

    private final UserService userService;

    private final DtoService dtoService;

    private final RoleService roleService;

    /**
     * CDI constructor.
     *
     * @param aInstitutionService institution service.
     *
     * @param aDtoService dto service.
     *
     * @param aRoleService role service.
     *
     * @param aUserService user service.
     */
    @Autowired
    public InstitutionHandler( final InstitutionService aInstitutionService, final RoleService aRoleService,
                               final DtoService aDtoService, final UserService aUserService )
    {
        institutionService = aInstitutionService;
        dtoService = aDtoService;
        userService = aUserService;
        roleService = aRoleService;
    }

    /**
     * Resolves Institution details.
     *
     * @param aInstitutionId Institution id used to resolve Institution.
     *
     * @return Response with status 400 if Institution with given id does not exist
     *          or response with status 200 with Institution data.
     */
    public ResponseEntity< ? > getInstitutionDetails( final Long aInstitutionId )
    {
        final Optional< Institution > institutionOptional = institutionService
                .tryLoadEntity( aInstitutionId );

        if( institutionOptional.isEmpty() )
        {
            return ResponseEntity.badRequest()
                    .body( MessageGenerator.getInstutionNotFoundMessage( aInstitutionId ) );
        }

        final Institution institution = institutionOptional.get();
        if( !institution.isActive() )
        {
            return ResponseEntity.badRequest()
                    .body( MessageGenerator.getInstitutionNotActiveMessage( aInstitutionId ) );
        }

        return ResponseEntity.ok( dtoService
                .getDtoFromInstitution( institution ) );
    }

    /**
     * Updates Institution data.
     *
     * @param aInstitutionId id of Institution to be updated.
     *
     * @param aInstitutionDto dto with all necessary information related to Institution.
     *
     * @return response with status 200 and Institution data body if everything works fine,
     *          status 400 if Institution with given id does not exist or status 500 in any other error
     */
    public ResponseEntity< ? > updateInstitutionData( final Long aInstitutionId,
                                                      final InstitutionDto aInstitutionDto )
    {
        final Optional< Institution > institutionOptional = institutionService
                .tryLoadEntity( aInstitutionId );

        if( institutionOptional.isEmpty() )
        {
            return ResponseEntity.badRequest()
                    .body( MessageGenerator.getInstutionNotFoundMessage( aInstitutionId ) );
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

        institutionService.saveOrUpdate( institution );

        return ResponseEntity.ok( dtoService
                .getDtoFromInstitution( institution ) );
    }

    /**
     * Returns list of institution workers.
     *
     * @param aInstitutionId id of institution that workers should be assigned to
     *
     * @return Response entity with list of given institution workers
     */
    public ResponseEntity< ? > getInstitutionWorkers( final Long aInstitutionId )
    {
        final List< InstitutionWorkerDto > workers = this.institutionService
                .getInstitutionWorkers( aInstitutionId )
                .stream().map( dtoService::getInstitutionWorkerDtoFromUser )
                .toList();
        return ResponseEntity.ok( workers );
    }

    /**
     * Retrieves list of all institution workers. Method should be restricted for moderators only.
     *
     * @return Response entity with list of all institution workers.
     */
    public ResponseEntity< ? > getAllInstitutionWorkers()
    {
        final List< InstitutionWorkerDto > workers = this.institutionService
                .getAllInstitutionWorkers()
                .stream().map( dtoService::getInstitutionWorkerDtoFromUser )
                .toList();
        return ResponseEntity.ok( workers );
    }

    /**
     * Changes role on Institution worker.
     *
     * @param aInstitutionId id of Institution.
     *
     * @param aInstitutionWorkerId id of Institution worker.
     *
     * @param aRoleToSet role to be set.
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
    public ResponseEntity< ? > changeRoleOfInstitutionWorker( final Long aInstitutionId, final Long aInstitutionWorkerId,
                                                              final UserRole aRoleToSet )
    {
        try
        {
            final Optional< Institution > optionalInstitution = institutionService.tryLoadEntity( aInstitutionId );

            if( optionalInstitution.isPresent() )
            {
                final Institution institution = optionalInstitution.get();
                if ( !institution.isActive() )
                {
                    return ResponseEntity.badRequest()
                            .body( MessageGenerator.getInstitutionNotVerifiedMessage( institution ) );
                }

                final Optional< User > optionalInstitutionWorker = userService.tryLoadEntity( aInstitutionWorkerId );

                if( optionalInstitutionWorker.isPresent() )
                {
                    final User institutionWorker = optionalInstitutionWorker.get();

                    if( institutionWorker.getContactEmailAddress()
                            .equals( institution.getInstitutionContactPerson()
                                    .getContactEmail() ) )
                    {
                        return ResponseEntity.badRequest()
                                .body( "Role of Institution owner can not be changed." );
                    }

                    if( institution.getEmployees()
                            .stream()
                            .noneMatch( worker -> worker.equals( institutionWorker ) ) )
                    {
                        return ResponseEntity.badRequest()
                                .body( MessageGenerator.getInstitutionWorkerNotInInstitutionMessage( aInstitutionWorkerId, aInstitutionId ) );
                    }

                    institutionWorker.setRoles( roleService.findByNameIn( UserRole
                            .mapUserRolesToRoleNames( List.of( aRoleToSet ) ) ) );

                    userService.saveOrUpdate( institutionWorker );

                    return ResponseEntity.ok()
                            .build();
                }

                return  ResponseEntity.badRequest()
                        .body( MessageGenerator.getInstitutionWorkerNotFoundMessage( aInstitutionWorkerId ) );
            }

            return ResponseEntity.badRequest( )
                    .body( MessageGenerator.getInstitutionNotFoundMessage( aInstitutionId ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
