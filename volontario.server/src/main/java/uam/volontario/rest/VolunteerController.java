package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.dto.VolunteerPatchInfoDto;
import uam.volontario.exception.user.RoleMismatchException;
import uam.volontario.handler.VolunteerHandler;
import uam.volontario.model.offer.impl.VoluntaryPresenceStateEnum;

import java.util.Map;

/**
 * Controller for API related to {@linkplain uam.volontario.model.common.impl.User}s
 * of type {@linkplain uam.volontario.model.common.UserRole#VOLUNTEER}.
 */
@RestController
@RequestMapping( value = "/api/volunteer",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class VolunteerController
{
    private final VolunteerHandler volunteerHandler;

    /**
     * CDI constructor.
     *
     * @param aVolunteerHandler volunteer handler.
     */
    @Autowired
    public VolunteerController( final VolunteerHandler aVolunteerHandler )
    {
        volunteerHandler = aVolunteerHandler;
    }

    /**
     * Registers volunteer.
     *
     * @param aDto dto containing registration data.
     *
     * @return if volunteer passes validation, then ResponseEntity with 201 status and volunteer. If volunteer did not
     *         pass validation then ResponseEntity with 400 status and constraints violated. If there was an error,
     *         then ResponseEntity with 500 status and error message.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    @PostMapping( value = "/register" )
    public ResponseEntity< ? > registerVolunteer( @RequestBody final VolunteerDto aDto )
    {
        return volunteerHandler.registerVolunteer( aDto );
    }

    /**
     * Updates Volunteer's contact data, experience level, interest categories and participation motivation. If
     * some of mentioned data is not provided in dto then update on those properties is not performed.
     *
     * @param aVolunteerId volunteer id.
     *
     * @param aPatchDto volunteer patch info dto.
     *
     * @return
     *        - Response Entity with code 200 and patched Volunteer if everything went as expected.
     *        - Response Entity with code 400 if:
     *             1. There is no Volunteer with provided id found.
     *             2. Provided id belongs to User who is not Volunteer.
     *             3. Volunteer does not pass validation after patch (in this case also validation violations
     *                                                                are provided within the Response)
     *
     *        - Response Entity with code 500 when unexpected server-side error occurs.
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteers( authentication.principal )" )
    @PatchMapping( "/{volunteerId}" )
    public ResponseEntity< ? > updateVolunteerInformation( @PathVariable( "volunteerId" ) final Long aVolunteerId,
                                                           @RequestBody final VolunteerPatchInfoDto aPatchDto )
    {
        return volunteerHandler.updateVolunteerInformation( aVolunteerId, aPatchDto );
    }

    /**
     * Endpoint that updates volunteer interest data.
     *
     * @param aVolunteerId primary key of volunteer to be updated
     *
     * @param aInterestsBody json body where key <strong>interests</strong> contains value interests description value
     *
     * @return response entity with status 200 if everything is saved correctly
     *
     * @throws RoleMismatchException when user with given id
     *                              does not have {@linkplain uam.volontario.model.common.UserRole#VOLUNTEER} role
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteers( authentication.principal )" )
    @PatchMapping( "/{volunteerId}/interests" )
    public ResponseEntity< ? > updateVolunteerInterests( @PathVariable( "volunteerId" ) final Long aVolunteerId,
                                                         @RequestBody final Map<String, String> aInterestsBody )
    {
        return this.volunteerHandler
                .patchVolunteerInterests( aVolunteerId, aInterestsBody.get( "interests" ) );
    }

    /**
     * Endpoint that updates volunteer experience description data.
     *
     * @param aVolunteerId primary key of volunteer to be updated
     *
     * @param aInterestsBody json body where key <strong>experienceDescription</strong> contains value experience description value
     *
     * @return response entity with status 200 if everything is saved correctly
     *
     * @throws RoleMismatchException when user with given id
     *                              does not have {@linkplain uam.volontario.model.common.UserRole#VOLUNTEER} role
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteers( authentication.principal )" )
    @PatchMapping( "/{volunteerId}/experience-description" )
    public ResponseEntity< ? > updateVolunteerExperienceDescription( @PathVariable( "volunteerId" ) final Long aVolunteerId,
                                                         @RequestBody final Map<String, String> aInterestsBody )
    {
        return this.volunteerHandler
                .patchVolunteerExperienceDescription( aVolunteerId, aInterestsBody.get( "experienceDescription" ) );
    }

    /**
     * Resolves all {@linkplain uam.volontario.model.offer.impl.Offer}s in which Volunteer of provided id participated.
     *
     * @param aVolunteerId Volunteer id.
     *
     * @return
     *        - Response Entity with code 200 and List of Offers in which Volunteer participated
     *        - Response Entity with code 401 if no user of given id was found or if user was found, but was not volunteer.
     *        - Response Entity with code 500 in case of any unexpected server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )")
    @GetMapping( "/{volunteerId}/all-presences" )
    public ResponseEntity< ? > resolveAllPresencesOfVolunteer( @PathVariable( "volunteerId" ) final Long aVolunteerId )
    {
        return volunteerHandler.resolveAllPresencesOfVolunteer( aVolunteerId );
    }

    /**
     * Changes volunteer reported presence state to CONFIRMED.
     *
     * @param aVolunteerId id of Volunteer to change volunteer reported state.
     *
     * @param aOfferId id of  Offer.
     *
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 401 if User/Offer of given id was not found or if Application for Offer was not accepted.
     *        - Response Entity with code 500 in case of any unexpected server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteerWhichHasAcceptedApplicationForGivenOffer(" +
            " authentication.principal, #aOfferId )" )
    @PostMapping( "/confirm-presence/{volunteerId}/{offerId}" )
    public ResponseEntity< ? > confirmPresence( @PathVariable( "volunteerId" ) final Long aVolunteerId,
                                                @PathVariable( "offerId" ) final Long aOfferId )
    {
        return volunteerHandler.changeVolunteerReportedPresenceState( aVolunteerId, aOfferId,
                VoluntaryPresenceStateEnum.CONFIRMED );
    }

    /**
     * Changes volunteer reported presence state to DENIED.
     *
     * @param aVolunteerId id of Volunteer to change volunteer reported state.
     *
     * @param aOfferId id of Offer.
     *
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 401 if User/Offer of given id was not found or if Application for Offer was not accepted.
     *        - Response Entity with code 500 in case of any unexpected server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteerWhichHasAcceptedApplicationForGivenOffer(" +
            " authentication.principal, #aOfferId )" )
    @PostMapping( "/deny-presence/{volunteerId}/{offerId}" )
    public ResponseEntity< ? > denyPresence( @PathVariable( "volunteerId" ) final Long aVolunteerId,
                                             @PathVariable( "offerId" ) final Long aOfferId )
    {
        return volunteerHandler.changeVolunteerReportedPresenceState( aVolunteerId, aOfferId,
                VoluntaryPresenceStateEnum.DENIED );
    }

    /**
     * Postpones Voluntary Presence Confirmation on Volunteer's side by 7 days.
     *
     * @param aVolunteerId id of Volunteer to postpone presence confirmation.
     *
     * @param aOfferId id of Offer.
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 401 if User/Offer of given id was not found or if Application for Offer was not accepted.
     *        - Response Entity with code 500 in case of an unexpected server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForVolunteerWhichHasAcceptedApplicationForGivenOffer( " +
            "authentication.principal, #aOfferId )")
    @PostMapping( "/postpone-presence-confirmation/{volunteerId}/{offerId}" )
    public ResponseEntity< ? > postponePresenceConfirmation( @PathVariable( "volunteerId" ) final Long aVolunteerId,
                                                             @PathVariable( "offerId" ) final Long aOfferId )
    {
        return volunteerHandler.postponePresenceConfirmation( aVolunteerId, aOfferId );
    }
}
