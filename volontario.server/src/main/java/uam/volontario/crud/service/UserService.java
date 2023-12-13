package uam.volontario.crud.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import uam.volontario.crud.specification.UserSpecification;
import uam.volontario.model.common.impl.User;

import javax.swing.text.html.Option;
import java.util.List;
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

    /**
     * Loads paged and filtered user list
     * @param aSpecification query with filter params
     * @param aPageable page data
     * @return {@linkplain Page} with filtered and paged users
     */
    Page< User > findFiltered( UserSpecification aSpecification, Pageable aPageable );

    /**
     * Tries to load by user id.
     *
     * @param aUserId id of user to be loaded.
     *
     * @return user with given id or empty optional if user with given id is not found.
     */
    Optional< User > tryToFindById( final Long aUserId );

    /**
     * Saves or/and updates given Users.
     *
     * @param aUsers list of Users to save/update.
     *
     * @return list of saved/updated Users.
     */
    List< User > saveOrUpdateAll( final List< User > aUsers );

    /**
     * Tries to load currently logged user based on authentication principals.
     *
     * @return currently logged user or empty optional if user with given credentials does not appear in database
     */
    Optional< User > tryToGetLoggedUser();

    /**
     * Retrieves current user email based on token principal
     *
     * @return email String value
     */
    String getCurrentUserEmail();
}
