package uam.volontario.crud.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uam.volontario.crud.specification.OfferSpecification;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.Offer;

import java.util.List;

/**
 * Injection interface for {@linkplain uam.volontario.model.offer.impl.Offer}'s service.
 */
public interface OfferService extends EntityService< Offer >
{
    /**
     * Retrieves data based on specification defined with {@linkplain OfferSpecification}
     *
     * @param aSpecification - defines parameters used to filter retrieved data.
     *
     * @param aPageable - Spring parameters defining paging of result set.
     *
     * @return Page with {@linkplain Offer} entities.
     * */
    Page< Offer > findFiltered( OfferSpecification aSpecification, Pageable aPageable );

    /**
     * Looks for all Offers which are assigned to given Moderator.
     *
     * @param aModerator Moderator.
     *
     * @return list of Offers assigned to Moderator.
     */
    List< Offer > findAllOffersAssignedToModerator( User aModerator );

    /**
     * Looks for all Offers which are not assigned to any Moderator.
     *
     * @return list of all unassigned Offers.
     */
    List< Offer > findAllUnassignedOffers();
}
