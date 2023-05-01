package uam.volontario.crud.service;

import uam.volontario.model.offer.impl.Benefit;

import java.util.List;

/**
 * Injection interface for {@linkplain Benefit}'s service.
 */
public interface BenefitService extends EntityService< Benefit >
{
    List<Benefit> findByIds( List<Long> aIds );
}
