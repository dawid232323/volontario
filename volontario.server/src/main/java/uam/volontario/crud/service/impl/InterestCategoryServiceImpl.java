package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.InterestCategoryRepository;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.model.volunteer.impl.InterestCategory;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation of {@linkplain InterestCategoryService}.
 */
@Service
public class InterestCategoryServiceImpl implements InterestCategoryService
{
    private final InterestCategoryRepository interestCategoryRepository;

    /**
     * CDI constructor.
     *
     * @param aInterestCategoryRepository interest category repository.
     */
    @Autowired
    public InterestCategoryServiceImpl( final InterestCategoryRepository aInterestCategoryRepository )
    {
        interestCategoryRepository = aInterestCategoryRepository;
    }

    @Override
    public InterestCategory loadEntity( final Long aInterestCategoryId )
    {
        return tryLoadEntity( aInterestCategoryId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< InterestCategory > tryLoadEntity( final Long aInterestCategoryId )
    {
        return interestCategoryRepository.findById( aInterestCategoryId );
    }

    @Override
    public List< InterestCategory > findByIds( final List< Long > aIds )
    {
        return interestCategoryRepository.findByIdIn( aIds );
    }

    @Override
    public List< InterestCategory > loadAllEntities()
    {
        return Lists.newArrayList( interestCategoryRepository.findAll() );
    }

    @Override
    public InterestCategory saveOrUpdate( final InterestCategory aInterestCategory )
    {
        return interestCategoryRepository.save( aInterestCategory );
    }

    @Override
    public void deleteEntity( final Long aInterestCategoryId )
    {
        interestCategoryRepository.deleteById( aInterestCategoryId );
    }

    @Override
    public List< InterestCategory > findAllUsed()
    {
        return interestCategoryRepository.findAllByUsedTrue();
    }

    @Override
    public List< InterestCategory > findAllNotUsed()
    {
        return interestCategoryRepository.findAllByUsedFalse();
    }
}
