package uam.volontario.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPatchInfoDto
{
    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String contactEmailAddress;

    private String fieldOfStudy;

    private String participationMotivation;

    private List< Long > interestCategoriesIds;

}
