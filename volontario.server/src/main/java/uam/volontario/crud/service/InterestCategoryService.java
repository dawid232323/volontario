package uam.volontario.crud.service;

import uam.volontario.model.volunteer.impl.InterestCategory;

import java.util.List;

/**
 * Injection interface for {@linkplain InterestCategory}'s service.
 */
public interface InterestCategoryService extends SoftDeletableEntityService< InterestCategory >
{
    /**
     * Fetches all Interest Categories with given IDs.
     *
     * @param aIds IDs.
     *
     * @return all Interest Categories with given IDs.
     */
    List< InterestCategory > findByIds( List< Long > aIds );
}
