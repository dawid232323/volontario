package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.RoleRepository;
import uam.volontario.crud.service.RoleService;
import uam.volontario.model.common.impl.Role;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation of {@linkplain RoleService}.
 */
@Service
public class RoleServiceImpl implements RoleService
{
    private final RoleRepository roleRepository;

    /**
     * CDI constructor.
     *
     * @param aRoleRepository role repository.
     */
    @Autowired
    public RoleServiceImpl( final RoleRepository aRoleRepository )
    {
        roleRepository = aRoleRepository;
    }

    @Override
    public Role loadEntity( final Long aRoleId ) throws NoResultException
    {
        return tryLoadEntity( aRoleId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< Role > tryLoadEntity( final Long aRoleId )
    {
        return roleRepository.findById( aRoleId );
    }

    @Override
    public List< Role > loadAllEntities()
    {
        return Lists.newArrayList( roleRepository.findAll() );
    }

    @Override
    public Role saveOrUpdate( final Role aRole )
    {
        return roleRepository.save( aRole );
    }

    @Override
    public void deleteEntity( final Long aRoleId )
    {
        roleRepository.deleteById( aRoleId );
    }

    @Override
    public List< Role > findByNameIn( final List< String > aNames ) throws NoResultException
    {
        return roleRepository.findByNameIn( aNames );
    }

    @Override
    public List< Role > findByIdIn( final List< Long > aIds )
    {
        return this.roleRepository.findByIdIn( aIds );
    }
}
