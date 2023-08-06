package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.UserRepository;
import uam.volontario.crud.service.UserService;
import uam.volontario.crud.specification.UserSpecification;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.common.impl.UserSearchQuery;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation of {@linkplain UserService}.
 */
@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    /**
     * CDI constructor.
     *
     * @param aUserRepository user repository.
     */
    @Autowired
    public UserServiceImpl( final UserRepository aUserRepository )
    {
        userRepository = aUserRepository;
    }

    @Override
    public User loadEntity( final Long aUserId )
    {
        return tryLoadEntity( aUserId )
                    .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< User > tryLoadEntity( final Long aUserId )
    {
        return userRepository.findById( aUserId );
    }

    @Override
    public List< User > loadAllEntities()
    {
        return Lists.newArrayList( userRepository.findAll() );
    }

    @Override
    public User saveOrUpdate( final User aUser )
    {
        return userRepository.save( aUser );
    }

    @Override
    public void deleteEntity( final Long aUserId )
    {
        userRepository.deleteById( aUserId );
    }

    @Override
    public Optional< User > tryToLoadByContactEmail( final String aContactEmail )
    {
        return userRepository.findByContactEmailAddress( aContactEmail );
    }

    @Override
    public Optional< User > tryToLoadByLogin( final String aLogin )
    {
        Optional< User > user = tryToLoadByContactEmail( aLogin );

        if( user.isPresent() )
        {
            return user;
        }

        return userRepository.findByPhoneNumber( aLogin );
    }

    /**
     * Method needed for Spring Security integration. In Volontario system we consider contact email address as
     * username (although we can also log in with phone number).
     *
     * @param aContactEmail contact email address (username).
     *
     * @return userDetails of found User.
     *
     * @throws UsernameNotFoundException when there is no user with such contact email in the system.
     */
    @Override
    public UserDetails loadUserByUsername( final String aContactEmail ) throws UsernameNotFoundException
    {
        return tryToLoadByContactEmail( aContactEmail )
                .orElseThrow( () -> new UsernameNotFoundException( "Contact email " + aContactEmail
                        + " was not found in the system." ) );
    }

    @Override
    public Page< User > findFiltered( final UserSpecification aUserSpecification, final Pageable aPageable )
    {
        return this.userRepository.findAll( aUserSpecification, aPageable );
    }

    @Override
    public Optional< User > tryToFindById( final Long aUserId )
    {
        return this.userRepository.findById( aUserId );
    }
}
