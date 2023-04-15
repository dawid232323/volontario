package uam.volontario.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uam.volontario.crud.service.ExperienceLevelService;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.volunteer.impl.ExperienceLevel;
import uam.volontario.security.jwt.JWTService;

// TODO: this controller needs to be rethought.
/**
 * Controller for fetching data.
 */
@RestController
@RequestMapping( value = "/api",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class FetchingController
{
    private final DtoService dtoService;

    private final InterestCategoryService interestCategoryService;

    private final ExperienceLevelService experienceLevelService;

    private final JWTService jwtService;

    /**
     * CDI constructor.
     *
     * @param aDtoService dto service.
     *
     * @param aInterestCategoryService interest category service.
     *
     * @param aExperienceLevelService volunteer experience service.
     *
     * @param aJwtService jwt service.
     */
    public FetchingController( final DtoService aDtoService, final InterestCategoryService aInterestCategoryService,
                              final ExperienceLevelService aExperienceLevelService,
                              final JWTService aJwtService )
    {
        dtoService = aDtoService;
        interestCategoryService = aInterestCategoryService;
        experienceLevelService = aExperienceLevelService;
        jwtService = aJwtService;
    }

    private static final Logger LOGGER = LogManager.getLogger( FetchingController.class );

    /**
     * Loads all {@linkplain uam.volontario.model.volunteer.impl.InterestCategory} in the system.
     *
     * @return Response Entity with 200 code with List of Interest Categories or Response Entity with 500 code when
     *         error on server side occurred.
     */
    @GetMapping( value = "/interestCategories" )
    public ResponseEntity< ? > loadInterestCategories()
    {
        try
        {
            return ResponseEntity.ok( interestCategoryService.loadAllEntities().stream()
                    .map( dtoService::interestCategoryToDto )
                    .toList() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on loading interest categories: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all {@linkplain ExperienceLevel}s in the system.
     *
     * @return Response Entity with 200 code with List of Volunteer Experiences or Response Entity with 500 code when
     *         error on server side occurred.
     */
    @GetMapping( value = "/experienceLevels" )
    public ResponseEntity< ? > loadExperienceLevels()
    {
        try
        {
            return ResponseEntity.ok( experienceLevelService.loadAllEntities().stream()
                    .map( dtoService::volunteerExperienceToDto )
                    .toList() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Error on loading experience levels: {}", aE.getMessage() );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads {@linkplain uam.volontario.model.common.impl.User}'s data from request's authorization header.
     *
     * @return Response Entity with 200 code with data of User or Response Entity with 500 code when
     *         error on server side occurred.
     */
    @GetMapping( value = "/userData" )
    public ResponseEntity< ? > loadUserData( @RequestHeader( HttpHeaders.AUTHORIZATION ) String aAuthenticationHeader )
    {
        try
        {
            final String jwt = jwtService.getTokenFromAuthorizationHeader( aAuthenticationHeader );

            return ResponseEntity.ok( jwtService.readUserFromJWT( jwt )
                    .orElseThrow() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
