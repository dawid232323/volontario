package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.ApplicationRepository;
import uam.volontario.crud.service.ApplicationService;
import uam.volontario.model.offer.impl.Application;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain ApplicationService}.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService
{
    private final ApplicationRepository applicationRepository;

    /**
     * CDI constructor.
     *
     * @param aApplicationRepository application repository.
     */
    @Autowired
    public ApplicationServiceImpl( final ApplicationRepository aApplicationRepository )
    {
        applicationRepository = aApplicationRepository;
    }

    @Override
    public Application loadEntity( final Long aApplicationId ) throws NoResultException
    {
        return tryLoadEntity( aApplicationId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< Application > tryLoadEntity( final Long aApplicationId )
    {
        return applicationRepository.findById( aApplicationId );
    }

    @Override
    public List< Application > loadAllEntities()
    {
        return Lists.newArrayList( applicationRepository.findAll() );
    }

    @Override
    public Application saveOrUpdate( final Application aApplication )
    {
        return applicationRepository.save( aApplication );
    }

    @Override
    public void deleteEntity( final Long aApplicationId )
    {
        applicationRepository.deleteById( aApplicationId );
    }
}
