package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.ApplicationDto;
import uam.volontario.handler.ApplicationProcessingHandler;
import uam.volontario.model.offer.impl.ApplicationStateEnum;

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

    /**
     * CDI constructor.
     *
     * @param aApplicationProcessingHandler application processing handler.
     */
    @Autowired
    public ApplicationController( final ApplicationProcessingHandler aApplicationProcessingHandler )
    {
        applicationProcessingHandler = aApplicationProcessingHandler;
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
    @PostMapping
    public ResponseEntity< ? > createApplication( @RequestBody final ApplicationDto aDto )
    {
        return applicationProcessingHandler.createApplication( aDto );
    }

    /**
     * Sets Application's state to {@linkplain ApplicationStateEnum#ACCEPTED} and informs Volunteer about it
     * via contact email.
     *
     * @param aApplicationId id of Application to accept.
     *
     *
     * @return
     *          - Response Entity with accepted Application and 200 code, if everything went well.
     *          - Response Entity with code 400, if there is no Application with given id.
     */
    @PostMapping( "/accept/{applicationId}" )
    public ResponseEntity< ? > acceptApplication( @PathVariable( "applicationId" ) final Long aApplicationId )
    {
        return applicationProcessingHandler.resolveApplication( aApplicationId, ApplicationStateEnum.ACCEPTED );
    }

    /**
     * Sets Application's state to {@linkplain ApplicationStateEnum#DECLINED} and informs Volunteer about it
     * via contact email.
     *
     * @param aApplicationId id of Application to decline.
     *
     *
     * @return
     *          - Response Entity with declined Application and 200 code, if everything went well.
     *          - Response Entity with code 400, if there is no Application with given id.
     */
    @PostMapping( "/decline/{applicationId}" )
    public ResponseEntity< ? > declineApplication( @PathVariable( "applicationId" ) final Long aApplicationId )
    {
        return applicationProcessingHandler.resolveApplication( aApplicationId, ApplicationStateEnum.DECLINED );
    }
}
