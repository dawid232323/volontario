package uam.volontario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for {@linkplain uam.volontario.model.volunteer.impl.VolunteerExperience}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerExperienceDto
{
    private String name;

    private String definition;
}
