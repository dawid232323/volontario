package uam.volontario.dto.presence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Data Transfer Object for {@linkplain uam.volontario.model.offer.impl.VoluntaryPresence}.
 */
@Data
@Builder( toBuilder = true )
@NoArgsConstructor
@AllArgsConstructor
public class VoluntaryPresenceInstitutionDataDto
{
    private boolean wasPresenceConfirmed;

    private boolean canDecisionBeChanged;

    private boolean canPostponeReminder;

    private Instant decisionChangeDeadlineDate;
}
