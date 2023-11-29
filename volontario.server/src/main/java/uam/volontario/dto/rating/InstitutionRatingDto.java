package uam.volontario.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstitutionRatingDto
{
    private Long volunteerId;

    private Long offerId;

    private String offerName;

    private String volunteerName;

    private int rating;

    private String ratingComment;
}
