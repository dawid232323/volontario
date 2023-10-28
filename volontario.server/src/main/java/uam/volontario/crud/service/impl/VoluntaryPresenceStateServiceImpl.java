package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.VoluntaryPresenceStateRepository;
import uam.volontario.crud.service.VoluntaryPresenceStateService;
import uam.volontario.model.offer.impl.VoluntaryPresenceState;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain VoluntaryPresenceStateService}.
 */
@Service
public class VoluntaryPresenceStateServiceImpl implements VoluntaryPresenceStateService
{
    private final VoluntaryPresenceStateRepository voluntaryPresenceStateRepository;

    /**
     * Constructor.
     *
     * @param aVoluntaryPresenceStateRepository voluntary presence state repository.
     */
    @Autowired
    public VoluntaryPresenceStateServiceImpl( final VoluntaryPresenceStateRepository aVoluntaryPresenceStateRepository )
    {
        voluntaryPresenceStateRepository = aVoluntaryPresenceStateRepository;
    }

    @Override
    public VoluntaryPresenceState loadEntity(final Long aVoluntaryPresenceId ) throws NoResultException
    {
        return tryLoadEntity( aVoluntaryPresenceId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< VoluntaryPresenceState > tryLoadEntity( final Long aVoluntaryPresenceId )
    {
        return voluntaryPresenceStateRepository.findById( aVoluntaryPresenceId );
    }

    @Override
    public List< VoluntaryPresenceState > loadAllEntities()
    {
        return Lists.newArrayList( voluntaryPresenceStateRepository.findAll() );
    }

    @Override
    public VoluntaryPresenceState saveOrUpdate( final VoluntaryPresenceState aVoluntaryPresence )
    {
        return voluntaryPresenceStateRepository.save( aVoluntaryPresence );
    }

    @Override
    public void deleteEntity( final Long aVoluntaryPresenceId )
    {
        voluntaryPresenceStateRepository.deleteById( aVoluntaryPresenceId );
    }

    @Override
    public Optional< VoluntaryPresenceState > tryLoadByState( final String aState )
    {
        return voluntaryPresenceStateRepository.findByState( aState );
    }
}
