package uam.volontario.dto.Institution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for data related to person
 * creating {@linkplain uam.volontario.model.institution.impl.Institution}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionContactPersonDto
{
    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String contactEmail;
}
