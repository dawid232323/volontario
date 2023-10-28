package uam.volontario.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.offer.impl.VoluntaryPresenceStateEnum;

/**
 * DTO representing list of ids which belong to Volunteers.
 */
@Data
@Builder( toBuilder = true )
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerPresenceDto
{
    private Long volunteerId;

    private VoluntaryPresenceStateEnum presenceState;
}
