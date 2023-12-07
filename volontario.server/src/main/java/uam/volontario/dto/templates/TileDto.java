package uam.volontario.dto.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TileDto
{
    private String title;
    private String stepContent;
    private String stepIcon;
}
