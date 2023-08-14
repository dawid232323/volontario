package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.dto.Institution.InterestCategoryDto;
import uam.volontario.model.volunteer.impl.InterestCategory;

@Service
public class InterestCategoryHandler
{
    private final InterestCategoryService interestCategoryService;

    /**
     * CDI controller.
     *
     * @param aInterestCategoryService InterestCategory service.
     */
    @Autowired
    public InterestCategoryHandler( final InterestCategoryService aInterestCategoryService )
    {
        interestCategoryService = aInterestCategoryService;
    }

    /**
     * Loads all used Interest Categories.
     *
     * @return
     *         - Response Entity with code 200 and list of all used Interest Categories if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    public ResponseEntity< ? > loadAllUsedInterestCategories()
    {
        try
        {
            return ResponseEntity.ok( interestCategoryService.findAllUsed() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all not used Interest Categories.
     *
     * @return
     *         - Response Entity with code 200 and list of all not used Interest Categories if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    public ResponseEntity< ? > loadAllNotUsedInterestCategories()
    {
        try
        {
            return ResponseEntity.ok( interestCategoryService.findAllNotUsed() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all Interest Categories.
     *
     * @return
     *         - Response Entity with code 200 and list of all Interest Categories if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    public ResponseEntity< ? > loadAllInterestCategories()
    {
        try
        {
            return ResponseEntity.ok( interestCategoryService.loadAllEntities() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
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
    public ResponseEntity< ? > createInterestCategory( final InterestCategoryDto aInterestCategoryDto )
    {
        try
        {
            final InterestCategory interestCategory = InterestCategory.builder()
                    .name( aInterestCategoryDto.getName() )
                    .isUsed( true )
                    .build();

            // TODO: validation for InterestCategory saving.
            return ResponseEntity.status( HttpStatus.CREATED )
                    .body( interestCategoryService.saveOrUpdate( interestCategory ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
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
    public ResponseEntity< ? > updateInterestCategory( final InterestCategoryDto aInterestCategoryDto )
    {
        try
        {
            final InterestCategory interestCategory = InterestCategory.builder()
                    .name( aInterestCategoryDto.getName() )
                    .isUsed( true )
                    .id( aInterestCategoryDto.getId() )
                    .build();

            // TODO: validation for InterestCategory update.
            return ResponseEntity.ok( interestCategoryService.saveOrUpdate( interestCategory ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
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
    public ResponseEntity< ? > removeInterestCategory( final Long aId )
    {
        try
        {
            interestCategoryService.deleteEntity( aId );
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
     * Softly removes Interest Category instance.
     *
     * @param aId id of Interest Category to softly remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    public ResponseEntity< ? > softRemoveInterestCategory( final Long aId )
    {
        try
        {
            interestCategoryService.tryLoadEntity( aId )
                    .ifPresent( interestCategoryService::softDelete );
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
     * Reverts removal of Interest Category instance.
     *
     * @param aId id of Interest Category to revert soft removal.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    public ResponseEntity< ? > revertSoftRemovalOfInterestCategory( final Long aId )
    {
        try
        {
            interestCategoryService.tryLoadEntity( aId )
                    .ifPresent( interestCategoryService::revertRemoval );
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
