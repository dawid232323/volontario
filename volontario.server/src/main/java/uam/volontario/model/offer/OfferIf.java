package uam.volontario.model.offer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uam.volontario.model.common.VolontarioDomainElementIf;
import uam.volontario.model.offer.impl.OfferType;
import uam.volontario.model.offer.impl.OfferTypeEnum;

/**
 * Definition of offer in the system.
 */
public interface OfferIf extends VolontarioDomainElementIf
{
    @JsonIgnore
    OfferType getOfferType();

    OfferTypeEnum getOfferTypeAsEnum();
}
