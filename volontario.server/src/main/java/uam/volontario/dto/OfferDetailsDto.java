package uam.volontario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDetailsDto
{
    private Long id;
    private String offerTitle;
    private Date offerExpirationDate;
    private InstitutionContactPersonDto contactPerson;
    private OfferTypeDto offerType;
    private Date startDate;
    private Date endDate;
    private List< Integer > offerWeekDays;
    private String offerInterval;
    private List< InterestCategoryDto > interestCategories;
    private Boolean isExperienceRequired;
    private ExperienceLevelDto experienceLevel;
    private String offerDescription;
    private String offerPlace;
    private Boolean isPoznanOnly;
    private List< BenefitDto > offerBenefits;
    private Boolean isInsuranceNeeded;
}
