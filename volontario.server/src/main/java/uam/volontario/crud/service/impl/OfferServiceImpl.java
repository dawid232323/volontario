package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.OfferRepository;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.specification.OfferSpecification;
import uam.volontario.model.offer.impl.Offer;

import org.springframework.data.domain.Pageable;
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
}
