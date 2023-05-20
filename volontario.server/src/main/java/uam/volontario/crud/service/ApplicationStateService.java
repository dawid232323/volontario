package uam.volontario.crud.service;

import uam.volontario.model.offer.impl.ApplicationState;

import java.util.Optional;

/**
 * Injection interface for {@linkplain ApplicationState}'s service.
 */
public interface ApplicationStateService extends EntityService< ApplicationState >
{
    /**
     * Looks for Application State with given name.
     *
     * @param aName name.
     *
     * @return Application State entity if found.
     */
    Optional< ApplicationState > tryLoadByName( String aName );
}
