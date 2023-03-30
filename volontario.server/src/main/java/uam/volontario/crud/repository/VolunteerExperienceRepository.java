package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.volunteer.impl.VolunteerExperience;

/**
 * Repository for {@linkplain VolunteerExperience} entity.
 */
@Repository
public interface VolunteerExperienceRepository extends CrudRepository< VolunteerExperience, Long >
{}
