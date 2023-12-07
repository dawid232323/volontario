package uam.volontario.dto.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionDto
{
    private String id;
    private String title;
    private LandingPageSectionType type;
    private String content;
    private ImageCategory imageCategory;
    private List<TileDto> tiles;
}
