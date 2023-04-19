package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.institution.impl.InstitutionContactPerson;

/**
 * Repository for {@linkplain uam.volontario.model.institution.impl.InstitutionContactPerson} entity.
 */
@Repository
public interface InstitutionContactPersonRepository extends CrudRepository< InstitutionContactPerson, Long >
{}
