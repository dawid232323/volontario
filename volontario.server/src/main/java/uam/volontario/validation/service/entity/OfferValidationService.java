package uam.volontario.validation.service.entity;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.validation.service.AbstractValidationService;

@Service
@RequestScope
public class OfferValidationService extends AbstractValidationService< Offer >
{
    @Override
    protected void validateEntityByCustomConstraints( Offer aEntity )
    {
        this.doValidateOfferDates( aEntity );
    }

    @Override
    protected void postProcessValidation( final Offer aOffer )
    {
        // no post process validation for offer entity.
    }

    private void doValidateOfferDates( final Offer aOffer )
    {
        final String offerDatesKey = "offerDates";
        if ( aOffer.getEndDate().isBefore( aOffer.getExpirationDate() ) )
        {
            this.validationViolations.put( offerDatesKey,
                    "End date should not be earlier than Expiration date" );
        }
    }
}
