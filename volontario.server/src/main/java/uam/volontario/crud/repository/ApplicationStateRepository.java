package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.offer.impl.ApplicationState;

import java.util.Optional;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.ApplicationState} entity.
 */
@Repository
public interface ApplicationStateRepository extends CrudRepository< ApplicationState, Long >
{
    /**
     * Looks for Application State with given name.
     *
     * @param aName name.
     *
     * @return Application State entity if found.
     */
    Optional< ApplicationState > findByName( String aName );
}
