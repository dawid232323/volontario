package uam.volontario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for {@linkplain uam.volontario.model.common.impl.User} of Volunteer role.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VolunteerDto
{
    private String firstName;

    private String lastName;

    private String password;

    private String domainEmail;

    private String contactEmail;

    private String phoneNumber;

    private String participationMotivation;

    private VolunteerExperienceDto experience;

    private List< InterestCategoriesDto > interestCategories;
}
