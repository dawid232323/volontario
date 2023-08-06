package uam.volontario.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.RoleService;
import uam.volontario.crud.service.UserService;
import uam.volontario.crud.specification.UserSpecification;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.dto.user.AdministrativeUserDetailsDto;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.common.impl.UserSearchQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service responsible with all {@link uam.volontario.model.common.impl.User} operations except creating volunteer
 * and updating volunteer basic info
 */
@Service
public class UserHandler
{
    private final UserService userService;
    private final DtoService dtoService;
    private final RoleService roleService;

    @Autowired
    public UserHandler( final UserService aUserService, final DtoService aDtoService,
                        final RoleService aRoleService )
    {
        this.userService = aUserService;
        this.dtoService = aDtoService;
        this.roleService = aRoleService;
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
     * @return list of {@linkplain uam.volontario.dto.user.AdministrativeUserDetailsDto}
     */
    public Page< AdministrativeUserDetailsDto > getFilteredUsers( final List< Long > aRoleIds, final String aName,
                                                                  final String aLastName, final String aEmail, final Pageable aPageable )
    {
        final UserSearchQuery userSearchQuery = this.getUserSearchQuery( aRoleIds, aName, aLastName, aEmail );
        final UserSpecification userSpecification = new UserSpecification( userSearchQuery );

        final Pageable pageable = PageRequest
                .of( aPageable.getPageNumber(), aPageable.getPageSize(), Sort.by( "firstName", "lastName" ) );
        final Page< User > filteredUsers = this.userService.findFiltered( userSpecification, pageable );
        return filteredUsers.map( dtoService::getAdmUserDetailsDtoFromUser );
    }

    /**
     * Activates or deactivates single user.
     *
     * @param aUserId id of user to be activated or deactivated.
     *
     * @param isDeactivated boolean value that determines if user should be deactivated.
     *
     * @return response with status 200 and {@linkplain AdministrativeUserDetailsDto} when user status is changed correctly,
     * status 400 when user with given id cannot be found
     * or status 500 when something goes wrong
     */
    public ResponseEntity< ? > changeUserStatus( final Long aUserId, final Boolean isDeactivated )
    {
        final User user;
        try
        {
             user = this.getUserWithGivenId( aUserId );
        } catch ( EntityNotFoundException aE )
        {
            return ResponseEntity.status( HttpStatus.BAD_REQUEST )
                    .build();
        }
        user.setVerified( !isDeactivated );
        this.userService.saveOrUpdate( user );
        final AdministrativeUserDetailsDto userDetailsDto = this.dtoService
                .getAdmUserDetailsDtoFromUser( user );
        return ResponseEntity.status( HttpStatus.OK )
                .body( userDetailsDto );
    }

    /**
     * Assigns user to a role ro removes user from the role.
     *
     * @param aUserId id of user to be changed.
     *
     * @param aUserRoles list of role ids that should be assigned to the user
     *
     * @return response with status 200 and {@linkplain AdministrativeUserDetailsDto} when roles are assigned correctly,
     * status 400 when user with given id cannot be found
     * or status 500 when something goes wrong
     */
    public ResponseEntity< ? > assignUserRoles( final Long aUserId, final List< Map< String, Long > > aUserRoles )
    {
        final User user;
        try
        {
            user = this.getUserWithGivenId( aUserId );
        } catch ( EntityNotFoundException aE )
        {
            return ResponseEntity.status( HttpStatus.BAD_REQUEST )
                    .build();
        }
        final List< Long > roleIds = aUserRoles.stream()
                .map( roleMap -> roleMap.get( "roleId" ) ).toList();
        final List< Role > roles = this.roleService.findByIdIn( roleIds );
        user.setRoles( roles );
        this.userService.saveOrUpdate( user );
        final AdministrativeUserDetailsDto userDetailsDto = this.dtoService
                .getAdmUserDetailsDtoFromUser( user );
        return ResponseEntity.status( HttpStatus.OK )
                .body( userDetailsDto );
    }

    private UserSearchQuery getUserSearchQuery( final List< Long > aRoleIds, final String aName,
                                                final String aLastName, final String aEmail )
    {
        return UserSearchQuery.builder()
                .roleIds( aRoleIds )
                .firstName( aName )
                .lastName( aLastName )
                .email( aEmail )
                .build();
    }

    private User getUserWithGivenId( final Long aUserId ) throws EntityNotFoundException
    {
        final Optional< User > userOptional =  this.userService.tryToFindById( aUserId );
        if( userOptional.isEmpty() )
        {
            throw new EntityNotFoundException();
        }
        return userOptional.get();
    }
}
