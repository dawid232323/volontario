package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.Institution.InterestCategoryDto;
import uam.volontario.handler.InterestCategoryHandler;

/**
 * Controller for API related to {@linkplain uam.volontario.model.volunteer.impl.InterestCategory}.
 */
@RestController
@RequestMapping( value = "/api/interest-category",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class InterestCategoryController
{
    private final InterestCategoryHandler interestCategoryHandler;

    /**
     * CDI constructor.
     *
     * @param aInterestCategoryHandler Interest Category handler.
     */
    @Autowired
    public InterestCategoryController( final InterestCategoryHandler aInterestCategoryHandler )
    {
        interestCategoryHandler = aInterestCategoryHandler;
    }

    /**
     * Loads all used Interest Categories.
     *
     * @return
     *         - Response Entity with code 200 and list of all used Interest Categories if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    @GetMapping( "/all-used" )
    public ResponseEntity< ? > loadAllUsedInterestCategories()
    {
        return interestCategoryHandler.loadAllUsedInterestCategories();
    }

    /**
     * Loads all not used Interest Categories.
     *
     * @return
     *         - Response Entity with code 200 and list of all not used Interest Categories if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @GetMapping( "/all-not-used" )
    public ResponseEntity< ? > loadAllNotUsedInterestCategories()
    {
        return interestCategoryHandler.loadAllNotUsedInterestCategories();
    }

    /**
     * Loads all Interest Categories.
     *
     * @return
     *         - Response Entity with code 200 and list of all Interest Categories if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @GetMapping
    public ResponseEntity< ? > loadAllInterestCategories()
    {
        return interestCategoryHandler.loadAllInterestCategories();
    }

    /**
     * Creates Interest Category instance.
     *
     * @param aInterestCategoryDto dto containing Interest Category data.
     *
     * @return
     *        - Response Entity with code 201 and created Interest Category if everything went as expected.
     *        - Response Entity with code 500 and error message in case of server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PostMapping
    public ResponseEntity< ? > createInterestCategory( @RequestBody final InterestCategoryDto aInterestCategoryDto )
    {
        return interestCategoryHandler.createInterestCategory( aInterestCategoryDto );
    }

    /**
     * Updates Interest Category instance.
     *
     * @param aInterestCategoryDto dto containing Interest Category data.
     *
     * @return
     *        - Response Entity with code 200 and updated Interest Category if everything went as expected.
     *        - Response Entity with code 500 and error message in case of server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PutMapping
    public ResponseEntity< ? > updateInterestCategory( @RequestBody final InterestCategoryDto aInterestCategoryDto )
    {
        return interestCategoryHandler.updateInterestCategory( aInterestCategoryDto );
    }

    /**
     * Removes Interest Category instance.
     *
     * @param aId id of Interest Category to remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @DeleteMapping( "/{id}" )
    public ResponseEntity< ? > removeInterestCategory( @PathVariable( "id" ) final Long aId )
    {
        return interestCategoryHandler.removeInterestCategory( aId );
    }

    /**
     * Softly removes Interest Category instance.
     *
     * @param aId id of Interest Category to softly remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @DeleteMapping( "/soft-delete/{id}" )
    public ResponseEntity< ? > softRemoveInterestCategory( @PathVariable( "id" ) final Long aId )
    {
        return interestCategoryHandler.softRemoveInterestCategory( aId );
    }

    /**
     * Reverts removal of Interest Category instance.
     *
     * @param aId id of Interest Category to revert soft removal.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PostMapping( "/revert-delete/{id}" )
    public ResponseEntity< ? > revertSoftRemovalOfInterestCategory( @PathVariable( "id" ) final Long aId )
    {
        return interestCategoryHandler.revertSoftRemovalOfInterestCategory( aId );
    }
}
