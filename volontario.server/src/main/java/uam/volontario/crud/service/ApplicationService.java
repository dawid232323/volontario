package uam.volontario.crud.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uam.volontario.crud.specification.ApplicationSpecification;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.Offer;

import java.util.List;

import java.util.List;
import java.util.Map;

/**
 * Injection interface for {@linkplain Application}'s service.
 */
public interface ApplicationService extends EntityService< Application >
{
    /**
     * Retrieves data based on specification defined with {@linkplain uam.volontario.crud.specification.ApplicationSpecification}
     *
     * @param aSpecification - defines parameters used to filter retrieved data.
     * @param aPageable      - Spring parameters defining paging of result set.
     * @return Page with {@linkplain Application} entities.
     */
    Page< Application > findFiltered( ApplicationSpecification aSpecification, Pageable aPageable );

    Map< Long, Long > getApplicationsCountForOffers( List< Long > aOfferIds );

    /**
     * Looks for all Application for given {@linkplain Offer}s.
     *
     * @param aOffers offers.
     *
     * @return Applications for given Offers.
     */
    List< Application > findAllByOffers( List< Offer > aOffers );

    /**
     * Creates/updates given Applications.
     *
     * @param aApplications applications.
     *
     * @return list of created or updated Applications.
     */
    List< Application > saveAll( List< Application > aApplications );
}
