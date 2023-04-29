package uam.volontario.model.offer.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uam.volontario.model.common.VolontarioDomainElementIf;

import java.util.List;

/**
 * Definition of {@linkplain Offer} type.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "offer_types" )
public class OfferType implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotNull( message = "Offer type name must be defined." )
    @Size( max = 64, message = "Offer type name must not exceed 64 characters." )
    private String name;

    @JsonIgnore
    @OneToMany( mappedBy = "offerType" )
    private List< Offer > offers;

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append( name )
                .toHashCode();
    }

    @Override
    public boolean equals( final Object aObj )
    {
        if( aObj instanceof OfferType offerType )
        {
            return new EqualsBuilder()
                    .append( this.name, offerType.name )
                    .isEquals();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Offer type (name: " + name + ")";
    }
}
