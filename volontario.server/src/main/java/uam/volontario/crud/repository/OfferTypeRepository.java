package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.offer.impl.OfferType;

/**
 * Repository for {@linkplain OfferType} entity.
 */
@Repository
public interface OfferTypeRepository extends CrudRepository< OfferType, Long >
{}
