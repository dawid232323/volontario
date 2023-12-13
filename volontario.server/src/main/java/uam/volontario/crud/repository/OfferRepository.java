package uam.volontario.crud.repository;

import org.springframework.data.jpa.repository.Query;
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

    /**
     * Resolves list of offers that are common with specified institution worker and volunteer.
     * Common means that volunteer has applied for certain offer and his application is in state under recruitment.
     *
     * @param aInstitutionWorkerId id of institution worker
     *
     * @param aVolunteerId id of volunteer
     *
     * @return list of common offers
     */
    @Query("select o from Offer o join Application a on a.offer.id = o.id " +
            "join Institution i on i.id = o.institution.id " +
            "join User cp on o.contactPerson.id = cp.id " +
            "where a.volunteer.id = ?2 and a.state.id = 3l and cp.id = ?1")
    List< Offer > findCommonOffersWithInstitutionWorker( final Long aInstitutionWorkerId, final Long aVolunteerId );

    /**
     * Resolves list of offers that are common with specified institution and volunteer.
     * Common means that volunteer has applied for certain offer and his application is in state under recruitment.
     *
     * @param aInstitutionId id of institution worker
     *
     * @param aVolunteerId id of volunteer
     *
     * @return list of common offers
     */
    @Query("select o from Offer o join Application a on a.offer.id = o.id " +
            "join Institution i on i.id = o.institution.id " +
            "join User cp on o.contactPerson.id = cp.id " +
            "where a.volunteer.id = ?2 and a.state.id = 3l and i.id = ?1")
    List< Offer > findCommonOffersWithInstitution( final Long aInstitutionId, final Long aVolunteerId );
}
