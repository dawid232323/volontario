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
        this.doValidateOfferDates( aEntity );
        this.doValidateOfferExperience( aEntity.getIsExperienceRequired(),
                aEntity.getMinimumExperience() );
    }

    private void doValidateOfferDates( final Offer aOffer )
    {
        final String offerDatesKey = "offerDates";
        if ( aOffer.getExpirationDate().isBefore( aOffer.getEndDate() ) )
        {
            this.validationViolations.put( offerDatesKey,
                    "Expiration date should not be earlier than end date" );
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
}
