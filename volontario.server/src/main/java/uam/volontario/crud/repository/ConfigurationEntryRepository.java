package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.configuration.ConfigurationEntry;

import java.util.Optional;

/**
 * Repository for {@linkplain ConfigurationEntry} entity.
 */
@Repository
public interface ConfigurationEntryRepository extends CrudRepository< ConfigurationEntry, Long >
{
    /**
     * Looks for Configuration Entry with given key.
     *
     * @param aKey key.
     *
     * @return Configuration Entry with given key or empty Optional if not found.
     */
    Optional< ConfigurationEntry > findByKey( String aKey );
}
