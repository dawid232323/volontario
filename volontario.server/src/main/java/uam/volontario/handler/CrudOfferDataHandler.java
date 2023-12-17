package uam.volontario.handler;

import jakarta.mail.MessagingException;
import jakarta.persistence.NoResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.*;
import uam.volontario.crud.specification.OfferSpecification;
import uam.volontario.crud.specification.UserSpecification;
import uam.volontario.dto.Offer.OfferBaseInfoDto;
import uam.volontario.dto.Offer.OfferDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.common.impl.UserSearchQuery;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.OfferSearchQuery;
import uam.volontario.model.offer.impl.OfferStateEnum;
import uam.volontario.model.utils.ModelUtils;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.security.jwt.JWTService;
import uam.volontario.security.mail.MailService;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.OfferValidationService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Handler class for {@linkplain uam.volontario.model.offer.impl.Offer} data fetching.
 */
@Service
public class CrudOfferDataHandler
{
    private final OfferService offerService;

    private final OfferTypeService offerTypeService;

    private final DtoService dtoService;

    private final OfferValidationService offerValidationService;

    private final ApplicationService applicationService;
    private final JWTService jwtService;
    private final UserService userService;
    private final OfferStateService offerStateService;
    private final MailService mailService;

    /**
     * CDI constructor.
     *
     * @param aOfferService
     *                              offer service.
     * @param aOfferTypeService
     *                              offer type service.
     */
    @Autowired
    public CrudOfferDataHandler( final OfferService aOfferService, final OfferTypeService aOfferTypeService,
        final DtoService aDtoService, final OfferValidationService aOfferValidationService,
        final ApplicationService aApplicationService, final JWTService aJWTService,
        final UserService aUserService, final OfferStateService aOfferStateService,
        final MailService aMailService )
    {
        offerService = aOfferService;
        offerTypeService = aOfferTypeService;
        dtoService = aDtoService;
        offerValidationService = aOfferValidationService;
        applicationService = aApplicationService;
        jwtService = aJWTService;
        userService = aUserService;
        offerStateService = aOfferStateService;
        mailService = aMailService;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( CrudOfferDataHandler.class );

    /**
     * Loads base offers info from the system, filtered by given criteria.
     *
     * @param aOfferTypeId
     *                                 offer type id.
     * @param aStartDate
     *                                 start date - will return offers that start after given time.
     * @param aEndDate
     *                                 end date - will return offers that end before given time.
     * @param aInterestCategoryIds
     *                                 interest categories id - will return offers with at least one matching.
     * @param aOfferPlace
     *                                 offer place - will return offers where saved place contains this
     *                                 substring.
     * @param isPoznanOnly
     *                                 whether offer is Poznan only - will return matching.
     * @return Response Entity with code 200 and list of offers or Response Entity with code 500 when error
     *         occurred during fetching offers.
     */
    public ResponseEntity< ? > loadBaseOffersInfoFiltered( String aTitle, Long aOfferTypeId, Date aStartDate,
        Date aEndDate, List< Long > aInterestCategoryIds, String aOfferPlace,
        Boolean isPoznanOnly, Long aInstitutionId, Long aContactPersonId, String aVisibility,
        Pageable pageable )
    {
        OfferSearchQuery query = new OfferSearchQuery( aTitle, aOfferTypeId, aStartDate, aEndDate,
            aInterestCategoryIds, aOfferPlace, isPoznanOnly, aVisibility, aInstitutionId,
            aContactPersonId );
        OfferSpecification specification = new OfferSpecification( query );

        try
        {
            final Page< Offer > filteredOffers = offerService.findFiltered( specification, pageable );
            return ResponseEntity.ok( this.getProcessedOffers( filteredOffers ) );
        }
        catch( Exception aE )
        {
            LOGGER.error( "Error on loading offers", aE );
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
        catch( Exception aE )
        {
            LOGGER.error( "Error on loading offers: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( aE.getMessage() );
        }
    }

    /**
     * Loads all offer types in the system.
     *
     * @return Response Entity with code 200 and list of offers types or Response Entity with code 500 when
     *         error occurred during fetching offers types.
     */
    public ResponseEntity< ? > loadAllOfferTypes()
    {
        try
        {
            return ResponseEntity.ok( offerTypeService.loadAllEntities() );
        }
        catch( Exception aE )
        {
            LOGGER.error( "Error on loading offer types: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( aE.getMessage() );
        }
    }

    /**
     * Creates new offer
     * 
     * @param aOfferDto
     *                      dto for new offer object
     * @return response with status 201 with body of newly created offer object, status of 400 if request has
     *         wrong data and 500 in case of any other errors
     */
    public ResponseEntity< ? > createNewOffer( final OfferDto aOfferDto )
    {
        try
        {
            Offer newOffer = this.dtoService.createOfferFromDto( aOfferDto );
            final ValidationResult offerValidationResult =
                this.offerValidationService.validateEntity( newOffer );
            if( offerValidationResult.isValidated() )
            {
                if( !newOffer.getInstitution()
                    .isActive() )
                {
                    newOffer.setIsHidden( true );
                }
                newOffer = this.offerService.saveOrUpdate( newOffer );
                handleMailNotificationsForVolunteers( newOffer );
                return ResponseEntity.status( HttpStatus.CREATED )
                    .body( newOffer );
            }
            return ResponseEntity.status( HttpStatus.BAD_REQUEST )
                .body( offerValidationResult.getValidationViolations() );
        }
        catch( NoResultException aNoResultException )
        {
            return ResponseEntity.badRequest()
                .body( aNoResultException.getMessage() );
        }
        catch( Exception aE )
        {
            LOGGER.error( aE );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( aE.getMessage() );
        }
    }

    public ResponseEntity< ? > updateOffer( Long aId, OfferDto aOfferDto )
    {
        try
        {
            Offer offer = this.dtoService.createOfferFromDto( aOfferDto );
            Offer oldOffer = offerService.loadEntity( aId );
            if( shouldUpdateOfferState( oldOffer, offer ) )
            {
                offer.setOfferState( ModelUtils.resolveOfferState( OfferStateEnum.NEW, offerStateService ) );
            }
            final ValidationResult offerValidationResult =
                this.offerValidationService.validateEntity( offer );
            if( offerValidationResult.isValidated() )
            {
                offer.setId( oldOffer.getId() ); // To check if entity being updated exists
                offer = this.offerService.saveOrUpdate( offer );
                return ResponseEntity.status( HttpStatus.OK )
                    .body( offer );
            }
            return ResponseEntity.status( HttpStatus.BAD_REQUEST )
                .body( offerValidationResult.getValidationViolations() );
        }
        catch( Exception aE )
        {
            LOGGER.error( aE );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( aE.getMessage() );
        }
    }

    public ResponseEntity< ? > loadOfferDetails( Long aId )
    {
        try
        {
            Optional< Offer > offer = offerService.tryLoadEntity( aId );
            if( offer.isPresent() )
            {
                return ResponseEntity.ok( dtoService.createOfferDetailsDto( offer.get() ) );
            }
            return ResponseEntity.status( HttpStatus.NOT_FOUND )
                .body( MessageGenerator.getOfferNotFoundMessage( aId ) );
        }
        catch( Exception aE )
        {
            LOGGER.error( "Error on loading offer types", aE );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( aE.getMessage() );
        }
    }

    public ResponseEntity< ? > changeOfferVisibility( final Long aOfferId, boolean isHidden )
    {
        Optional< Offer > optionalOfferToUpdate;
        final Offer offerToUpdate;
        try
        {
            optionalOfferToUpdate = this.offerService.tryLoadEntity( aOfferId );
            if( optionalOfferToUpdate.isEmpty() )
            {
                return ResponseEntity.status( HttpStatus.BAD_REQUEST )
                    .body( MessageGenerator.getOfferNotFoundMessage( aOfferId ) );
            }
            offerToUpdate = optionalOfferToUpdate.get();
        }
        catch( Exception aException )
        {
            LOGGER.error( "Error on changing offer visibility", aException );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( aException.getMessage() );
        }
        if( offerToUpdate.getInstitution()
            .isActive() )
        {
            offerToUpdate.setIsHidden( isHidden );
            this.offerService.saveOrUpdate( offerToUpdate );
            return ResponseEntity.ok( this.dtoService.createBaseInfoDtoOfOffer( offerToUpdate ) );
        }
        return ResponseEntity.status( HttpStatus.FORBIDDEN )
            .body( MessageGenerator.getInstitutionNotActiveMessage( offerToUpdate.getInstitution()
                .getId() ) );
    }

    private Page< OfferBaseInfoDto > getProcessedOffers( final Page< Offer > aOffers )
    {
        final String userEmail = this.jwtService.getCurrentUserEmail();
        final Optional< User > loggedUserOptional = this.userService.tryToLoadByLogin( userEmail );

        if( loggedUserOptional.isEmpty() )
        {
            return aOffers.map( dtoService::createBaseInfoDtoOfOffer );
        }

        final User loggedUser = loggedUserOptional.get();

        if( !this.shouldLoadApplicationCount( loggedUser ) )
        {
            return aOffers.map( dtoService::createBaseInfoDtoOfOffer );
        }

        final Map< Long, Long > applicationsCount =
            this.applicationService.getApplicationsCountForOffers( aOffers.stream()
                .map( Offer::getId )
                .toList() );

        return aOffers.map( offer -> {
            final OfferBaseInfoDto mappedOffer = this.dtoService.createBaseInfoDtoOfOffer( offer );
            if( this.offerService.isUserEntitledToOfferDetails( loggedUser, offer ) )
            {
                mappedOffer.setApplicationsCount( applicationsCount.getOrDefault( offer.getId(), 0L ) );
            }
            return mappedOffer;
        } );
    }

    private Boolean shouldLoadApplicationCount( final User aLoggedUser )
    {
        return Stream
            .of( UserRole.ADMIN, UserRole.INSTITUTION_EMPLOYEE, UserRole.INSTITUTION_ADMIN, UserRole.MOD )
            .anyMatch( aLoggedUser::hasUserRole );
    }

    private boolean shouldUpdateOfferState( Offer oldOffer, Offer offer )
    {
        return oldOffer.getOfferStateAsEnum()
            .equals( OfferStateEnum.EXPIRING )
            && !offer.getExpirationDate()
                .equals( oldOffer.getExpirationDate() );
    }

    private void handleMailNotificationsForVolunteers( Offer newOffer )
    {
        if( !newOffer.getInstitution().isActive() )
        {
            return;
        }
        List< Long > interestCategoriesIds = newOffer.getInterestCategories()
            .stream()
            .map( InterestCategory::getId )
            .toList();
        userService.findFiltered( new UserSpecification( UserSearchQuery.builder()
            .interestCategoriesIds( interestCategoriesIds )
            .build() ), Pageable.unpaged() )
            .stream()
            .forEach( user -> {
                try
                {
                    mailService.sendEmailsAboutNewOffer( newOffer, user );
                }
                catch( MessagingException | IOException e )
                {
                    LOGGER.error( "Sending email about new offer failed for user {}, offer {}",
                        user.getContactEmailAddress(), newOffer.getTitle() );
                }
            } );
    }
}
