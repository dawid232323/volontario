package uam.volontario.dto.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegulationsDto
{
    private String useRegulation;
    private String rodoRegulation;
}
