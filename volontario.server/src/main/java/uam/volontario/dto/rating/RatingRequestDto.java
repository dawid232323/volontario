package uam.volontario.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingRequestDto
{
    private Long volunteerId;

    private Long offerId;

    private Integer rating;

    private String ratingReason;
}
