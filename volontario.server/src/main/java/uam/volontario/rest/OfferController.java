package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.Offer.OfferDto;
import uam.volontario.handler.BenefitHandler;
import uam.volontario.handler.CrudOfferDataHandler;
import uam.volontario.handler.OfferAssignmentHandler;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller for API related to {@linkplain uam.volontario.model.offer.impl.Offer}s.
 */
@RestController
@RequestMapping( value = "/api/offer",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class OfferController
{
    private final CrudOfferDataHandler crudOfferDataHandler;

    private final OfferAssignmentHandler offerAssignmentHandler;

    private final BenefitHandler benefitHandler;

    /**
     * CDI constructor.
     *
     * @param aOfferDataHandler fetch offer data handler.
     *
     * @param aOfferAssignmentHandler offer assignment handler.
     *
     * @param aBenefitHandler benefit handler.
     */
    @Autowired
    public OfferController( final CrudOfferDataHandler aOfferDataHandler,
                            final OfferAssignmentHandler aOfferAssignmentHandler, final BenefitHandler aBenefitHandler )
    {
        crudOfferDataHandler = aOfferDataHandler;
        offerAssignmentHandler = aOfferAssignmentHandler;
        benefitHandler = aBenefitHandler;
    }

    /**
     * Loads all offers in the system.
     *
     * @return Response Entity with code 200 and list of offers or Response Entity with code 500 when error
     *         occurred during fetching offers.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )" )
    @GetMapping
    public ResponseEntity< ? > loadAllOffers()
    {
        return crudOfferDataHandler.loadAllOffers();
    }

    /**
     * Loads offers from the system, filtered by passed criteria.
     *
     * @return Response Entity with code 200 and list of offers or Response Entity with code 500 when error
     *         occurred during fetching offers.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )" )
    @GetMapping( "/search" )
    public ResponseEntity< ? > loadBaseOffersInfoFiltered( @RequestParam( required = false ) String title,
                                                           @RequestParam( required = false ) Long offerTypeId,
                                                           @RequestParam( required = false ) @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                           @RequestParam( required = false ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                                           @RequestParam( required = false ) List<Long> interestCategoryIds,
                                                           @RequestParam( required = false ) String offerPlace,
                                                           @RequestParam( required = false ) Long experienceLevelId,
                                                           @RequestParam( required = false ) Boolean isPoznanOnly,
                                                           @RequestParam( required = false ) Long institutionId,
                                                           @RequestParam( required = false ) Long contactPersonId,
                                                           @RequestParam( required = false ) String visibility,
                                                           Pageable aPageable
                                                           )
    {
        return crudOfferDataHandler.loadBaseOffersInfoFiltered(title, offerTypeId, startDate, endDate,
                interestCategoryIds, offerPlace, experienceLevelId, isPoznanOnly,
                institutionId, contactPersonId, visibility, aPageable );
    }

    /**
     * Loads all benefits in the system.
     *
     * @return Response Entity with code 200 and list of benefits or Response Entity with code 500 when error
     *         occurred during fetching benefits.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )" )
    @GetMapping( value = "/benefit" )
    public ResponseEntity< ? > loadAllBenefits()
    {
        return benefitHandler.loadAllUsedBenefits();
    }

    /**
     * Loads all offer types in the system.
     *
     * @return Response Entity with code 200 and list of offers types or Response Entity with code 500 when error
     *         occurred during fetching offers types.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )" )
    @GetMapping( value = "/type" )
    public ResponseEntity< ? > loadAllOfferTypes()
    {
        return crudOfferDataHandler.loadAllOfferTypes();
    }

    /**
     * Creates new Offer.
     *
     * @param aOfferDto Offer DTO.
     *
     * @return Response with status 201 with body of newly created offer object or
     *         status of 400 if request had wrong data and 500 in case of any other errors.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitution( authentication.principal )" )
    @PostMapping
    public ResponseEntity< ? > createNewOffer( @RequestBody final OfferDto aOfferDto )
    {
        return crudOfferDataHandler.createNewOffer( aOfferDto );
    }

    /**
     * Assigns Offer to {@linkplain User} of role {@linkplain UserRole#MOD}.
     *
     * @param aIdsMap map which should contain keys "offerId" and "moderatorId" mapped to their respective ids.
     *
     * @return Response Entity with assigned Offer and status 200,
     *         Response Entity with status 400 if:
     *
     *              - there is no User with given id.
     *              - User with given id is not Moderator.
     *              - Offer with given id was not found.
     *
     *        or Response Entity with status 500 if an unexpected error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @PostMapping( value = "/assign" )
    public ResponseEntity< ? > assignOffer( @RequestBody final Map< String, Long > aIdsMap )
    {
        return offerAssignmentHandler.assignOffer( aIdsMap.get( "offerId" ), aIdsMap.get( "moderatorId" ) );
    }

    /**
     * Loads all Offers assigned to Moderator with given id.
     *
     * @param aModeratorId id of Moderator.
     *
     * @return Response Entity with Offers assigned to Moderator and status 200,
     *         Response Entity with status 400 if:
     *
     *              - there is no User with given id.
     *              - User with given id is not Moderator.
     *
     *        or Response Entity with status 500 if an unexpected error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @GetMapping( "/assigned" )
    public ResponseEntity< ? > loadOffersAssignedToModerator( @RequestParam( value = "mod" ) final Long aModeratorId )
    {
        return offerAssignmentHandler.loadOffersAssignedToModerator( aModeratorId );
    }

    /**
     * Loads all unassigned Offers.
     *
     * @return Response Entity with all unassigned Offers and status 200,
     *        or Response Entity with status 500 if an unexpected error occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    @GetMapping( "/unassigned" )
    public ResponseEntity< ? > loadUnassignedOffers()
    {
        return offerAssignmentHandler.loadAllUnassignedOffers();
    }

    @PreAuthorize( "@permissionEvaluator.allowForInstitutionRelatedToTheOffer( authentication.principal, #aId )" )
    @PutMapping( value = "/{id}" )
    public ResponseEntity< ? > updateOffer( @PathVariable( "id" ) Long aId, @RequestBody final OfferDto aOfferDto )
    {
        return this.crudOfferDataHandler.updateOffer( aId, aOfferDto );
    }

    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )" )
    @GetMapping( value = "/details/{id}" )
            public ResponseEntity< ? > getOffer( @PathVariable( "id" ) Long aId )
    {
        return this.crudOfferDataHandler.loadOfferDetails( aId );
    }

    /**
     * Changes visibility of offer with given id.
     *
     * @param aOfferId Primary key of the offer which state should be changed
     *
     * @param aVisibilityMap {@linkplain Map} with single key isHidden
     *
     * @return {@linkplain ResponseEntity} with code 200 when visibility is changed correctly,
 *                                      status 400 when offer with given id does not exist,
     *
     */
    @PatchMapping( "changeVisibility/{offerId}" )
    public ResponseEntity< ? > changeOfferVisibility( @PathVariable( "offerId" ) final Long aOfferId,
                                                      @RequestBody final Map<String, Boolean> aVisibilityMap )
    {
        return this.crudOfferDataHandler.changeOfferVisibility( aOfferId,
                aVisibilityMap.getOrDefault( "isHidden", false ) );
    }
}
