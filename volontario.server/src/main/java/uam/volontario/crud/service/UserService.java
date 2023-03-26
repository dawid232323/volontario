package uam.volontario.crud.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import uam.volontario.model.common.impl.User;

import java.util.Optional;

/**
 * Injection interface for {@linkplain User}'s service.
 */
public interface UserService extends EntityService< User >, UserDetailsService
{
    /**
     * Tries to load user by domain email address.
     *
     * @param aDomainEmail domain email address.
     *
     * @return user with given domain email or empty optional if given domain email does not belong to any user.
     */
    Optional< User > tryToLoadByDomainEmail( String aDomainEmail );
}
