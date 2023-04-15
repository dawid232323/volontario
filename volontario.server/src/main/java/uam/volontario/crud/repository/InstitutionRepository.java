package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.institution.impl.Institution;

import java.util.Optional;

/**
 * Repository for {@linkplain uam.volontario.model.institution.impl.Institution} entity.
 */
@Repository
public interface InstitutionRepository extends CrudRepository< Institution, Long >
{
    /**
     * Looks for Institution with given registration token.
     *
     * @param aRegistrationToken registration token.
     *
     * @return institution with given registration token if found.
     */
    Optional< Institution > findByRegistrationToken( String aRegistrationToken );
}
