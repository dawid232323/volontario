package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.InstitutionService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.UserService;
import uam.volontario.crud.service.VoluntaryRatingService;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.dto.rating.InstitutionRatingDataDto;
import uam.volontario.dto.rating.InstitutionRatingDto;
import uam.volontario.dto.rating.VolunteerRatingDataDto;
import uam.volontario.dto.rating.VolunteerRatingDto;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.VoluntaryPresence;
import uam.volontario.model.offer.impl.VoluntaryRating;
import uam.volontario.model.utils.ModelUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handler for {@linkplain uam.volontario.model.offer.impl.VoluntaryRating} business logic.
 */
@Service
public class VoluntaryRatingHandler 
{
    private final OfferService offerService;
    
    private final UserService userService;

    private final InstitutionService institutionService;
    
    private final VoluntaryRatingService voluntaryRatingService;

    private final DtoService dtoService;

    /**
     * CDI constructor.
     * 
     * @param aOfferService offer service.
     *                      
     * @param aUserService user service.
     *                     
     * @param aVoluntaryRatingService voluntary rating service.
     *
     * @param aInstitutionService institution service.
     *
     * @param aDtoService dto service.
     */
    @Autowired
    public VoluntaryRatingHandler( final OfferService aOfferService, final UserService aUserService,
                                   final VoluntaryRatingService aVoluntaryRatingService,
                                   final InstitutionService aInstitutionService, final DtoService aDtoService )
    {
        offerService = aOfferService;
        userService = aUserService;
        voluntaryRatingService = aVoluntaryRatingService;
        institutionService = aInstitutionService;
        dtoService = aDtoService;
    }

