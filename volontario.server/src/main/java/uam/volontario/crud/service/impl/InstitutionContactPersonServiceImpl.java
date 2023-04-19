package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.InstitutionContactPersonRepository;
import uam.volontario.crud.repository.InstitutionRepository;
import uam.volontario.crud.service.InstitutionContactPersonService;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.model.institution.impl.InstitutionContactPerson;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation of {@linkplain uam.volontario.model.institution.impl.InstitutionContactPerson}.
 */
@Service
public class InstitutionContactPersonServiceImpl implements InstitutionContactPersonService
{
    private final InstitutionContactPersonRepository institutionContactPersonRepository;

    /**
     * CDI constructor.
     *
     * @param aInstitutionContactPersonRepository institution contact repository
     */
    @Autowired
    public InstitutionContactPersonServiceImpl( final InstitutionContactPersonRepository aInstitutionContactPersonRepository )
    {
        institutionContactPersonRepository = aInstitutionContactPersonRepository;
    }

    @Override
    public InstitutionContactPerson loadEntity( final Long aInstitutionContactPersonId ) throws NoResultException
    {
        return tryLoadEntity( aInstitutionContactPersonId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< InstitutionContactPerson > tryLoadEntity( final Long aInstitutionContactPersonId )
    {
        return institutionContactPersonRepository.findById( aInstitutionContactPersonId );
    }

    @Override
    public List< InstitutionContactPerson > loadAllEntities()
    {
        return Lists.newArrayList( institutionContactPersonRepository.findAll() );
    }

    @Override
    public InstitutionContactPerson saveOrUpdate( final InstitutionContactPerson aInstitutionContactPerson )
    {
        return institutionContactPersonRepository.save( aInstitutionContactPerson );
    }

    @Override
    public void deleteEntity( final Long aInstitutionContactPersonId )
    {
        institutionContactPersonRepository.deleteById( aInstitutionContactPersonId );
    }
}
