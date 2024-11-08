package uam.volontario.model.offer.impl;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplicationSearchQuery
{
    private ApplicationStateEnum state;
    private Boolean isStarred;
    private Long offerId;
    private Long volunteerId;
    private Long institutionId;

    public ApplicationSearchQuery( String aState, Boolean isStarred, Long aOfferId, Long aVolunteerId,
                                   Long aInstitutionId )
    {
        this.state = resolveState( aState );
        this.isStarred = isStarred;
        this.offerId = aOfferId;
        this.volunteerId = aVolunteerId;
        this.institutionId = aInstitutionId;
    }

    private ApplicationStateEnum resolveState( String aState )
    {
        if ( aState == null )
        {
            return null;
        }
        return switch ( aState )
        {
            case "awaiting" -> ApplicationStateEnum.AWAITING;

            case "under recruitment" -> ApplicationStateEnum.UNDER_RECRUITMENT;

            case "declined" -> ApplicationStateEnum.DECLINED;

            case "reserve list" -> ApplicationStateEnum.RESERVE_LIST;

            default -> throw new IllegalArgumentException( aState + " is not a defined application state." );
        };
    }
}
