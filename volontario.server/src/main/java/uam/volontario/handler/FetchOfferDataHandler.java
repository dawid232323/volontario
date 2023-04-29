package uam.volontario.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.BenefitService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.OfferTypeService;

/**
 * Handler class for {@linkplain uam.volontario.model.offer.impl.Offer} data fetching.
 */
@Service
public class FetchOfferDataHandler
{
    private final OfferService offerService;

    private final OfferTypeService offerTypeService;

    private final BenefitService benefitService;

    /**
     * CDI constructor.
     *
     * @param aOfferService offer service.
     *
     * @param aBenefitService benefit service.
     *
     * @param aOfferTypeService offer type service.
     */
    @Autowired
    public FetchOfferDataHandler( final OfferService aOfferService, final OfferTypeService aOfferTypeService,
                                  final BenefitService aBenefitService )
    {
        offerService = aOfferService;
        offerTypeService = aOfferTypeService;
        benefitService = aBenefitService;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( FetchOfferDataHandler.class );

    /**
     * Loads all offers in the system.
     *
     * @return Response Entity with code 200 and list of offers or Response Entity with code 500 when error
     *         occurred during fetching offers.
     */
    public ResponseEntity< ? > loadAllOffers()
    {
        try
        {
            return ResponseEntity.ok( offerService.loadAllEntities() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on loading offers: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all benefits in the system.
     *
     * @return Response Entity with code 200 and list of benefits or Response Entity with code 500 when error
     *         occurred during fetching benefits.
     */
    public ResponseEntity< ? > loadAllBenefits()
    {
        try
        {
            return ResponseEntity.ok( benefitService.loadAllEntities() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on loading benefits: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all offer types in the system.
     *
     * @return Response Entity with code 200 and list of offers types or Response Entity with code 500 when error
     *         occurred during fetching offers types.
     */
    public ResponseEntity< ? > loadAllOfferTypes()
    {
        try
        {
            return ResponseEntity.ok( offerTypeService.loadAllEntities() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on loading offer types: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
