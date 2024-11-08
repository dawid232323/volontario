package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.Application.ApplicationDto;
import uam.volontario.dto.presence.VoluntaryPresenceVolunteerDataDto;
import uam.volontario.handler.ApplicationProcessingHandler;
import uam.volontario.handler.OfferPresenceHandler;
import uam.volontario.model.offer.impl.ApplicationStateEnum;
import uam.volontario.model.offer.impl.VoluntaryPresence;

import java.util.Map;
import java.util.Optional;

/**
 * Controller for API related to {@linkplain uam.volontario.model.offer.impl.Application}s.
 */
@RestController
@RequestMapping( value = "/api/application",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class ApplicationController
{
    private final ApplicationProcessingHandler applicationProcessingHandler;

    private final OfferPresenceHandler offerPresenceHandler;

    /**
     * CDI constructor.
     *
     * @param aApplicationProcessingHandler application processing handler.
     *
     * @param aOfferPresenceHandler offer presence handler.
     */
    @Autowired
    public ApplicationController( final ApplicationProcessingHandler aApplicationProcessingHandler,
                                  final OfferPresenceHandler aOfferPresenceHandler )
    {
        applicationProcessingHandler = aApplicationProcessingHandler;
        offerPresenceHandler = aOfferPresenceHandler;

    }

    /**
     * Stars Application.
     *
     * @param aApplicationId id of Application to star.
     *
     *
     * @return
     *          - Response Entity with starred Application and code 200, if update was successful.
     *          - Response Entity with code 400, if there was no Application with given id.
     *          - Response Entity with code 501, if there was an unexpected server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitution( authentication.principal )" )
    @PatchMapping( "/star/{applicationId}" )
    public ResponseEntity< ? > starApplication( @PathVariable( "applicationId" ) final Long aApplicationId )
    {
        return applicationProcessingHandler.updateStarOfApplication( aApplicationId, true );
    }

    /**
     * Unstars Application.
     *
     * @param aApplicationId id of Application to unstar.
     *
     *
     * @return
     *          - Response Entity with unstarred Application and code 200, if update was successful.
     *          - Response Entity with code 400, if there was no Application with given id.
     *          - Response Entity with code 501, if there was an unexpected server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitution( authentication.principal )" )
    @PatchMapping( "/unstar/{applicationId}" )
    public ResponseEntity< ? > unstarApplication( @PathVariable( "applicationId" ) final Long aApplicationId )
    {
        return applicationProcessingHandler.updateStarOfApplication( aApplicationId, false );
    }

    /**
     * Creates Application instance.
     *
     * @param aDto dto with data to create Application.
     *
     * @return
     *        - Response Entity with code 201 and newly created Application, if everything went as expected.
     *        - Response Entity with code 400 when User and/or Offer could not be found based on DTO's data, or when
     *          newly created Application did not pass validation.
     *        - Response Entity with code 500, if there was an unexpected server-side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteers( authentication.principal )" )
    @PostMapping
    public ResponseEntity< ? > createApplication( @RequestBody final ApplicationDto aDto )
    {
        return applicationProcessingHandler.createApplication( aDto );
    }

    /**
     * Sets Application's state to {@linkplain ApplicationStateEnum#UNDER_RECRUITMENT} and informs Volunteer about it
     * via contact email.
     *
     * @param aApplicationId id of Application to accept.
     *
     *
     * @return
     *          - Response Entity with accepted Application and 200 code, if everything went well.
     *          - Response Entity with code 400, if there is no Application with given id.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionRelatedToTheApplication( authentication.principal, #aApplicationId )" )
    @PatchMapping( "/accept/{applicationId}" )
    public ResponseEntity< ? > acceptApplication( @PathVariable( "applicationId" ) final Long aApplicationId )
    {
        return applicationProcessingHandler.resolveApplication( aApplicationId, ApplicationStateEnum.UNDER_RECRUITMENT, Optional.empty() );
    }

    /**
     * Sets Application's state to {@linkplain ApplicationStateEnum#RESERVE_LIST} and informs Volunteer about it
     * via contact email.
     *
     * @param aApplicationId id of Application to add to reserve list.
     *
     *
     * @return
     *          - Response Entity with Application added to reserve list and 200 code, if everything went well.
     *          - Response Entity with code 400, if there is no Application with given id.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionRelatedToTheApplication( authentication.principal, #aApplicationId )" )
    @PatchMapping( "/reserve-list/{applicationId}" )
    public ResponseEntity< ? > addApplicationToReserveList( @PathVariable( "applicationId" ) final Long aApplicationId )
    {
        return applicationProcessingHandler.resolveApplication( aApplicationId, ApplicationStateEnum.RESERVE_LIST, Optional.empty() );
    }

    /**
     * Sets Application's state to {@linkplain ApplicationStateEnum#DECLINED} and informs Volunteer about it
     * via contact email.
     *
     * @param aApplicationId id of Application to decline.
     *
     * @param aDeclineReasonMap map that should contain decisionReason key
     *                          with reason why given application was declined
     *
     * @return
     *          - Response Entity with declined Application and 200 code, if everything went well.
     *          - Response Entity with code 400, if there is no Application with given id.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionRelatedToTheApplication( authentication.principal, #aApplicationId )" )
    @PatchMapping( "/decline/{applicationId}" )
    public ResponseEntity< ? > declineApplication( @PathVariable( "applicationId" ) final Long aApplicationId,
                                                   @RequestBody final Map<String, String> aDeclineReasonMap )
    {
        return applicationProcessingHandler.resolveApplication( aApplicationId, ApplicationStateEnum.DECLINED,
                Optional.ofNullable( aDeclineReasonMap ) );
    }

    /**
     * Loads application details from the system, filtered by passed criteria.
     *
     * @return Response Entity with code 200 and list of detailed application info or Response Entity with code 500 when error
     *         occurred during fetching application.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitution( authentication.principal )" )
    @GetMapping( "/searchDetails" )
    public ResponseEntity< ? > loadApplicationInfoFilteredDetails( @RequestParam( required = false ) String state,
                                                            @RequestParam( required = false ) Boolean isStarred,
                                                            @RequestParam( required = false ) Long offerId,
                                                            @RequestParam( required = false ) Long volunteerId,
                                                            @RequestParam( required = false ) Long institutionId,
                                                            Pageable aPageable
    )
    {
        return applicationProcessingHandler.loadApplicationInfoFiltered( state, isStarred, offerId, volunteerId,
                institutionId, aPageable, true );
    }

    /**
     * Loads application details from the system by id.
     *
     * @return Response Entity with code 200 and detailed application info or Response Entity with code 400,
     * if there is no Application with given id.
     */
    @PreAuthorize( "@permissionEvaluator.allowForInstitutionRelatedToTheApplication( authentication.principal, #aApplicationId )" )
    @GetMapping( "/details/{applicationId}" )
    public ResponseEntity< ? > loadApplicationInfoFilteredDetails( @PathVariable( "applicationId" ) final Long aApplicationId )
    {
        return applicationProcessingHandler.loadApplicationDetails( aApplicationId );
    }

    /**
     * Loads application base info from the system, filtered by passed criteria.
     *
     * @return Response Entity with code 200 and list of base application info or Response Entity with code 500 when error
     *         occurred during fetching offers.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )" )
    @GetMapping( "/search" )
    public ResponseEntity< ? > loadApplicationInfoFiltered( @RequestParam( required = false ) String state,
                                                            @RequestParam( required = false ) Boolean isStarred,
                                                            @RequestParam( required = false ) Long offerId,
                                                            @RequestParam( required = false ) Long volunteerId,
                                                            @RequestParam( required = false ) Long institutionId,
                                                            Pageable aPageable
    )
    {
        return applicationProcessingHandler.loadApplicationInfoFiltered( state, isStarred, offerId, volunteerId,
                institutionId, aPageable, false );
    }

    /**
     * Find if a given volunteer applied for an offer and if he did, what is the application status.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )" )
    @GetMapping( "/checkState" )
    public ResponseEntity< ? > checkState( @RequestParam Long offerId, @RequestParam Long volunteerId )
    {
        return applicationProcessingHandler.checkState( offerId, volunteerId );
    }

    /**
     * Checks whether Offer is ready to confirm Presences.
     *
     * @param aOfferId id of Offer.
     *
     * @return
     *        - Response Entity with code 200 and true/false which depends on whether presence can be confirmed.
     *        - Response Entity with code 401 when passed id does not match any existing Offer.
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteerWhichHasAcceptedApplicationForGivenOffer( authentication.principal," +
            " #aOfferId) " )
    @GetMapping( "/is-presence-available/{offerId}" )
    public ResponseEntity< ? > isPresenceAvailable( @PathVariable( "offerId" ) final Long aOfferId )
    {
        return offerPresenceHandler.isOfferReadyToConfirmPresences( aOfferId );
    }

    /**
     * Loads state of {@linkplain VoluntaryPresence}.
     *
     * @param aOfferId offer id.
     *
     * @return
     *        - Response Entity with code 200 and state as {@linkplain VoluntaryPresenceVolunteerDataDto}.
     *        - Response Entity with code 400 if Application for given Offer does not exist, or it was not accepted,
     *          or User of given ID does not exist or is not a Volunteer.
     *        - Response Entity with code 501 in case of unexpected server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteerWhichHasAcceptedApplicationForGivenOffer( authentication.principal," +
            " #aOfferId) " )
    @GetMapping( "/load-voluntary-presence/{volunteerId}/{offerId}" )
    public ResponseEntity< ? > loadVoluntaryPresenceState( @PathVariable( "volunteerId" ) final Long aVolunteerId,
                                                           @PathVariable( "offerId" ) final Long aOfferId )
    {
        return offerPresenceHandler.loadVoluntaryPresenceStateOfVolunteer( aVolunteerId, aOfferId );
    }
}
