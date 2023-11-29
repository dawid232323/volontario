package uam.volontario.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VolunteerRatingDto
{
    private Long volunteerId;

    private Long offerId;

    private String offerName;

    private String institutionName;

    private Long contactPersonId;

    private String contactPersonName;

    private int rating;

    private String ratingComment;
}
