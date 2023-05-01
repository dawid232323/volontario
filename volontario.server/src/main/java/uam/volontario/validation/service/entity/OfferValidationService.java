package uam.volontario.validation.service.entity;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.OfferTypeEnum;
import uam.volontario.model.volunteer.impl.ExperienceLevel;
import uam.volontario.validation.service.AbstractValidationService;

@Service
@RequestScope
public class OfferValidationService extends AbstractValidationService< Offer >
{
    @Override
    protected void validateEntityByCustomConstraints( Offer aEntity )
    {
        final OfferTypeEnum offerTypeEnum = OfferTypeEnum
                .mapOfferTypeToOfferTypeEnum( aEntity.getOfferType() );
        this.doValidateOfferDates( offerTypeEnum, aEntity );
        this.doValidateOfferExperience( aEntity.getIsExperienceRequired(),
                aEntity.getMinimumExperience() );
        this.doValidateInterval( offerTypeEnum, aEntity.getOfferInterval() );
    }

    private void doValidateOfferDates( final OfferTypeEnum aOfferType, final Offer aOffer )
    {
        final String offerDatesKey = "offerDates";
        if ( aOfferType.equals( OfferTypeEnum.REGULAR ) && aOffer.getEndDate() != null )
        {
            this.validationViolations.put( offerDatesKey,
                    "End date should not be specified when offer type is regular" );
        }
        if ( !aOfferType.equals( OfferTypeEnum.REGULAR ) && aOffer.getEndDate() == null )
        {
            this.validationViolations.put( offerDatesKey,
                    "End date should be specified" );
        }
    }

    private void doValidateOfferExperience( final Boolean aIsExperienceRequired,
                                           final ExperienceLevel aOfferExperience )
    {
        final String experienceKey = "experience";
        if ( aIsExperienceRequired && aOfferExperience == null )
        {
            this.validationViolations.put( experienceKey, "Experience should be specified" );
        }
        if ( !aIsExperienceRequired && aOfferExperience != null )
        {
            this.validationViolations.put( experienceKey, "Experience should not be specified" );
        }
    }

    private void doValidateInterval( final OfferTypeEnum aOfferType,
                                     final String aInterval )
    {
        final String intervalKey = "interval";
        if ( aOfferType.equals( OfferTypeEnum.REGULAR ) && aInterval == null )
        {
            this.validationViolations.put( intervalKey,
                    "Interval shoud be specified" );
        }
        if ( !aOfferType.equals( OfferTypeEnum.REGULAR ) && aInterval != null )
        {
            this.validationViolations.put( intervalKey,
                    "Interval shoud not be specified" );
        }
    }
}
