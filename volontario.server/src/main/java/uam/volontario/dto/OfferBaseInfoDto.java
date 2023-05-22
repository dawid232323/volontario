package uam.volontario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object for {@linkplain uam.volontario.model.offer.impl.Offer} containing only basic information.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferBaseInfoDto
{
    private Long id;
    private String offerTitle;
    private Date offerExpirationDate;
    private String offerTypeName;
    private Date startDate;
    private Date endDate;
    private String offerPlace;
    private String institutionName;
    private Boolean isPoznanOnly;
}
