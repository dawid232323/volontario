package uam.volontario.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.volunteer.impl.InterestCategory;

import java.util.List;

@Data
@Builder( toBuilder = true )
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto
{
    private Long id;
    private String firstName;
    private String lastName;
    private String contactEmailAddress;
    private String phoneNumber;
    private String domainEmailAddress;
    private String participationMotivation;
    private String fieldOfStudy;
    private String interests;
    private String experienceDescription;
    private List<String> userRoles;
    private List< InterestCategory > interestCategories;
    private Long institutionId;
    private String institutionName;
}
