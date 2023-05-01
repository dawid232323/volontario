package uam.volontario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object for {@linkplain uam.volontario.model.offer.impl.Offer}.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto
{

    private String offerTitle;
    private Date offerExpirationDate;
    private Long contactPersonId;
    private Long offerTypeId;
    private Date startDate;
    private Date endDate;
    private List<Integer> offerWeekDays;
    private String durationUnit;
    private Long durationValue;
    private String offerInterval;
    private List<Long> interestCategoryIds;
    private Boolean isExperienceRequired;
    private Long experienceLevelId;
    private String offerDescription;
    private String offerPlace;
    private Boolean isPoznanOnly;
    private List<Long> offerBenefitIds;
    private Boolean isInsuranceNeeded;
}
