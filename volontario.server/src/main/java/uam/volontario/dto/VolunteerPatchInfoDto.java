package uam.volontario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VolunteerPatchInfoDto
{
    private String contactEmailAddress;

    private String phoneNumber;

    private List< Long > interestCategoriesIds;

    private String participationMotivation;
}
