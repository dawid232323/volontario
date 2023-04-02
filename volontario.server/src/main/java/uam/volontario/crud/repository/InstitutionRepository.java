package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.institution.impl.Institution;

/**
 * Repository for {@linkplain uam.volontario.model.institution.impl.Institution} entity.
 */
@Repository
public interface InstitutionRepository extends CrudRepository< Institution, Long >
{}
