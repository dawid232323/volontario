package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import uam.volontario.model.offer.impl.Offer;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.Offer} entity.
 */
public interface OfferRepository extends CrudRepository< Offer, Long >
{}
