package uam.volontario.dto.Institution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for data related to registration of any Institution Employee.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionEmployeeDto
{
    private Long institutionId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String contactEmail;
}
