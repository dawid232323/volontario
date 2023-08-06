package uam.volontario.model.common.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchQuery
{
    private List<Long> roleIds;
    private String firstName;
    private String lastName;
    private String email;
}
