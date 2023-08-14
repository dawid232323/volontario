package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.ExperienceLevelDto;
import uam.volontario.handler.ExperienceLevelHandler;

/**
 * Controller for API related to {@linkplain uam.volontario.model.volunteer.impl.ExperienceLevel}.
 */
@RestController
@RequestMapping( value = "/api/experience-level",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class ExperienceLevelController
{
    private final ExperienceLevelHandler experienceLevelHandler;

    /**
     * CDI constructor.
     *
     * @param aExperienceLevelHandler Experience Level handler.
     */
    @Autowired
    public ExperienceLevelController( final ExperienceLevelHandler aExperienceLevelHandler )
    {
        experienceLevelHandler = aExperienceLevelHandler;
    }

    /**
     * Loads all used Experience Levels.
     *
     * @return
     *         - Response Entity with code 200 and list of all used Experience Levels if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    @GetMapping( "/all-used" )
    public ResponseEntity< ? > loadAllUsedExperienceLevels()
    {
        return experienceLevelHandler.loadAllUsedExperienceLevels();
    }

    /**
     * Loads all not used Experience Levels.
     *
     * @return
     *         - Response Entity with code 200 and list of all not used Experience Levels if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @GetMapping( "/all-not-used" )
    public ResponseEntity< ? > loadAllNotUsedExperienceLevels()
    {
        return experienceLevelHandler.loadAllNotUsedExperienceLevels();
    }

    /**
     * Loads all Experience Levels.
     *
     * @return
     *         - Response Entity with code 200 and list of all Experience Levels if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @GetMapping
    public ResponseEntity< ? > loadAllExperienceLevels()
    {
        return experienceLevelHandler.loadAllExperienceLevels();
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
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PostMapping
    public ResponseEntity< ? > createExperienceLevel( @RequestBody final ExperienceLevelDto aExperienceLevelDto )
    {
        return experienceLevelHandler.createExperienceLevel( aExperienceLevelDto );
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
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PutMapping
    public ResponseEntity< ? > updateExperienceLevel( @RequestBody final ExperienceLevelDto aExperienceLevelDto )
    {
        return experienceLevelHandler.updateExperienceLevel( aExperienceLevelDto );
    }

    /**
     * Removes Experience Level instance.
     *
     * @param aId id of ExperienceLevel to remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @DeleteMapping( "/{id}" )
    public ResponseEntity< ? > removeExperienceLevel( @PathVariable( "id" ) final Long aId )
    {
        return experienceLevelHandler.removeExperienceLevel( aId );
    }

    /**
     * Softly removes Experience Level instance.
     *
     * @param aId id of ExperienceLevel to softly remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @DeleteMapping( "/soft-delete/{id}" )
    public ResponseEntity< ? > softRemoveExperienceLevel( @PathVariable( "id" ) final Long aId )
    {
        return experienceLevelHandler.softRemoveExperienceLevel( aId );
    }

    /**
     * Reverts removal of Experience Level instance.
     *
     * @param aId id of ExperienceLevel to revert soft removal.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PostMapping( "/revert-delete/{id}" )
    public ResponseEntity< ? > revertSoftRemovalOfExperienceLevel( @PathVariable( "id" ) final Long aId )
    {
        return experienceLevelHandler.revertSoftRemovalOfExperienceLevel( aId );
    }
}
