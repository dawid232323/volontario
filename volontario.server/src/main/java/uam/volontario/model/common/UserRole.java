package uam.volontario.model.common;

import uam.volontario.model.common.impl.Role;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Definition of role in the system. All the {@linkplain RoleIf} instances should be mapped to their
 * corresponding UserRole instances as they are easier to work with.
 */
public enum UserRole
{
    ADMIN,

    MOD,

    VOLUNTEER,

    INSTITUTION_EMPLOYEE,

    INSTITUTION_ADMIN;

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
                .map( userRoleToRoleNameMapper() )
                .toList();
    }

    private static Function< Role, UserRole > roleToUserRoleMapper()
    {
        return role -> switch ( role.getName() )
        {
            case "Pracownik Instytucji" -> INSTITUTION_EMPLOYEE;
            case "Administrator Instytucji" -> INSTITUTION_ADMIN;
            case "Wolontariusz" -> VOLUNTEER;
            case "Administrator" -> ADMIN;
            case "Moderator" -> MOD;

            default -> throw new IllegalArgumentException( role.getName() + " is not a defined role in the system." );
        };
    }

    private static Function< UserRole, String > userRoleToRoleNameMapper()
    {
        return userRole -> switch ( userRole )
        {
            case INSTITUTION_EMPLOYEE -> "Pracownik Instytucji";
            case INSTITUTION_ADMIN -> "Administrator Instytucji";
            case VOLUNTEER -> "Wolontariusz";
            case ADMIN -> "Administrator";
            case MOD -> "Moderator";
        };
    }
}
