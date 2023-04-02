package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.volunteer.impl.ExperienceLevel;

/**
 * Repository for {@linkplain ExperienceLevel} entity.
 */
@Repository
public interface ExperienceLevelRepository extends CrudRepository< ExperienceLevel, Long >
{}
