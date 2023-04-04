package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.ExperienceLevelRepository;
import uam.volontario.crud.service.ExperienceLevelService;
import uam.volontario.model.volunteer.impl.ExperienceLevel;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain ExperienceLevelService}.
 */
@Service
public class ExperienceLevelServiceImpl implements ExperienceLevelService
{
    private final ExperienceLevelRepository experienceLevelRepository;

    /**
     * CDI constructor.
     *
     * @param aExperienceLevelRepository experience level repository.
     */
    @Autowired
    public ExperienceLevelServiceImpl( final ExperienceLevelRepository aExperienceLevelRepository )
    {
        experienceLevelRepository = aExperienceLevelRepository;
    }

    @Override
    public ExperienceLevel loadEntity( final Long aVolunteerExperienceId ) throws NoResultException
    {
        return experienceLevelRepository.findById( aVolunteerExperienceId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< ExperienceLevel > tryLoadEntity( final Long aVolunteerExperienceId )
    {
        return experienceLevelRepository.findById( aVolunteerExperienceId );
    }

    @Override
    public List< ExperienceLevel > loadAllEntities()
    {
        return Lists.newArrayList( experienceLevelRepository.findAll() );
    }

    @Override
    public ExperienceLevel saveOrUpdate( final ExperienceLevel aExperienceLevel )
    {
        return experienceLevelRepository.save( aExperienceLevel );
    }

    @Override
    public void deleteEntity( final Long aVolunteerExperienceId )
    {
        experienceLevelRepository.deleteById( aVolunteerExperienceId );
    }
}
