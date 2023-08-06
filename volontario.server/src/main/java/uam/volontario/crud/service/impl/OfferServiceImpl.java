package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.OfferRepository;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.specification.OfferSpecification;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.offer.impl.Offer;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation of {@linkplain uam.volontario.model.offer.impl.Offer}.
 */
@Service
public class OfferServiceImpl implements OfferService
{
    private final OfferRepository offerRepository;

    /**
     * CDI constructor.
     *
     * @param aOfferRepository offer repository.
     */
    @Autowired
    public OfferServiceImpl( final OfferRepository aOfferRepository )
    {
        offerRepository = aOfferRepository;
    }

    @Override
    public Offer loadEntity( final Long aOfferId ) throws NoResultException
    {
        return tryLoadEntity( aOfferId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Page< Offer > findFiltered( OfferSpecification aSpecification, Pageable aPageable )
    {
        return offerRepository.findAll( aSpecification, aPageable );
    }

    @Override
    public Optional< Offer > tryLoadEntity( final Long aOfferId )
    {
        return offerRepository.findById( aOfferId );
    }

    @Override
    public List< Offer > loadAllEntities()
    {
        return Lists.newArrayList( offerRepository.findAll() );
    }

    @Override
    public Offer saveOrUpdate( final Offer aOffer )
    {
        return offerRepository.save( aOffer );
    }

    @Override
    public void deleteEntity( final Long aOfferId )
    {
        offerRepository.deleteById( aOfferId );
    }

    @Override
    public List< Offer > findAllOffersAssignedToModerator( final User aModerator )
    {
        return offerRepository.findAllByAssignedModerator( aModerator );
    }

    @Override
    public List< Offer > findAllUnassignedOffers()
    {
        return offerRepository.findAllByAssignedModeratorIsNull();
    }

    @Override
    public List< Offer > saveAll( final List< Offer > aOffers )
    {
        return offerRepository.saveAll( aOffers );
    }

    @Override
    public Boolean isUserEntitledToOfferDetails(final User aLoggedUser, final Offer aOffer )
    {
        if ( aLoggedUser.hasUserRole( UserRole.VOLUNTEER ) )
        {
            return false;
        }
        if ( aLoggedUser.hasUserRole( UserRole.ADMIN ) || aLoggedUser.hasUserRole( UserRole.ADMIN ) )
        {
            return true;
        }

        final Institution offerInstitution = aOffer.getInstitution();
        final Institution userInstitution = aLoggedUser.getInstitution();

        if ( aLoggedUser.hasUserRole( UserRole.INSTITUTION_ADMIN ) && userInstitution.getId()
                .equals( offerInstitution.getId() ) )
        {
            return true;
        }

        return aOffer.getContactPerson().getId().equals( aLoggedUser.getId() );
    }
}
