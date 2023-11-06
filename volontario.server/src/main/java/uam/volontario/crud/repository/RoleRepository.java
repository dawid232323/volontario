package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import uam.volontario.model.common.impl.Role;

import java.util.List;

/**
 * Repository for {@linkplain uam.volontario.model.common.impl.Role} entity.
 */
public interface RoleRepository extends CrudRepository< Role, Long >
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
     * Looks for Roles with ids present in argument list.
     *
     * @param aIds role ids.
     *
     * @return roles with ids present in argument list.
     */
    List< Role > findByIdIn( List< Long > aIds );
}
