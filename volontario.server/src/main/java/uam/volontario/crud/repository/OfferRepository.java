package uam.volontario.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uam.volontario.model.offer.impl.Offer;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.Offer} entity. We're extending from {@linkplain JpaRepository}
 * to make use of filtering methods provided.
 */
public interface OfferRepository extends JpaRepository< Offer, Long >, JpaSpecificationExecutor< Offer >
{}
