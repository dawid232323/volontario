package uam.volontario.crud.repository;

import org.springframework.data.jpa.repository.Query;
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

    /**
     * Fetches all Interest Categories which are marked as used.
     *
     * @return all used Interest Categories.
     */
    @Query( "SELECT interestCategory FROM InterestCategory interestCategory " +
            "WHERE interestCategory.isUsed = true" )
    List< InterestCategory > findAllByUsedTrue();

    /**
     * Fetches all Interest Categories which are marked as not used.
     *
     * @return all not used Interest Categories.
     */
    @Query( "SELECT interestCategory FROM InterestCategory interestCategory " +
            "WHERE interestCategory.isUsed = false" )
    List< InterestCategory > findAllByUsedFalse();
}
