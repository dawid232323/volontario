package uam.volontario.model.common;

import org.springframework.security.core.GrantedAuthority;

/**
 * Role definition.
 */
public interface RoleIf extends VolontarioDomainElementIf, GrantedAuthority
{
    String getName();

    @Override
    default String getAuthority()
    {
        return getName();
    }
}
