package uam.volontario.crud.service;

import uam.volontario.model.offer.impl.Benefit;

import java.util.List;

/**
 * Injection interface for {@linkplain Benefit}'s service.
 */
public interface BenefitService extends SoftDeletableEntityService< Benefit >
{
    /**
     * Fetches all Benefits with given IDs.
     *
     * @param aIds IDs.
     *
     * @return all Benefits with given IDs.
     */
    List< Benefit > findByIds( List< Long > aIds );
}
