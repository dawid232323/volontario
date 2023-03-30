package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.VolunteerExperienceRepository;
import uam.volontario.crud.service.VolunteerExperienceService;
import uam.volontario.model.volunteer.impl.VolunteerExperience;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain VolunteerExperienceService}.
 */
@Service
public class VolunteerExperienceServiceImpl implements VolunteerExperienceService
{
    @Autowired
    private VolunteerExperienceRepository volunteerExperienceRepository;

    @Override
    public VolunteerExperience loadEntity( final Long aEntityId ) throws NoResultException
    {
        return volunteerExperienceRepository.findById( aEntityId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional<VolunteerExperience> tryLoadEntity( final Long aEntityId )
    {
        return volunteerExperienceRepository.findById( aEntityId );
    }

    @Override
    public List<VolunteerExperience> loadAllEntities()
    {
        return Lists.newArrayList( volunteerExperienceRepository.findAll() );
    }

    @Override
    public VolunteerExperience saveOrUpdate( final VolunteerExperience aEntity )
    {
        return volunteerExperienceRepository.save( aEntity );
    }

    @Override
    public void deleteEntity( final Long aEntityId )
    {
        volunteerExperienceRepository.deleteById( aEntityId );
    }
}
