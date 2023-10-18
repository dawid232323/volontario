package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uam.volontario.handler.ProfilePictureHandler;
import uam.volontario.handler.UserHandler;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(
        value = "/api/user",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController
{

    private final UserHandler userHandler;
    private final ProfilePictureHandler profilePictureHandler;

    @Autowired
    public UserController( final UserHandler aUserHandler, final ProfilePictureHandler aProfilePictureHandler )
    {
        this.userHandler = aUserHandler;
        this.profilePictureHandler = aProfilePictureHandler;
    }

    /**
     * Returns paged result of basic user data required for administrators optionally filtered.
     *
     * @param aName user's first name
     *
     * @param aLastName user's last name
     *
     * @param aEmail user's email
     *
     * @param aRoleIds ids of users' roles
     *
     * @param aPageable {@linkplain Pageable}
     *
     * @return response with {@linkplain uam.volontario.dto.user.AdministrativeUserDetailsDto} with status 200
     */
    @GetMapping
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    public ResponseEntity< ? > getFilteredUsersData(
            @RequestParam( value = "name", required = false ) final String aName,
            @RequestParam( value = "lastName", required = false ) final String aLastName,
            @RequestParam( value = "email", required = false ) final String aEmail,
            @RequestParam( value = "roleIds", required = false ) final List< Long > aRoleIds,
            final Pageable aPageable
            )
    {
        return ResponseEntity.ok( this.userHandler
                .getFilteredUsers( aRoleIds, aName, aLastName, aEmail, aPageable ) );
    }

    /**
     * Used to ban or unban single user, available only for administrators.
     * For security and common sense reasons this endpoint allows only for single user modification
     *
     * @param aBooleanMap Map that should contain only single value with key "isDeactivated"
     *                    and boolean value that corresponds to the given key
     *
     * @return response with status 200 when user status is changed correctly,
     * status 400 when user with given id cannot be found
     * or status 500 when something goes wrong
     */
    @PatchMapping( "/change-status/{user_id}" )
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    public ResponseEntity< ? > changeUserActiveStatus(
            @PathVariable( "user_id" ) final Long aUserId,
            @RequestBody final Map< String, Boolean > aBooleanMap )
    {
        return this.userHandler.changeUserStatus( aUserId,
                aBooleanMap.getOrDefault( "isDeactivated", false ) );
    }

    /**
     * Used to assign user to roles, or remove some role from the user.
     *
     * @param aRoleIds list of {@linkplain Map} with single key "roleId"
     *                 and {@linkplain Long} value of desired role id
     *
     * @return response with status 200 when user status is changed correctly,
     * status 400 when user with given id cannot be found
     * or status 500 when something goes wrong
     */
    @PatchMapping( "/change-roles/{user_id}" )
    @PreAuthorize( "@permissionEvaluator.allowForAdministration( authentication.principal )" )
    public ResponseEntity< ? > assignUserRoles(
            @PathVariable( "user_id" ) final Long aUserId,
            @RequestBody final List< Map< String, Long > > aRoleIds )
    {
        return this.userHandler.assignUserRoles( aUserId, aRoleIds );
    }

    /**
     * Returns data necessary to display user profile details.
     *
     * @param aUserId id of user to retrieve
     *
     * @return Response entity with profile data body or error status code.
     */
    @GetMapping( "/profile/{user_id}" )
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    public ResponseEntity< ? > getUserProfileDetails( @PathVariable( "user_id" ) final Long aUserId )
    {
        return this.userHandler.getUserProfileDetails( aUserId );
    }

    /**
     * Saves profile picture for selected user.
     *
     * @param aUserId id of user
     *
     * @param aMultipartFile content of a user profile picture
     *
     * @return response entity that contains json body with new file name
     */
    @PostMapping(value = "/profile/{user_id}/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map< String, String> saveUserProfilePicture( @RequestParam( "picture" ) MultipartFile aMultipartFile,
                                                        @PathVariable( "user_id" ) final Long aUserId )
    {
        return this.profilePictureHandler.saveUserProfilePicture( aUserId, aMultipartFile );
    }

    /**
     * Retrieves image for selected user.
     *
     * @param aUserId id of user that image needs to be retrieved
     *
     * @return response entity with empty body if user does not have any photo assigned, or
     *          response entity with photo content
     */
    @GetMapping(value = "/profile/{user_id}/picture", consumes = MediaType.ALL_VALUE)
    public ResponseEntity< ? > getUserProfilePicture( @PathVariable( "user_id" ) final Long aUserId )
    {
        return this.profilePictureHandler.getUserProfilePicture( aUserId );
    }
}