    /**
     * Rates Volunteer based on how he performed on given Offer.
     *
     * @param aVolunteerId id of Volunteer to rate.
     *
     * @param aOfferId id of Offer related to rating.
     *
     * @param aRating rating note.
     *
     * @param aFirstRating flag to indicate whether is first (initial) rating
     *
     * @param aRatingReason optional rating comment.
     *
     * @return
     *         - Response Entity with code 200 if everything went as expected.
     *         - Response Entity with code 400 if Volunteer presence wasn't confirmed,
     *                  if rating was out of bounds or if Volunteer was already rated.
     *         - Response Entity with code 501 when unexpected server side error occurs.
     */
    public ResponseEntity< ? > rateVolunteer( final Long aVolunteerId, final Long aOfferId, final int aRating,
                                              final String aRatingReason, final boolean aFirstRating )
    {
        try
        {
            final User volunteer = ModelUtils.resolveVolunteer( aVolunteerId, userService );
            final Offer offer = ModelUtils.resolveOffer( aOfferId, offerService );

            final Optional< ResponseEntity< ? > > optionalValidationError
                    = checkIfPresenceWasNotConfirmedOrIfRatingIsOutOfBounds( volunteer, offer, aRating );
            if( optionalValidationError.isPresent() )
            {
                return optionalValidationError.get();
            }

            final Optional< VoluntaryRating > optionalVoluntaryRating =
                    voluntaryRatingService.findByOfferAndVolunteer( offer, volunteer );

            if( optionalVoluntaryRating.isPresent() && !aFirstRating &&
                    optionalVoluntaryRating.get().getVolunteerRating() != null )
            {
                return ResponseEntity.badRequest()
                        .body( String.format( "Volunteer %s was already rated for Offer %s.",
                                volunteer.getUsername(), offer.getTitle() ) );
            }

            final VoluntaryRating voluntaryRating = optionalVoluntaryRating.orElseGet( () -> VoluntaryRating.builder()
                    .offer( offer )
                    .institution( offer.getInstitution() )
                    .volunteer( volunteer )
                    .build() );

            voluntaryRating.setVolunteerRating( aRating );
            voluntaryRating.setVolunteerRatingReason( aRatingReason );

            voluntaryRatingService.saveOrUpdate( voluntaryRating );

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
     * Rates Institution based on Volunteer organizational feeling on given Offer.
     *
     * @param aVolunteerId id of Volunteer who rates.
     *
     * @param aInstitutionId id of Institution to rate.
     *
     * @param aOfferId id of Offer related to rating.
     *
     * @param aRating rating note.
     *
     * @param aFirstRating flag to indicate whether is first (initial) rating
     *
     * @param aRatingReason optional rating comment.
     *
     * @return
     *         - Response Entity with code 200 if everything went as expected.
     *         - Response Entity with code 400 if Volunteer presence wasn't confirmed,
     *                  if rating was out of bounds or if Institution was already rated.
     *         - Response Entity with code 501 when unexpected server side error occurs.
     */
    public ResponseEntity< ? > rateInstitution( final Long aVolunteerId, final Long aInstitutionId,
                                                final Long aOfferId, final int aRating, final String aRatingReason,
                                                final boolean aFirstRating )
    {
        try
        {
            final User volunteer = ModelUtils.resolveVolunteer( aVolunteerId, userService );
            final Institution institution = ModelUtils.resolveInstitution( aInstitutionId, institutionService );
            final Offer offer = ModelUtils.resolveOffer( aOfferId, offerService );

            final Optional< ResponseEntity< ? > > optionalValidationError
                    = checkIfPresenceWasNotConfirmedOrIfRatingIsOutOfBounds( volunteer, offer, aRating );
            if( optionalValidationError.isPresent() )
            {
                return optionalValidationError.get();
            }

            final Optional< VoluntaryRating > optionalVoluntaryRating =
                    voluntaryRatingService.findByOfferAndVolunteer( offer, volunteer );

            if( optionalVoluntaryRating.isPresent() && !aFirstRating &&
                    optionalVoluntaryRating.get().getInstitutionRating() != null )
            {
                return ResponseEntity.badRequest()
                        .body( String.format( "Institution %s was already rated for Offer %s.",
                                institution.getName(), offer.getTitle() ) );
            }

            final VoluntaryRating voluntaryRating = optionalVoluntaryRating.orElseGet( () -> VoluntaryRating.builder()
                    .offer( offer )
                    .institution( offer.getInstitution() )
                    .volunteer( volunteer )
                    .build() );

            voluntaryRating.setInstitutionRating( aRating );
            voluntaryRating.setInstitutionRatingReason( aRatingReason );

            voluntaryRatingService.saveOrUpdate( voluntaryRating );

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
     * Resolves Institution's average rating and list of all ratings.
     *
     * @param aInstitutionId id of Institution.
     *
     * @return
     *        - Response Entity with code 200 and rating data.
     *        - Response Entity with code 500 in case of unexpected server side error.
     */
    public ResponseEntity< ? > resolveInstitutionRatingData( final Long aInstitutionId )
    {
        try
        {
            final Institution institution = ModelUtils.resolveInstitution( aInstitutionId, institutionService );
            final List< VoluntaryRating > institutionRatings = voluntaryRatingService.findAllByInstitution( institution )
                    .stream()
                    .filter( voluntaryRating -> voluntaryRating.getInstitutionRating() != null )
                    .toList();

            final List< InstitutionRatingDto > ratingsDto = institutionRatings.stream()
                    .map( dtoService::institutionRatingToDto )
                    .toList();

            final double ratingsSum = (double)institutionRatings.stream().map( VoluntaryRating::getInstitutionRating )
                    .reduce( Integer::sum )
                    .orElse( Integer.MIN_VALUE );

            return createInstitutionRatingDto( ratingsSum, institutionRatings.size(), ratingsDto );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Resolves Volunteer's average rating and list of all ratings.
     *
     * @param aVolunteerId id of Volunteer.
     *
     * @return
     *        - Response Entity with code 200 and rating data.
     *        - Response Entity with code 500 in case of unexpected server side error.
     */
    public ResponseEntity< ? > resolveVolunteerRatingData( final Long aVolunteerId )
    {
        try
        {
            final User volunteer = ModelUtils.resolveVolunteer( aVolunteerId, userService );
            final List< VoluntaryRating > volunteerRatings = voluntaryRatingService.findAllByVolunteer( volunteer )
                    .stream()
                    .filter( voluntaryRating -> voluntaryRating.getVolunteerRating() != null )
                    .toList();

            final double ratingsSum = (double)volunteerRatings.stream().map( VoluntaryRating::getVolunteerRating )
                    .reduce( Integer::sum )
                    .orElse( Integer.MIN_VALUE );

            final List< VolunteerRatingDto > ratingsDto = volunteerRatings.stream()
                    .map( dtoService::volunteerRatingToDto )
                    .toList();

            return createVolunteerRatingDto( ratingsSum, volunteerRatings.size(), ratingsDto );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }



    private Optional< VoluntaryPresence > findConfirmedVoluntaryPresenceWithGivenVolunteerAndOffer(
            final User aVolunteer, final Offer aOffer )
    {
        return aVolunteer.getVoluntaryPresences().stream()
                .filter( voluntaryPresence -> voluntaryPresence.getOffer()
                        .equals( aOffer ) && voluntaryPresence.isPresenceConfirmed() )
                .findAny();
    }

    private Optional< ResponseEntity < ? > > checkIfPresenceWasNotConfirmedOrIfRatingIsOutOfBounds(
            final User aVolunteer, final Offer aOffer, final int aRating )
    {
        final Optional< VoluntaryPresence > optionalVoluntaryPresence
                = findConfirmedVoluntaryPresenceWithGivenVolunteerAndOffer( aVolunteer, aOffer );

        if( optionalVoluntaryPresence.isEmpty() )
        {
            return Optional.of( ResponseEntity.badRequest()
                    .body( String.format( "Volunteer %s does not have his presence confirmed for Offer %s.",
                            aVolunteer.getUsername(), aOffer.getTitle() ) ) );
        }
        if( aRating < 0 || aRating > 5 )
        {
            return Optional.of( ResponseEntity.badRequest()
                    .body( "Allowed rating values are between 0 and 5." ) );
        }

        return Optional.empty();
    }

    /**
     * Resolves all Offers in which Volunteer participated and which can be rated by him.
     *
     * @param aVolunteerId id of Volunteer.
     *
     * @return
     *       - Response Entity with code 200 and list of Offers Volunteer can rate.
     *       - Response Entity with code 500 in case of unexpected server side error.
     */
    public ResponseEntity< ? > resolveOffersLeftToRateForVolunteer( final Long aVolunteerId )
    {
        try
        {
            final User volunteer = ModelUtils.resolveVolunteer( aVolunteerId, userService );

            final List< Offer > offersLeftToRate = volunteer.getVoluntaryPresences().stream()
                    .filter( VoluntaryPresence::isPresenceConfirmed )
                    .map( VoluntaryPresence::getOffer )
                    .map( offer -> offer.getVoluntaryRatings().stream()
                            .filter( voluntaryRating -> voluntaryRating.getVolunteer()
                                    .equals( volunteer ) ).findAny() )
                    .filter( Optional::isPresent )
                    .map( Optional::get )
                    .filter( voluntaryRating -> voluntaryRating.getInstitutionRating() == null )
                    .map( VoluntaryRating::getOffer )
                    .distinct()
                    .toList();

            return ResponseEntity.ok( Map.of( "offersLeftToRate", offersLeftToRate ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Resolves all Offers of Institution in which given Volunteer participated and which Institution can rate.
     *
     * @param aVolunteerId id of Volunteer.
     *
     * @param aInstitutionId id of Institution/
     *
     * @return
     *       - Response Entity with code 200 and list of Offers Institution can rate for given Volunteer.
     *       - Response Entity with code 500 in case of unexpected server side error.
     */
    public ResponseEntity< ? > resolveOffersLeftToRateForInstitutionForGivenVolunteer( final Long aInstitutionId,
                                                                                       final Long aVolunteerId )
    {
        try
        {
            final Institution institution = ModelUtils.resolveInstitution( aInstitutionId, institutionService );
            final User volunteer = ModelUtils.resolveVolunteer( aVolunteerId, userService );

            final List< Offer > offersLeftToRate = volunteer.getVoluntaryPresences().stream()
                    .filter( VoluntaryPresence::isPresenceConfirmed )
                    .map( VoluntaryPresence::getOffer )
                    .filter( offer -> offer.getInstitution().equals( institution ) )
                    .map( offer -> offer.getVoluntaryRatings().stream()
                            .filter( voluntaryRating -> voluntaryRating.getVolunteer()
                                    .equals( volunteer ) ).findAny() )
                    .filter( Optional::isPresent )
                    .map( Optional::get )
                    .filter( voluntaryRating -> voluntaryRating.getVolunteerRating() == null )
                    .map( VoluntaryRating::getOffer )
                    .distinct()
                    .toList();

            return ResponseEntity.ok( Map.of( "offersLeftToRate", offersLeftToRate ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    private ResponseEntity< ? > createInstitutionRatingDto( final double aRatingsSum, final int aRatingsQuantity,
                                                            final List< InstitutionRatingDto > aInstitutionRatingDtos )
    {
        if( aRatingsSum < 0 )
        {
            return ResponseEntity.ok()
                    .body( InstitutionRatingDataDto.builder()
                            .averageRating( null )
                            .institutionRatings( Collections.emptyList() ) );
        }

        return ResponseEntity.ok()
                .body( InstitutionRatingDataDto.builder()
                        .averageRating( aRatingsSum / ((double)aRatingsQuantity) )
                        .institutionRatings( aInstitutionRatingDtos ) );
    }

    private ResponseEntity< ? > createVolunteerRatingDto( final double aRatingsSum, final int aRatingsQuantity,
                                                            final List< VolunteerRatingDto > aVolunteerRatingDtos )
    {
        if( aRatingsSum < 0 )
        {
            return ResponseEntity.ok()
                    .body( VolunteerRatingDataDto.builder()
                            .averageRating( null )
                            .volunteerRatings( Collections.emptyList() ) );
        }

        return ResponseEntity.ok()
                .body( VolunteerRatingDataDto.builder()
                        .averageRating( aRatingsSum / ((double)aRatingsQuantity) )
                        .volunteerRatings( aVolunteerRatingDtos ) );
    }
}
