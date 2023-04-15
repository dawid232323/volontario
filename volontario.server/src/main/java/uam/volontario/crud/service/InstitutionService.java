package uam.volontario.crud.service;

import uam.volontario.model.institution.impl.Institution;

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
}
