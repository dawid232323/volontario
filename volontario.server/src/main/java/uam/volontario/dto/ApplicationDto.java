package uam.volontario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationDto
{
    private final Long volunteerId;
    private final Long offerId;
    private final String participationMotivation;
}