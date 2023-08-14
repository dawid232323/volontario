package uam.volontario.model.volunteer.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uam.volontario.model.common.SoftDeletable;
import uam.volontario.model.common.VolontarioDomainElementIf;
import uam.volontario.model.offer.impl.Offer;

import java.util.List;

/**
 * Definition of interest category.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "interest_categories" )
public class InterestCategory implements VolontarioDomainElementIf, SoftDeletable
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "Interest category must be named" )
    private String name;

    @Column( length = 750 )
    @Size( max = 750 )
    private String description;

    @JsonIgnore
    @ManyToMany( mappedBy = "interestCategories" )
    private List< VolunteerData > volunteerData;

    @JsonIgnore
    @ManyToMany( mappedBy = "interestCategories" )
    private List< Offer > offers;

    @Column
    private boolean isUsed;

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
        if( aObj instanceof InterestCategory interestCategory )
        {
            return new EqualsBuilder()
                    .append( this.name, interestCategory.name )
                    .isEquals();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Interest Category (id: " + id + ", name: " + name +")";
    }
}
