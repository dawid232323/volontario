package uam.volontario.crud.service;

import uam.volontario.model.offer.impl.OfferState;

import java.util.Optional;

/**
 * Injection interface for {@linkplain uam.volontario.model.offer.impl.OfferState}'s service.
 */
public interface OfferStateService extends EntityService< OfferState >
{
    /**
     * Looks for Offer State with given state.
     *
     * @param aState state.
     *
     * @return Offer State entity if found.
     */
    Optional< OfferState > tryLoadByState( String aState );
}
