package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.offer.impl.VoluntaryPresenceState;

import java.util.Optional;

/**
 * Repository for {@linkplain VoluntaryPresenceState} entity.
 */
@Repository
public interface VoluntaryPresenceStateRepository extends CrudRepository< VoluntaryPresenceState, Long >
{
    /**
     * Looks for Voluntary Presence State with given state.
     *
     * @param aState state.
     *
     * @return Voluntary Presence State entity if found.
     */
    Optional< VoluntaryPresenceState > findByState( String aState );
}
