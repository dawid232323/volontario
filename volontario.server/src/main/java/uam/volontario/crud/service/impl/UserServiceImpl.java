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
    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadEntity( final Long aEntityId )
    {
        return userRepository.findById( aEntityId )
                    .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< User > tryLoadEntity( final Long aEntityId )
    {
        return userRepository.findById( aEntityId );
    }

    @Override
    public List< User > loadAllEntities()
    {
        return Lists.newArrayList( userRepository.findAll() );
    }

    @Override
    public User saveOrUpdate( final User aEntity )
    {
        return userRepository.save( aEntity );
    }

    @Override
    public void deleteEntity( final Long aEntityId )
    {
        userRepository.deleteById( aEntityId );
    }

    @Override
    public Optional< User > tryToLoadByDomainEmail( final String aDomainEmail )
    {
        return userRepository.findByDomainEmailAddress( aDomainEmail );
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
    {
        return tryToLoadByDomainEmail( username ).orElseThrow( () -> new UsernameNotFoundException( "not found" ) );
    }
}
