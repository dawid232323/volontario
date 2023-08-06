package uam.volontario.crud.service;

import uam.volontario.model.configuration.ConfigurationEntry;

import java.util.Optional;

/**
 * Injection interface for {@linkplain uam.volontario.model.configuration.ConfigurationEntry}'s service.
 */
public interface ConfigurationEntryService extends EntityService< ConfigurationEntry >
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
