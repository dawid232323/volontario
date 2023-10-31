package uam.volontario.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uam.volontario.model.common.impl.Role;

import java.util.List;
import java.util.function.Function;

/**
 * Definition of role in the system. All the {@linkplain RoleIf} instances should be mapped to their
 * corresponding UserRole instances as they are easier to work with.
 */
@AllArgsConstructor
@Getter
public enum UserRole
{
    ADMIN( "Administrator(ka)" ),

    MOD( "Moderator(ka)" ),

    VOLUNTEER( "Wolontariusz(ka)" ),

    INSTITUTION_EMPLOYEE( "Pracownik/czka Instytucji" ),

    INSTITUTION_ADMIN( "Administrator(ka) Instytucji" );

    private final String translatedRoleName;

    /**
     * Maps entities of {@linkplain Role} type to more flexible UserRole instances.
     *
     * @param aRoles set of role entities.
     *
     * @return set of user roles.
     */
    public static List< UserRole > mapRolesToUserRoles( final List< Role > aRoles )
    {
        return aRoles.stream()
                .map( roleToUserRoleMapper() )
                .toList();
    }

    /**
     * Maps UserRoles to names of {@linkplain Role} entities.
     *
     * @param aUserRoles set of user roles.
     *
     * @return set of names of roles entities.
     */
    public static List< String > mapUserRolesToRoleNames( final List< UserRole > aUserRoles )
    {
        return aUserRoles.stream()
                .map( UserRole::getTranslatedRoleName )
                .toList();
    }

    private static Function< Role, UserRole > roleToUserRoleMapper()
    {
        return role -> {
            for( final UserRole userRole : UserRole.values() )
            {
                if( role.getName().equals( userRole.getTranslatedRoleName() ) )
                {
                    return userRole;
                }
            }
            throw new IllegalArgumentException( role.getName() + " is not a defined role in the system." );
        };
    }
}
