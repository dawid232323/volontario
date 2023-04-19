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
    /**
     * Either domain email address, or contact email address, or phone number.
     */
    private String login;

    private String password;
}
