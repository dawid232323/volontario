package uam.volontario.crud.service;

import uam.volontario.model.offer.impl.VoluntaryPresence;

import java.util.List;

/**
 * Injection interface for {@linkplain VoluntaryPresence}'s service.
 */
public interface VoluntaryPresenceService extends EntityService< VoluntaryPresence >
{
    /**
     * Persists/updates given Voluntary Presences.
     *
     * @param aVoluntaryPresences applications.
     *
     * @return list of created or updated Voluntary Presences.
     */
    List< VoluntaryPresence > saveAll( List< VoluntaryPresence > aVoluntaryPresences );
}
