package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.UserRepository;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.impl.User;

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
    public Optional< User > tryToLoadByDomainEmail( final String aDomainEmail )
    {
        return userRepository.findByDomainEmailAddress( aDomainEmail );
    }

    /**
     * Method needed for Spring Security integration. Domain email address is username in Volontario system.
     *
     * @param aDomainEmail domain email address (username).
     *
     * @return userDetails of found User.
     *
     * @throws UsernameNotFoundException when there is no user with such domain email in the system.
     */
    @Override
    public UserDetails loadUserByUsername( final String aDomainEmail ) throws UsernameNotFoundException
    {
        return tryToLoadByDomainEmail( aDomainEmail )
                .orElseThrow( () -> new UsernameNotFoundException( "Domain email " + aDomainEmail
                        + " was not found in the system." ) );
    }
}
