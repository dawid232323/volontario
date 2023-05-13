package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import uam.volontario.model.common.impl.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uam.volontario.model.offer.impl.Offer;

import java.util.List;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.Offer} entity. We're extending from {@linkplain JpaRepository}
 * to make use of filtering methods provided.
 */
public interface OfferRepository extends JpaRepository< Offer, Long >, JpaSpecificationExecutor< Offer >
{
    /**
     * Looks for all Offers which are assigned to given Moderator.
     *
     * @param aModerator Moderator.
     *
     * @return list of Offers assigned to Moderator.
     */
    List< Offer > findAllByAssignedModerator( User aModerator );

    /**
     * Looks for all Offers which are not assigned to any Moderator.
     *
     * @return list of all unassigned Offers.
     */
    List< Offer > findAllByAssignedModeratorIsNull();
}
