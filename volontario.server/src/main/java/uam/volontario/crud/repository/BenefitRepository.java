package uam.volontario.crud.repository;

import org.springframework.data.jpa.repository.Query;
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
     * Fetches all {@linkplain Benefit} entities with certain id.
     *
     * @param aIds list of ids that need to be queried.
     *
     * @return list of benefits corresponding to the param id list.
     */
    List< Benefit > findByIdIn( List< Long > aIds );

    /**
     * Fetches all Benefits which are marked as used.
     *
     * @return all used Benefits.
     */
    @Query( "SELECT benefit FROM Benefit benefit WHERE benefit.isUsed = true" )
    List< Benefit > findAllByUsedTrue();

    /**
     * Fetches all Benefits which are marked as not used.
     *
     * @return all not used Benefits.
     */
    @Query( "SELECT benefit FROM Benefit benefit WHERE benefit.isUsed = false" )
    List< Benefit > findAllByUsedFalse();
}