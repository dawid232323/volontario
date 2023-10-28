package uam.volontario.dto.presence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.offer.impl.VoluntaryPresenceStateEnum;

import java.time.Instant;

/**
 * Data Transfer Object for {@linkplain uam.volontario.model.offer.impl.VoluntaryPresence}.
 */
@Data
@Builder( toBuilder = true )
@NoArgsConstructor
@AllArgsConstructor
public class VoluntaryPresenceVolunteerDataDto
{
    private VoluntaryPresenceStateEnum confirmationState;

    private boolean canDecisionBeChanged;

    private Instant decisionChangeDeadlineDate;
}
