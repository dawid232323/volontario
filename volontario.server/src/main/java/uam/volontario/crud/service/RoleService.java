package uam.volontario.crud.service;

import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;

import java.util.List;

/**
 * Injection interface for {@linkplain User}'s service.
 */
public interface RoleService extends EntityService< Role >
{
    /**
     * Looks for Roles with one of given names.
     *
     * @param aNames role names.
     *
     * @return roles with the one of the names from provided list.
     */
    List< Role > findByNameIn( List< String > aNames );

    /**
     * Looks for Role with given name.
     *
     * @param aName name of role.
     *
     * @return role with given name.
     */
    Role findByName( String aName );

    /**
     * Looks for Roles with ids from given list.
     *
     * @param aIds role ids.
     *
     * @return roles with ids that are present in argument list.
     */
    List< Role > findByIdIn( List< Long > aIds );
}
