package uam.volontario.rest;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.handler.InterestCategoryHandler;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.security.jwt.JWTService;

import java.util.List;
import java.util.Optional;

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

    private final JWTService jwtService;

    private final InterestCategoryHandler interestCategoryHandler;

    /**
     * CDI constructor.
     *
     * @param aDtoService dto service.
     *
     * @param aInterestCategoryHandler interest category handler.
     *
     * @param aJwtService jwt service.
     */
    public FetchingController( final DtoService aDtoService, final InterestCategoryHandler aInterestCategoryHandler,
                               final JWTService aJwtService )
    {
        dtoService = aDtoService;
        interestCategoryHandler = aInterestCategoryHandler;
        jwtService = aJwtService;
    }

    private static final Logger LOGGER = LogManager.getLogger( FetchingController.class );

    /**
     * Loads all {@linkplain uam.volontario.model.volunteer.impl.InterestCategory} in the system.
     *
     * @return Response Entity with 200 code with List of Interest Categories or Response Entity with 500 code when
     *         error on server side occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    @GetMapping( value = "/interestCategories" )
    public ResponseEntity< ? > loadInterestCategories()
    {
        try
        {
            final List< InterestCategory > interestCategories = Lists.newArrayList();
            Optional.ofNullable( interestCategoryHandler.loadAllUsedInterestCategories().getBody() )
                    .filter( List.class::isInstance )
                    .map( List.class::cast )
                    .ifPresent( interestCategories::addAll );

            return ResponseEntity.ok( interestCategories.stream()
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
     * Loads {@linkplain uam.volontario.model.common.impl.User}'s data from request's authorization header.
     *
     * @return Response Entity with 200 code with data of User or Response Entity with 500 code when
     *         error on server side occurred.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryUser( authentication.principal )" )
    @GetMapping( value = "/userData" )
    public ResponseEntity< ? > loadUserData( @RequestHeader( HttpHeaders.AUTHORIZATION ) final String aAuthenticationHeader )
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
