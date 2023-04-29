package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.BenefitRepository;
import uam.volontario.crud.service.BenefitService;
import uam.volontario.model.offer.impl.Benefit;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain BenefitService}.
 */
@Service
public class BenefitServiceImpl implements BenefitService
{
    private final BenefitRepository benefitRepository;

    /**
     * CDI constructor.
     *
     * @param aBenefitRepository benefit repository.
     */
    @Autowired
    public BenefitServiceImpl( final BenefitRepository aBenefitRepository )
    {
        benefitRepository = aBenefitRepository;
    }

    @Override
    public Benefit loadEntity( final Long aBenefitId ) throws NoResultException
    {
        return tryLoadEntity( aBenefitId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< Benefit > tryLoadEntity( final Long aBenefitId )
    {
        return benefitRepository.findById( aBenefitId );
    }

    @Override
    public List< Benefit > loadAllEntities()
    {
        return Lists.newArrayList( benefitRepository.findAll() );
    }

    @Override
    public Benefit saveOrUpdate( final Benefit aBenefit )
    {
        return benefitRepository.save( aBenefit );
    }

    @Override
    public void deleteEntity( final Long aBenefitId )
    {
        benefitRepository.deleteById( aBenefitId );
    }
}
