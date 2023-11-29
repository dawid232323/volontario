package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.VoluntaryRating;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.VoluntaryRating} entity.
 */
@Repository
public interface VoluntaryRatingRepository extends CrudRepository< VoluntaryRating, Long >
{
    /**
     * Tried to find Voluntary Rating with given Offer and Volunteer.
     *
     * @param aOffer Offer.
     *
     * @param aVolunteer Volunteer.
     *
     * @return voluntary rating (if found).
     */
    Optional< VoluntaryRating > findByOfferAndVolunteer( Offer aOffer, User aVolunteer );

    /**
     * Tried to find Voluntary Ratings with given Institution.
     *
     * @param aInstitution institution.
     *
     * @return voluntary ratings.
     */
    List< VoluntaryRating > findAllByInstitution( Institution aInstitution );

    /**
     * Tried to find Voluntary Ratings with given Volunteer.
     *
     * @param aVolunteer volunteer.
     *
     * @return voluntary ratings.
     */
    List< VoluntaryRating > findAllByVolunteer( User aVolunteer );
}
