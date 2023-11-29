package uam.volontario.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VolunteerRatingDataDto
{
    private Double averageRating;
    private List< VolunteerRatingDto > volunteerRatings;
}
