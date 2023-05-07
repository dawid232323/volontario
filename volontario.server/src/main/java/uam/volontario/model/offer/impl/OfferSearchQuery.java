package uam.volontario.model.offer.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferSearchQuery
{
    private String title;
    private Long offerTypeId;
    private Date startDate;
    private Date endDate;
    private List<Long> interestCategoryIds;
    private List<Integer> offerWeekDays;
    private String offerPlace;
    private Long experienceLevelId;
    private Boolean isPoznanOnly;
    private Boolean isInsuranceNeeded;
    private Long institutionId;
    private Long contactPersonId;
}
