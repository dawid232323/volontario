package uam.volontario.model.common;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uam.volontario.model.common.impl.Role;

import java.util.Collection;
import java.util.Set;

/**
 * User definition.
 */
public interface UserIf extends VolontarioDomainElementIf, UserDetails
{
    String getFirstName();

    String getLastName();

    String getHashedPassword();

    String getDomainEmailAddress();

    String getContactEmailAddress();

    String getPhoneNumber();

    boolean isVerified();

    Set< Role > getRoles();

    @Override
    default Collection< ? extends GrantedAuthority > getAuthorities()
    {
       return getRoles();
    }

    @Override
    default String getPassword()
    {
        return getHashedPassword();
    }

    @Override
    default String getUsername()
    {
        return getDomainEmailAddress();
    }

    @Override
    default boolean isAccountNonExpired()
    {
        return true; // TODO: decision must be made whether we want users' account to be expire at some point.
    }

    @Override
    default boolean isAccountNonLocked()
    {
        return true; // TODO: decision must be made whether we want users' account to be locked under some circumstances.
    }

    @Override
    default boolean isCredentialsNonExpired()
    {
        return true; // TODO: decision must be made whether we want users' credentials to be expire at some point.
    }

    @Override
    default boolean isEnabled()
    {
        return isVerified();
    }
}
