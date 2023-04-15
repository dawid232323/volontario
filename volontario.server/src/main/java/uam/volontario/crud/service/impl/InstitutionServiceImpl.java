package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.InstitutionRepository;
import uam.volontario.crud.service.InstitutionService;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.model.institution.impl.Institution;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation of {@linkplain InterestCategoryService}.
 */
@Service
public class InstitutionServiceImpl implements InstitutionService
{
    private final InstitutionRepository institutionRepository;

    /**
     * CDI constructor.
     *
     * @param aInstitutionRepository institution repository.
     */
    @Autowired
    public InstitutionServiceImpl( final InstitutionRepository aInstitutionRepository )
    {
        institutionRepository = aInstitutionRepository;
    }

    @Override
    public Institution loadEntity( final Long aInstitutionId ) throws NoResultException
    {
        return tryLoadEntity( aInstitutionId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< Institution > tryLoadEntity( final Long aInstitutionId )
    {
        return institutionRepository.findById( aInstitutionId );
    }

    @Override
    public List< Institution > loadAllEntities()
    {
        return Lists.newArrayList( institutionRepository.findAll() );
    }

    @Override
    public Institution saveOrUpdate( final Institution aInstitution )
    {
        return institutionRepository.save( aInstitution );
    }

    @Override
    public void deleteEntity( final Long aInstitutionId )
    {
        institutionRepository.deleteById( aInstitutionId );
    }

    @Override
    public Optional< Institution > loadByRegistrationToken( final String aRegistrationToken )
    {
        return institutionRepository.findByRegistrationToken( aRegistrationToken );
    }
}
