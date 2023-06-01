package uam.volontario.dto.Offer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.dto.BenefitDto;
import uam.volontario.dto.ExperienceLevelDto;
import uam.volontario.dto.Institution.InstitutionContactPersonDto;
import uam.volontario.dto.Institution.InterestCategoryDto;

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
    private List<InterestCategoryDto> interestCategories;
    private Boolean isExperienceRequired;
    private ExperienceLevelDto experienceLevel;
    private String offerDescription;
    private String offerPlace;
    private String periodicDescription;
    private Boolean isPoznanOnly;
    private List< BenefitDto > offerBenefits;
    private Long institutionId;
    private String institutionName;
}
