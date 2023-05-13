package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.OfferDto;
import uam.volontario.handler.CrudOfferDataHandler;
import uam.volontario.handler.OfferAssignmentHandler;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.List;

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

    /**
     * CDI constructor.
     *
     * @param aOfferDataHandler fetch offer data handler.
     *
     * @param aOfferAssignmentHandler offer assignment handler.
     */
    @Autowired
    public OfferController( final CrudOfferDataHandler aOfferDataHandler,
                            final OfferAssignmentHandler aOfferAssignmentHandler )
    {
        crudOfferDataHandler = aOfferDataHandler;
        offerAssignmentHandler = aOfferAssignmentHandler;
    }

    /**
     * Loads all offers in the system.
     *
     * @return Response Entity with code 200 and list of offers or Response Entity with code 500 when error
     *         occurred during fetching offers.
     */
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
    @GetMapping( "/search" )
    public ResponseEntity< ? > loadBaseOffersInfoFiltered( @RequestParam( required = false ) String title,
                                                           @RequestParam( required = false ) Long offerTypeId,
                                                           @RequestParam( required = false ) @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                           @RequestParam( required = false ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                                           @RequestParam( required = false ) List<Long> interestCategoryIds,
                                                           @RequestParam( required = false ) List<Integer> offerWeekDays,
                                                           @RequestParam( required = false ) String offerPlace,
                                                           @RequestParam( required = false ) Long experienceLevelId,
                                                           @RequestParam( required = false ) Boolean isPoznanOnly,
                                                           @RequestParam( required = false ) Boolean isInsuranceNeeded,
                                                           @RequestParam( required = false ) Long institutionId,
                                                           @RequestParam( required = false ) Long contactPersonId,
                                                           Pageable aPageable
                                                           )
    {
        return crudOfferDataHandler.loadBaseOffersInfoFiltered(title, offerTypeId, startDate, endDate,
                interestCategoryIds, offerWeekDays, offerPlace, experienceLevelId, isPoznanOnly, isInsuranceNeeded,
                institutionId, contactPersonId, aPageable );
    }

    /**
     * Loads all benefits in the system.
     *
     * @return Response Entity with code 200 and list of benefits or Response Entity with code 500 when error
     *         occurred during fetching benefits.
     */
    @GetMapping( value = "/benefit" )
    public ResponseEntity< ? > loadAllBenefits()
    {
        return crudOfferDataHandler.loadAllBenefits();
    }

    /**
     * Loads all offer types in the system.
     *
     * @return Response Entity with code 200 and list of offers types or Response Entity with code 500 when error
     *         occurred during fetching offers types.
     */
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
    @GetMapping( "/unassigned" )
    public ResponseEntity< ? > loadUnassignedOffers()
    {
        return offerAssignmentHandler.loadAllUnassignedOffers();
    }

    @PutMapping( value = "/{id}" )
    public ResponseEntity< ? > updateOffer( @PathVariable( "id" ) Long aId, @RequestBody final OfferDto aOfferDto )
    {
        return this.crudOfferDataHandler.updateOffer( aId, aOfferDto );
    }

    @GetMapping( value = "/details/{id}" )
            public ResponseEntity< ? > getOffer( @PathVariable( "id" ) Long aId )
    {
        return this.crudOfferDataHandler.loadOfferDetails( aId );
    }
}
