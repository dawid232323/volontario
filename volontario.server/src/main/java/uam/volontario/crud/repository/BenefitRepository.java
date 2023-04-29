package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.offer.impl.Benefit;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.Benefit} entity.
 */
@Repository
public interface BenefitRepository extends CrudRepository< Benefit, Long >
{}