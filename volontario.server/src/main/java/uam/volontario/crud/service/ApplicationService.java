package uam.volontario.crud.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uam.volontario.crud.specification.ApplicationSpecification;
import uam.volontario.model.offer.impl.Application;

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
}
