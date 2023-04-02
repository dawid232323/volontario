package uam.volontario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.volunteer.impl.ExperienceLevel;

/**
 * Data Transfer Object for {@linkplain ExperienceLevel}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceLevelDto
{
    private String name;

    private String definition;

    private Long value;
}
