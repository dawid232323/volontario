package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.ExperienceLevelService;
import uam.volontario.dto.ExperienceLevelDto;
import uam.volontario.model.volunteer.impl.ExperienceLevel;

@Service
public class ExperienceLevelHandler
{
    private final ExperienceLevelService experienceLevelService;

    /**
     * CDI controller.
     *
     * @param aExperienceLevelService Experience Level service.
     */
    @Autowired
    public ExperienceLevelHandler( final ExperienceLevelService aExperienceLevelService )
    {
        experienceLevelService = aExperienceLevelService;
    }

    /**
     * Loads all used Experience Levels.
     *
     * @return
     *         - Response Entity with code 200 and list of all used Experience Levels if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    public ResponseEntity< ? > loadAllUsedExperienceLevels()
    {
        try
        {
            return ResponseEntity.ok( experienceLevelService.findAllUsed() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all not used Experience Levels.
     *
     * @return
     *         - Response Entity with code 200 and list of all not used Experience Levels if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    public ResponseEntity< ? > loadAllNotUsedExperienceLevels()
    {
        try
        {
            return ResponseEntity.ok( experienceLevelService.findAllNotUsed() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all Experience Levels.
     *
     * @return
     *         - Response Entity with code 200 and list of all Experience Levels if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    public ResponseEntity< ? > loadAllExperienceLevels()
    {
        try
        {
            return ResponseEntity.ok( experienceLevelService.loadAllEntities() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Creates Experience Level instance.
     *
     * @param aExperienceLevelDto dto containing Experience Level data.
     *
     * @return
     *        - Response Entity with code 201 and created Experience Level if everything went as expected.
     *        - Response Entity with code 500 and error message in case of server side error.
     */
    public ResponseEntity< ? > createExperienceLevel( final ExperienceLevelDto aExperienceLevelDto )
    {
        try
        {
            final ExperienceLevel experienceLevel = ExperienceLevel.builder()
                    .name( aExperienceLevelDto.getName() )
                    .isUsed( true )
                    .build();

            // TODO: validation for ExperienceLevel saving.
            return ResponseEntity.status( HttpStatus.CREATED )
                    .body( experienceLevelService.saveOrUpdate( experienceLevel ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Updates Experience Level instance.
     *
     * @param aExperienceLevelDto dto containing Experience Level data.
     *
     * @return
     *        - Response Entity with code 200 and updated Experience Level if everything went as expected.
     *        - Response Entity with code 500 and error message in case of server side error.
     */
    public ResponseEntity< ? > updateExperienceLevel( final ExperienceLevelDto aExperienceLevelDto )
    {
        try
        {
            final ExperienceLevel experienceLevel = ExperienceLevel.builder()
                    .name( aExperienceLevelDto.getName() )
                    .isUsed( true )
                    .id( aExperienceLevelDto.getId() )
                    .build();

            // TODO: validation for ExperienceLevel update.
            return ResponseEntity.ok( experienceLevelService.saveOrUpdate( experienceLevel ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Removes Experience Level instance.
     *
     * @param aId id of Experience Level to remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    public ResponseEntity< ? > removeExperienceLevel( final Long aId )
    {
        try
        {
            experienceLevelService.deleteEntity( aId );
            return ResponseEntity.ok().
                    build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Softly removes Experience Level instance.
     *
     * @param aId id of Experience Level to softly remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    public ResponseEntity< ? > softRemoveExperienceLevel( final Long aId )
    {
        try
        {
            experienceLevelService.tryLoadEntity( aId )
                    .ifPresent( experienceLevelService::softDelete );
            return ResponseEntity.ok()
                    .build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Reverts removal of Experience Level instance.
     *
     * @param aId id of Experience Level to revert soft removal.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    public ResponseEntity< ? > revertSoftRemovalOfExperienceLevel( final Long aId )
    {
        try
        {
            experienceLevelService.tryLoadEntity( aId )
                    .ifPresent( experienceLevelService::revertRemoval );
            return ResponseEntity.ok()
                    .build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
