package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.OfferDto;
import uam.volontario.handler.CrudOfferDataHandler;

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
