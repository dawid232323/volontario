package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.VoluntaryPresenceRepository;
import uam.volontario.crud.service.VoluntaryPresenceService;
import uam.volontario.model.offer.impl.VoluntaryPresence;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain VoluntaryPresenceService}.
 */
@Service
public class VoluntaryPresenceServiceImpl implements VoluntaryPresenceService
{
    private final VoluntaryPresenceRepository voluntaryPresenceRepository;

    /**
     * Constructor.
     *
     * @param aVoluntaryPresenceRepository voluntary presence repository.
     */
    @Autowired
    public VoluntaryPresenceServiceImpl( final VoluntaryPresenceRepository aVoluntaryPresenceRepository )
    {
        voluntaryPresenceRepository = aVoluntaryPresenceRepository;
    }

    @Override
    public VoluntaryPresence loadEntity( final Long aVoluntaryPresenceId ) throws NoResultException
    {
        return tryLoadEntity( aVoluntaryPresenceId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< VoluntaryPresence > tryLoadEntity( final Long aVoluntaryPresenceId )
    {
        return voluntaryPresenceRepository.findById( aVoluntaryPresenceId );
    }

    @Override
    public List< VoluntaryPresence > loadAllEntities()
    {
        return Lists.newArrayList( voluntaryPresenceRepository.findAll() );
    }

    @Override
    public VoluntaryPresence saveOrUpdate( final VoluntaryPresence aVoluntaryPresence )
    {
        return voluntaryPresenceRepository.save( aVoluntaryPresence );
    }

    @Override
    public void deleteEntity( final Long aVoluntaryPresenceId )
    {
        voluntaryPresenceRepository.deleteById( aVoluntaryPresenceId );
    }

    @Override
    public List< VoluntaryPresence > saveAll( final List< VoluntaryPresence > aVoluntaryPresences )
    {
        return Lists.newArrayList( voluntaryPresenceRepository.saveAll( aVoluntaryPresences ) );
    }
}
