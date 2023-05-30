package uam.volontario.dto.Application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.dto.Offer.OfferBaseInfoDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationBaseInfoDto
{
    private Long id;
    private String firstName;
    private String lastName;
    private String participationMotivation;
    private boolean isStarred;
    private String state;
    private OfferBaseInfoDto offer;
}
