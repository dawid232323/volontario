package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.OfferDto;
import uam.volontario.handler.CrudOfferDataHandler;

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

    /**
     * CDI constructor.
     *
     * @param aOfferDataHandler fetch offer data handler.
     */
    @Autowired
    public OfferController( final CrudOfferDataHandler aOfferDataHandler )
    {
        crudOfferDataHandler = aOfferDataHandler;
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

    @PostMapping
    public ResponseEntity< ? > createNewOffer( @RequestBody final OfferDto aOfferDto )
    {
        return this.crudOfferDataHandler.createNewOffer( aOfferDto );
    }
}
