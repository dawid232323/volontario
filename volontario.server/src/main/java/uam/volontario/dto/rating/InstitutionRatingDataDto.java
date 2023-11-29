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
public class InstitutionRatingDataDto
{
    private Double averageRating;
    private List< InstitutionRatingDto > institutionRatings;
}
