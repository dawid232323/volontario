package uam.volontario.crud.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.volunteer.impl.ExperienceLevel;

import java.util.List;

/**
 * Repository for {@linkplain ExperienceLevel} entity.
 */
@Repository
public interface ExperienceLevelRepository extends CrudRepository< ExperienceLevel, Long >
{
    /**
     * Fetches all Experience Levels which are marked as used.
     *
     * @return all used Experience Levels.
     */
    @Query( "SELECT experienceLevel FROM ExperienceLevel experienceLevel " +
            "WHERE experienceLevel.isUsed = true" )
    List< ExperienceLevel > findAllByUsedTrue();

    /**
     * Fetches all Experience Levels which are marked as not used.
     *
     * @return all not used Experience Levels.
     */
    @Query( "SELECT experienceLevel FROM ExperienceLevel experienceLevel " +
            "WHERE experienceLevel.isUsed = false" )
    List< ExperienceLevel > findAllByUsedFalse();
}
