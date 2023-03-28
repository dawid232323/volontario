package uam.volontario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for {@linkplain uam.volontario.model.volunteer.impl.InterestCategory}.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterestCategoryDto
{
    private Long id;

    private String name;

    private String description;
}
