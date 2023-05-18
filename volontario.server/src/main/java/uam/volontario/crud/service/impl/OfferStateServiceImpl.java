package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.OfferStateRepository;
import uam.volontario.crud.service.OfferStateService;
import uam.volontario.model.offer.impl.OfferState;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain OfferServiceImpl}.
 */
@Service
public class OfferStateServiceImpl implements OfferStateService
{
    private final OfferStateRepository offerStateRepository;

    /**
     * Constructor.
     *
     * @param aOfferStateRepository offer state repository.
     */
    @Autowired
    public OfferStateServiceImpl( final OfferStateRepository aOfferStateRepository )
    {
        offerStateRepository = aOfferStateRepository;
    }

    @Override
    public OfferState loadEntity( final Long aOfferStateId ) throws NoResultException
    {
        return tryLoadEntity( aOfferStateId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< OfferState > tryLoadEntity( final Long aOfferStateId )
    {
        return offerStateRepository.findById( aOfferStateId );
    }

    @Override
    public List< OfferState > loadAllEntities()
    {
        return Lists.newArrayList( offerStateRepository.findAll() );
    }

    @Override
    public OfferState saveOrUpdate( final OfferState aOfferState )
    {
        return offerStateRepository.save( aOfferState );
    }

    @Override
    public void deleteEntity( final Long aOfferStateId )
    {
        offerStateRepository.deleteById( aOfferStateId );
    }

    @Override
    public Optional< OfferState > tryLoadByState( final String aState )
    {
        return offerStateRepository.findByState( aState );
    }
}
