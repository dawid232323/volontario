package uam.volontario.handler;

import jakarta.persistence.NoResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.BenefitService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.OfferTypeService;
import uam.volontario.crud.specification.OfferSpecification;
import uam.volontario.dto.Offer.OfferDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.OfferSearchQuery;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.OfferValidationService;

import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Handler class for {@linkplain uam.volontario.model.offer.impl.Offer} data fetching.
 */
@Service
public class CrudOfferDataHandler
{
    private final OfferService offerService;

    private final OfferTypeService offerTypeService;

    private final BenefitService benefitService;

    private final DtoService dtoService;

    private final OfferValidationService offerValidationService;

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
    public CrudOfferDataHandler(final OfferService aOfferService, final OfferTypeService aOfferTypeService,
                                final BenefitService aBenefitService, final DtoService aDtoService,
                                final OfferValidationService aOfferValidationService )
    {
        offerService = aOfferService;
        offerTypeService = aOfferTypeService;
        benefitService = aBenefitService;
        dtoService = aDtoService;
        offerValidationService = aOfferValidationService;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( CrudOfferDataHandler.class );

    /**
     * Loads base offers info from the system, filtered by given criteria.
     *
     * @param aOfferTypeId         offer type id.
     * @param aStartDate           start date - will return offers that start after given time.
     * @param aEndDate             end date - will return offers that end before given time.
     * @param aInterestCategoryIds interest categories id - will return offers with at least one matching.
     * @param aOfferPlace          offer place - will return offers where saved place contains this substring.
     * @param aExperienceLevelId   experience level id - will return offers with matching or lower required experience level.
     * @param isPoznanOnly         whether offer is Poznan only - will return matching.
     * @return Response Entity with code 200 and list of offers or Response Entity with code 500 when error
     * occurred during fetching offers.
     */
    public ResponseEntity< ? > loadBaseOffersInfoFiltered(String aTitle, Long aOfferTypeId, Date aStartDate, Date aEndDate,
                                                          List<Long> aInterestCategoryIds, String aOfferPlace,
                                                          Long aExperienceLevelId, Boolean isPoznanOnly,
                                                          Long aInstitutionId, Long aContactPersonId,
                                                          String aVisibility, Pageable pageable )
    {
        OfferSearchQuery query = new OfferSearchQuery( aTitle, aOfferTypeId, aStartDate, aEndDate, aInterestCategoryIds,
                aOfferPlace, aExperienceLevelId, isPoznanOnly, aVisibility, aInstitutionId, aContactPersonId );
        OfferSpecification specification = new OfferSpecification( query );

        try
        {
            return ResponseEntity.ok( offerService.findFiltered( specification, pageable ).map( dtoService::createBaseInfoDtoOfOffer ) );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on loading offers: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

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

    /**
     * Creates new offer
     * @param aOfferDto dto for new offer object
     * @return response with status 201 with body of newly created offer object,
     *         status of 400 if request has wrong data and 500 in case of any other errors
     */
    public ResponseEntity< ? > createNewOffer( final OfferDto aOfferDto )
    {
        try
        {
            Offer newOffer = this.dtoService.createOfferFromDto( aOfferDto );
            final ValidationResult offerValidationResult = this.offerValidationService
                    .validateEntity( newOffer );
            if ( offerValidationResult.isValidated() ) {
                newOffer = this.offerService.saveOrUpdate( newOffer );
                return ResponseEntity.status( HttpStatus.CREATED )
                        .body( newOffer );
            }
            return ResponseEntity.status( HttpStatus.BAD_REQUEST )
                    .body( offerValidationResult.getValidationViolations() );
        }
        catch ( NoResultException aNoResultException )
        {
            return ResponseEntity.badRequest().body( aNoResultException.getMessage() );
        }
        catch ( Exception aE )
        {
            aE.printStackTrace();
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    public ResponseEntity< ? > updateOffer(Long aId, OfferDto aOfferDto)
    {
        try
        {
            Offer offer = this.dtoService.createOfferFromDto( aOfferDto );
            final ValidationResult offerValidationResult = this.offerValidationService
                    .validateEntity( offer );
            if ( offerValidationResult.isValidated() ) {
                offer.setId( offerService.loadEntity( aId ).getId() ) ; //To check if entity being updated exists
                offer = this.offerService.saveOrUpdate( offer );
                return ResponseEntity.status( HttpStatus.OK )
                        .body( offer );
            }
            return ResponseEntity.status( HttpStatus.BAD_REQUEST )
                    .body( offerValidationResult.getValidationViolations() );
        }
        catch ( Exception aE )
        {
            aE.printStackTrace();
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    public ResponseEntity<?> loadOfferDetails( Long aId )
    {
        try
        {
            Optional<Offer> offer = offerService.tryLoadEntity(aId);
            if ( offer.isPresent() )
            {
                return ResponseEntity.ok( dtoService.createOfferDetailsDto( offer.get() ) );
            }
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Offer of id " + aId + " not found" );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on loading offer types: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
