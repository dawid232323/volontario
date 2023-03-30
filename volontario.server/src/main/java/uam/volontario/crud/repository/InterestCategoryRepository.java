package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.volunteer.impl.InterestCategory;

import java.util.List;

/**
 * Repository for {@linkplain InterestCategory} entity.
 */
@Repository
public interface InterestCategoryRepository extends CrudRepository< InterestCategory, Long >
{
    /**
     * Fetches all Interest Categories which have IDs as provided.
     *
     * @param aIds ids.
     *
     * @return List of Interest Categories.
     */
    List< InterestCategory > findByIdIn( List< Long > aIds );
}
