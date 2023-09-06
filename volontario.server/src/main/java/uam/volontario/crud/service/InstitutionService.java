package uam.volontario.crud.service;

import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;

import java.util.List;
import java.util.Optional;

/**
 * Injection interface for {@linkplain Institution}'s service.
 */
public interface InstitutionService extends EntityService< Institution >
{
    /**
     * Looks for Institution with given registration token.
     *
     * @param aRegistrationToken registration token.
     *
     * @return institution with given registration token if found.
     */
    Optional< Institution > loadByRegistrationToken( String aRegistrationToken );

    /**
     * Retrieves institution workers.
     *
     * @param aInstitutionId id of the institution
     *
     * @return list of workers assigned to the institution with given id
     */
    List< User > getInstitutionWorkers( final Long aInstitutionId );

    /**
     * Retrieves list of all users assigned to any institution.
     *
     * @return list of all users assigned to any institution.
     */
    List< User > getAllInstitutionWorkers();
}
