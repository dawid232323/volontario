package uam.volontario.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.volunteer.impl.VolunteerData;

import java.util.List;

/**
 * Used to transfer user data with possible JWT changes.
 * */
@Data
@Builder( toBuilder = true )
@NoArgsConstructor
@AllArgsConstructor
public class UserWithJwtDto
{
    private Long id;
    private String firstName;
    private String lastName;
    private String contactEmailAddress;
    private String phoneNumber;
    private String domainEmailAddress;
    private VolunteerData volunteerData;
    private Institution institution;
    private List< UserRole > roles;
    private String token;
    private String refreshToken;
}

