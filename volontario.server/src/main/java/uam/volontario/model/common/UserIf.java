package uam.volontario.model.common;

import uam.volontario.model.common.impl.Role;

/**
 * User definition.
 */
public interface UserIf extends VolontarioDomainElementIf
{
    String getFirstName();

    String getLastName();

    String getHashedPassword();

    String getDomainEmailAddress();

    String getContactEmailAddress();

    String getPhoneNumber();

    boolean isVerified();

    Role getRole();
}
