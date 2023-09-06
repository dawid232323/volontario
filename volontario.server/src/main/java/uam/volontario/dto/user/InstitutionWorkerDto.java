package uam.volontario.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionWorkerDto
{
    private Long id;
    private String firstName;
    private String lastName;
    private Long institutionId;
    private String institutionName;
}
