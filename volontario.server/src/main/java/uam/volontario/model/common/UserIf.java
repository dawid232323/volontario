package uam.volontario.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uam.volontario.model.common.impl.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User definition.
 */
public interface UserIf extends VolontarioDomainElementIf, UserDetails
{
    String getFirstName();

    String getLastName();

    String getHashedPassword();

    String getContactEmailAddress();

    String getPhoneNumber();

    boolean isVerified();

    @JsonIgnore
    List< Role > getRoles();

    List< UserRole > getUserRoles();

    boolean hasUserRole( UserRole aUserRole );

    @Override
    @JsonIgnore
    default Collection< ? extends GrantedAuthority > getAuthorities()
    {
       return getRoles();
    }

    @Override
    @JsonIgnore
    default String getPassword()
    {
        return getHashedPassword();
    }

    @Override
    @JsonIgnore
    default String getUsername()
    {
        return getContactEmailAddress();
    }

    @Override
    @JsonIgnore
    default boolean isAccountNonExpired()
    {
        return true; // TODO: decision must be made whether we want users' account to be expire at some point.
    }

    @Override
    @JsonIgnore
    default boolean isAccountNonLocked()
    {
        return true; // TODO: decision must be made whether we want users' account to be locked under some circumstances.
    }

    @Override
    @JsonIgnore
    default boolean isCredentialsNonExpired()
    {
        return true; // TODO: decision must be made whether we want users' credentials to be expire at some point.
    }

    @Override
    @JsonIgnore
    default boolean isEnabled()
    {
        return isVerified();
    }
}
