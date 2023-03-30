package uam.volontario.rest.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.crud.service.UserService;
import uam.volontario.crud.service.VolunteerExperienceService;
import uam.volontario.dto.convert.DtoConverter;
import uam.volontario.security.jwt.JWTService;

/**
 * Controller for fetching data.
 */
@RestController
@RequestMapping( value = "/api",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE )
public class DataController
{
    @Autowired
    private DtoConverter dtoConverter;

    @Autowired
    private InterestCategoryService interestCategoryService;
    @Autowired
    private VolunteerExperienceService volunteerExperienceService;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LogManager.getLogger( DataController.class );

    @GetMapping( value = "/interestCategories" )
    public ResponseEntity< ? > loadInterestCategories()
    {
        try
        {
            return ResponseEntity.ok( interestCategoryService.loadAllEntities().stream()
                    .map( dtoConverter::interestCategoryToDto )
                    .toList() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Exception occured when loading interest categories: {}", aE.getMessage(), aE );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    @GetMapping( value = "/experienceLevels" )
    public ResponseEntity< ? > loadExperienceLevels()
    {
        try
        {
            return ResponseEntity.ok( volunteerExperienceService.loadAllEntities().stream()
                    .map( dtoConverter::volunteerExperienceToDto )
                    .toList() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

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
