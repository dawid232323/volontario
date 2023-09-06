package uam.volontario.dto.Application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.dto.ExperienceLevelDto;
import uam.volontario.dto.Institution.InterestCategoryDto;
import uam.volontario.dto.Offer.OfferBaseInfoDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDetailsDto
{
    private Long id;
    private String state;
    private String firstName;
    private String lastName;
    private String contactEmail;
    private String domainEmail;
    private String phoneNumber;
    private ExperienceLevelDto experienceLevel;
    private String participationMotivation;
    private List< InterestCategoryDto > interestCategories;
    private OfferBaseInfoDto offerInfo;
    private boolean isStarred;
    private Long assignedPersonId;
    private String decisionReason;
}
