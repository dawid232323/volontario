package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.rating.RatingRequestDto;
import uam.volontario.handler.VoluntaryRatingHandler;

/**
 * Controller for API related to {@linkplain uam.volontario.model.offer.impl.VoluntaryRating}s.
 */
@RestController
@RequestMapping( value = "/api/rating",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class VoluntaryRatingController
{
    private final VoluntaryRatingHandler voluntaryRatingHandler;

    /**
     * CDI controller.
     *
     * @param aVoluntaryRatingHandler voluntary rating handler.
     */
    @Autowired
    public VoluntaryRatingController( final VoluntaryRatingHandler aVoluntaryRatingHandler )
    {
        voluntaryRatingHandler = aVoluntaryRatingHandler;
    }

    /**
     * Rates Volunteer based on how he performed on given Offer.
     *
     * @param aVolunteerId id of Volunteer to rate.
     *
     * @param aRatingRequestDto rating request dto.
     *
     * @return
     *         - Response Entity with code 200 if everything went as expected.
     *         - Response Entity with code 400 if Volunteer presence wasn't confirmed,
     *                  if rating was out of bounds or if Volunteer was already rated.
     *         - Response Entity with code 501 when unexpected server side error occurs.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionRelatedToTheOffer( authentication.principal, " +
            "#aRatingRequestDto.offerId )" )
    @PostMapping( "/volunteer/{volunteerId}" )
    public ResponseEntity< ? > rateVolunteer( @PathVariable( "volunteerId" ) final Long aVolunteerId,
                                              @RequestBody final RatingRequestDto aRatingRequestDto )
    {
        return voluntaryRatingHandler.rateVolunteer( aVolunteerId, aRatingRequestDto.getOfferId(),
                aRatingRequestDto.getRating(), aRatingRequestDto.getRatingReason(), true );
    }

    /**
     * Rates Institution based on Volunteer organizational feeling on given Offer.
     *
     * @param aInstitutionId id of Institution to rate.
     *
     * @param aRatingRequestDto rating request dto.
     *
     * @return
     *         - Response Entity with code 200 if everything went as expected.
     *         - Response Entity with code 400 if Volunteer presence wasn't confirmed,
     *                  if rating was out of bounds or if Institution was already rated.
     *         - Response Entity with code 501 when unexpected server side error occurs.
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteerWhichHasAcceptedApplicationForGivenOffer( authentication.principal," +
            " #aRatingRequestDto.offerId )" )
    @PostMapping( "/institution/{institutionId}" )
    public ResponseEntity< ? > rateInstitution( @PathVariable( "institutionId" ) final Long aInstitutionId,
                                                @RequestBody final RatingRequestDto aRatingRequestDto )
    {
        return voluntaryRatingHandler.rateInstitution( aRatingRequestDto.getVolunteerId(), aInstitutionId,
                aRatingRequestDto.getOfferId(), aRatingRequestDto.getRating(), aRatingRequestDto.getRatingReason(),
                true );
    }

    /**
     * Patches rating of Volunteer.
     *
     * @param aVolunteerId id of Volunteer to rate.
     *
     * @param aRatingRequestDto rating request dto.
     *
     * @return
     *         - Response Entity with code 200 if everything went as expected.
     *         - Response Entity with code 400 if Volunteer presence wasn't confirmed,
     *                  if rating was out of bounds or if Volunteer was already rated.
     *         - Response Entity with code 501 when unexpected server side error occurs.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionRelatedToTheOffer( authentication.principal" +
            ", #aRatingRequestDto.offerId )" )
    @PatchMapping( "/volunteer/{volunteerId}" )
    public ResponseEntity< ? > patchVolunteerRating( @PathVariable( "volunteerId" ) final Long aVolunteerId,
                                                     @RequestBody final RatingRequestDto aRatingRequestDto )
    {
        return voluntaryRatingHandler.rateVolunteer( aVolunteerId, aRatingRequestDto.getOfferId(),
                aRatingRequestDto.getRating(), aRatingRequestDto.getRatingReason(), false );
    }

    /**
     * Patches rating of Institution.
     *
     * @param aInstitutionId id of Institution to rate.
     *
     * @param aRatingRequestDto rating request dto.
     *
     * @return
     *         - Response Entity with code 200 if everything went as expected.
     *         - Response Entity with code 400 if Volunteer presence wasn't confirmed,
     *                  if rating was out of bounds or if Institution was already rated.
     *         - Response Entity with code 501 when unexpected server side error occurs.
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteerWhichHasAcceptedApplicationForGivenOffer( " +
            "authentication.principal, #aRatingRequestDto.offerId )" )
    @PatchMapping( "/institution/{institutionId}" )
    public ResponseEntity< ? > patchInstitutionRating( @PathVariable( "institutionId" ) final Long aInstitutionId,
                                                       @RequestBody final RatingRequestDto aRatingRequestDto )
    {
        return voluntaryRatingHandler.rateInstitution( aRatingRequestDto.getVolunteerId(), aInstitutionId,
                aRatingRequestDto.getOfferId(), aRatingRequestDto.getRating(), aRatingRequestDto.getRatingReason(),
                false );
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
    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )" )
    @GetMapping( "/volunteer-average-rating/{volunteerId}" )
    public ResponseEntity< ? > resolveVolunteerRatingData( @PathVariable( "volunteerId" ) final Long aVolunteerId )
    {
        return voluntaryRatingHandler.resolveVolunteerRatingData( aVolunteerId );
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
    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )" )
    @GetMapping( "/institution-average-rating/{institutionId}" )
    public ResponseEntity< ? > resolveInstitutionRatingData( @PathVariable( "institutionId" ) final Long aInstitutionId )
    {
        return voluntaryRatingHandler.resolveInstitutionRatingData( aInstitutionId );
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
    @GetMapping( "/volunteer/{volunteerId}/offers" )
    public ResponseEntity< ? > resolveOffersLeftToRateForVolunteer(
            @PathVariable( "volunteerId" ) final Long aVolunteerId )
    {
        return voluntaryRatingHandler.resolveOffersLeftToRateForVolunteer( aVolunteerId );
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
    @GetMapping( "/institution/{institutionId}/offers-for/{volunteerId}" )
    public ResponseEntity< ? > resolveOffersLeftToRateForInstitutionForGivenVolunteer(
            @PathVariable( "institutionId" ) final Long aInstitutionId,
            @PathVariable( "volunteerId" ) final Long aVolunteerId )
    {
        return voluntaryRatingHandler.resolveOffersLeftToRateForInstitutionForGivenVolunteer( aInstitutionId, aVolunteerId );
    }
}
