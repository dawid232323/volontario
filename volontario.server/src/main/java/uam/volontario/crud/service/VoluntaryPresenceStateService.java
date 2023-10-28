package uam.volontario.crud.service;

import uam.volontario.model.offer.impl.VoluntaryPresenceState;

import java.util.Optional;

/**
 * Injection interface for {@linkplain VoluntaryPresenceService}'s service.
 */
public interface VoluntaryPresenceStateService extends EntityService< VoluntaryPresenceState >
{
    /**
     * Looks for Voluntary Presence State with given state.
     *
     * @param aState state.
     *
     * @return Voluntary Presence State entity if found.
     */
    Optional< VoluntaryPresenceState > tryLoadByState( String aState );
}
