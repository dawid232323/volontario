package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.ConfigurationEntryRepository;
import uam.volontario.crud.service.ConfigurationEntryService;
import uam.volontario.model.configuration.ConfigurationEntry;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain ConfigurationEntryService}.
 */
@Service
public class ConfigurationEntryServiceImpl implements ConfigurationEntryService
{
    private final ConfigurationEntryRepository configurationEntryRepository;

    /**
     * CDI constructor.
     *
     * @param aConfigurationEntryRepository configuration entry repository.
     */
    public ConfigurationEntryServiceImpl( final ConfigurationEntryRepository aConfigurationEntryRepository )
    {
        configurationEntryRepository = aConfigurationEntryRepository;
    }

    @Override
    public Optional< ConfigurationEntry > findByKey( final String aKey )
    {
        return configurationEntryRepository.findByKey( aKey );
    }

    @Override
    public ConfigurationEntry loadEntity( final Long aConfigurationEntryId ) throws NoResultException
    {
        return tryLoadEntity( aConfigurationEntryId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< ConfigurationEntry > tryLoadEntity( final Long aConfigurationEntryId )
    {
        return configurationEntryRepository.findById( aConfigurationEntryId );
    }

    @Override
    public List< ConfigurationEntry > loadAllEntities()
    {
        return Lists.newArrayList( configurationEntryRepository.findAll() );
    }

    @Override
    public ConfigurationEntry saveOrUpdate( final ConfigurationEntry aConfigurationEntry )
    {
        return configurationEntryRepository.save( aConfigurationEntry );
    }

    @Override
    public void deleteEntity( final Long aConfigurationEntryId )
    {
        configurationEntryRepository.deleteById( aConfigurationEntryId );
    }
}
