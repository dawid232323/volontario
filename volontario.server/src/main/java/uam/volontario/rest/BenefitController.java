package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.BenefitDto;
import uam.volontario.handler.BenefitHandler;
import uam.volontario.model.offer.impl.Benefit;

/**
 * Controller for API related to {@linkplain Benefit}.
 */
@RestController
@RequestMapping( value = "/api/benefit",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class BenefitController
{
    private final BenefitHandler benefitHandler;

    /**
     * CDI constructor.
     *
     * @param aBenefitHandler benefit handler.
     */
    @Autowired
    public BenefitController( final BenefitHandler aBenefitHandler )
    {
        benefitHandler = aBenefitHandler;
    }

    /**
     * Loads all used Benefits.
     *
     * @return
     *         - Response Entity with code 200 and list of all used Benefits if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    @GetMapping( "/all-used" )
    public ResponseEntity< ? > loadAllUsedBenefits()
    {
        return benefitHandler.loadAllUsedBenefits();
    }

    /**
     * Loads all not used Benefits.
     *
     * @return
     *         - Response Entity with code 200 and list of all not used Benefits if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @GetMapping( "/all-not-used" )
    public ResponseEntity< ? > loadAllNotUsedBenefits()
    {
        return benefitHandler.loadAllNotUsedBenefits();
    }

    /**
     * Loads all Benefits.
     *
     * @return
     *         - Response Entity with code 200 and list of all Benefits if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @GetMapping
    public ResponseEntity< ? > loadAllBenefits()
    {
        return benefitHandler.loadAllBenefits();
    }

    /**
     * Creates Benefit instance.
     *
     * @param aBenefitDto dto containing Benefit data.
     *
     * @return
     *        - Response Entity with code 201 and created Benefit if everything went as expected.
     *        - Response Entity with code 500 and error message in case of server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PostMapping
    public ResponseEntity< ? > createBenefit( @RequestBody final BenefitDto aBenefitDto )
    {
        return benefitHandler.createBenefit( aBenefitDto );
    }

    /**
     * Updates Benefit instance.
     *
     * @param aBenefitDto dto containing Benefit data.
     *
     * @return
     *        - Response Entity with code 200 and updated Benefit if everything went as expected.
     *        - Response Entity with code 500 and error message in case of server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PutMapping
    public ResponseEntity< ? > updateBenefit( @RequestBody final BenefitDto aBenefitDto )
    {
        return benefitHandler.updateBenefit( aBenefitDto );
    }

    /**
     * Removes Benefit instance.
     *
     * @param aId id of Benefit to remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @DeleteMapping( "/{id}" )
    public ResponseEntity< ? > removeBenefit( @PathVariable( "id" ) final Long aId )
    {
        return benefitHandler.removeBenefit( aId );
    }

    /**
     * Softly removes Benefit instance.
     *
     * @param aId id of Benefit to softly remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @DeleteMapping( "/soft-delete/{id}" )
    public ResponseEntity< ? > softRemoveBenefit( @PathVariable( "id" ) final Long aId )
    {
        return benefitHandler.softRemoveBenefit( aId );
    }

    /**
     * Reverts removal of Benefit instance.
     *
     * @param aId id of Benefit to revert soft removal.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PostMapping( "/revert-delete/{id}" )
    public ResponseEntity< ? > revertSoftRemovalOfBenefit( @PathVariable( "id" ) final Long aId )
    {
        return benefitHandler.revertSoftRemovalOfBenefit( aId );
    }
}
