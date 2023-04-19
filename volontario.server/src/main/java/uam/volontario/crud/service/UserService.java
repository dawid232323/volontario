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
     * Tries to load user by contact email address.
     *
     * @param aContactEmail contact email address.
     *
     * @return user with given contact email or empty optional if given contact email does not belong to any user.
     */
    Optional< User > tryToLoadByContactEmail( String aContactEmail );

    /**
     * Tries to load by login.
     *
     * @param aLogin either domain email, contact email or phone number
     *
     * @return user matching login or empty optional if given login does not belong to any user.
     */
    Optional< User > tryToLoadByLogin( String aLogin );
}
