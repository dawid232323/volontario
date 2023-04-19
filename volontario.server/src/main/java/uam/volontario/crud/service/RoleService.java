package uam.volontario.crud.service;

import jakarta.persistence.NoResultException;
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
}
