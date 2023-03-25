package uam.volontario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Login data.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto
{
    private String domainEmailAddress; // TODO: perhaps we will enable logging in from phoneNumber?

    private String password;
}
