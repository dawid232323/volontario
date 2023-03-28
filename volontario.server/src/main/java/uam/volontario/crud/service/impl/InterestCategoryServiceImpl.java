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
    @Autowired
    private InterestCategoryRepository interestCategoryRepository;

    @Override
    public InterestCategory loadEntity( final Long aEntityId )
    {
        return interestCategoryRepository.findById( aEntityId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< InterestCategory > tryLoadEntity( final Long aEntityId )
    {
        return interestCategoryRepository.findById( aEntityId );
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
    public InterestCategory saveOrUpdate( final InterestCategory aEntity )
    {
        return interestCategoryRepository.save( aEntity );
    }

    @Override
    public void deleteEntity( final Long aEntityId )
    {
        interestCategoryRepository.deleteById( aEntityId );
    }
}
