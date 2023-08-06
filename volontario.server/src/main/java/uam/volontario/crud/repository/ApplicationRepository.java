package uam.volontario.crud.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.Offer;

import java.util.List;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.Application} entity.
 */
@Repository
public interface ApplicationRepository extends JpaRepository< Application, Long >, JpaSpecificationExecutor< Application >
{
    /**
     * Looks for all Application for given {@linkplain Offer}s.
     *
     * @param aOffers offers.
     *
     * @return Applications for given Offers.
     */
    List< Application > findAllByOfferIn( List< Offer > aOffers );

    /**
     * Retrieves applications count per single offer grouped by offer id.
     *
     * @param aOfferIds {@linkplain List} of offer ids that need to have applications counted
     *
     * @return {@linkplain List} of {@linkplain Tuple} where "offerId" is primary key of single offer
     *         and "applicationCount" is number of applications for single offer
     */
    @Query( "select a.offer.id as offerId, count(*) as applicationCount from Application a " +
            "where a.offer.id in :offerIds group by a.offer.id" )
    List< Tuple > getApplicationsCountByOffer( @Param( "offerIds" ) final List< Long > aOfferIds );
}
