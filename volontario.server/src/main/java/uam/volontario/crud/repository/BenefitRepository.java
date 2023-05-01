package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.offer.impl.Benefit;

import java.util.List;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.Benefit} entity.
 */
@Repository
public interface BenefitRepository extends CrudRepository< Benefit, Long >
{
    /**
     * Fetches all {@linkplain Benefit} entities with certain id
     * @param aIds list of ids that need to be queried
     * @return list of benefits corresponding to the param id list
     */
    List< Benefit > findByIdIn( List< Long > aIds );
}