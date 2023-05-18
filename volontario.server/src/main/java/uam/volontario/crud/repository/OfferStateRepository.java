package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.offer.impl.OfferState;

import java.util.Optional;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.OfferState} entity.
 */
@Repository
public interface OfferStateRepository extends CrudRepository< OfferState, Long >
{
    /**
     * Looks for Offer State with given state.
     *
     * @param aState state.
     *
     * @return Offer State entity if found.
     */
    Optional< OfferState > findByState( String aState );
}
