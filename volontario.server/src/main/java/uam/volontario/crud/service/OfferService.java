package uam.volontario.crud.service;

import org.springframework.data.domain.Page;
import uam.volontario.crud.specification.OfferSpecification;
import uam.volontario.model.offer.impl.Offer;

import org.springframework.data.domain.Pageable;

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
}
