package uam.volontario.model.offer.impl;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class OfferSearchQuery
{
    private String title;
    private Long offerTypeId;
    private Date startDate;
    private Date endDate;
    private List<Long> interestCategoryIds;
    private String offerPlace;
    private Boolean isPoznanOnly;
    private Boolean isHidden;
    private Long institutionId;
    private Long contactPersonId;

    public OfferSearchQuery( String aTitle, Long aOfferTypeId, Date aStartDate, Date aEndDate, List<Long> aInterestCategoryIds,
                             String aOfferPlace, Boolean aIsPoznanOnly,
                             String aVisibility, Long aInstitutionId, Long aContactPersonId ) {
        this.title = aTitle;
        this.offerTypeId = aOfferTypeId;
        this.startDate = aStartDate;
        this.endDate = aEndDate;
        this.interestCategoryIds = aInterestCategoryIds;
        this.offerPlace = aOfferPlace;
        this.isPoznanOnly = aIsPoznanOnly;
        this.isHidden = resolveVisibility( aVisibility );
        this.institutionId = aInstitutionId;
        this.contactPersonId = aContactPersonId;
    }

    private Boolean resolveVisibility( String aVisibility )
    {
        if ( aVisibility == null )
        {
            return false;
        }
        return switch ( aVisibility )
        {
            case "active" -> false;

            case "hidden" -> true;

            case "all" -> null;

            default -> throw new IllegalArgumentException( aVisibility + " is not a defined visibility type param." );
        };
    }

}
