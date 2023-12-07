package uam.volontario.dto.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandingPageDto
{
    private List<SectionDto> sections;
}
