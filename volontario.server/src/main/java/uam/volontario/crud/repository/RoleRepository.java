package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import uam.volontario.model.common.impl.Role;

import java.util.List;
import java.util.Optional;

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
}
