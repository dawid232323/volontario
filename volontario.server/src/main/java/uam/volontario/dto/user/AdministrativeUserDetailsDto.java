package uam.volontario.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.common.impl.Role;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministrativeUserDetailsDto
{
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private List< Role > userRoles;
    private Boolean verified;
}
