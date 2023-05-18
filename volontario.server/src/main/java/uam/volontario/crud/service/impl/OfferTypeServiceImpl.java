package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.OfferTypeRepository;
import uam.volontario.crud.service.OfferTypeService;
import uam.volontario.model.offer.impl.OfferType;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain OfferTypeService}.
 */
@Service
public class OfferTypeServiceImpl implements OfferTypeService
{
    private final OfferTypeRepository offerTypeRepository;

    /**
     * CDI constructor.
     *
     * @param aOfferTypeRepository offer type repository.
     */
    @Autowired
    public OfferTypeServiceImpl( final OfferTypeRepository aOfferTypeRepository )
    {
        offerTypeRepository = aOfferTypeRepository;
    }

    @Override
    public OfferType loadEntity( final Long aOfferTypeId ) throws NoResultException
    {
        return tryLoadEntity( aOfferTypeId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< OfferType > tryLoadEntity( final Long aOfferTypeId )
    {
        return offerTypeRepository.findById( aOfferTypeId );
    }

    @Override
    public List< OfferType > loadAllEntities()
    {
        return Lists.newArrayList( offerTypeRepository.findAll() );
    }

    @Override
    public OfferType saveOrUpdate( final OfferType aOfferType )
    {
        return offerTypeRepository.save( aOfferType );
    }

    @Override
    public void deleteEntity( final Long aOfferTypeId )
    {
        offerTypeRepository.deleteById( aOfferTypeId );
    }
}
