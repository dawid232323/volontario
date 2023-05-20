package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.ApplicationStateRepository;
import uam.volontario.crud.service.ApplicationStateService;
import uam.volontario.model.offer.impl.ApplicationState;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain ApplicationStateService}.
 */
@Service
public class ApplicationStateServiceImpl implements ApplicationStateService
{
    private final ApplicationStateRepository applicationStateRepository;

    /**
     * CDI constructor.
     *
     * @param aApplicationStateRepository application state repository.
     */
    @Autowired
    public ApplicationStateServiceImpl( final ApplicationStateRepository aApplicationStateRepository )
    {
        applicationStateRepository = aApplicationStateRepository;
    }

    @Override
    public ApplicationState loadEntity( final Long aApplicationStateId ) throws NoResultException
    {
        return tryLoadEntity( aApplicationStateId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< ApplicationState > tryLoadEntity( final Long aApplicationStateId )
    {
        return applicationStateRepository.findById( aApplicationStateId );
    }

    @Override
    public List< ApplicationState > loadAllEntities()
    {
        return Lists.newArrayList( applicationStateRepository.findAll() );
    }

    @Override
    public ApplicationState saveOrUpdate( final ApplicationState aApplicationState )
    {
        return applicationStateRepository.save( aApplicationState );
    }

    @Override
    public void deleteEntity( final Long aApplicationStateId )
    {
        applicationStateRepository.deleteById( aApplicationStateId );
    }

    @Override
    public Optional< ApplicationState > tryLoadByName( final String aName )
    {
        return applicationStateRepository.findByName( aName );
    }
}
