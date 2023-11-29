package uam.volontario.model.institution.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uam.volontario.model.common.VolontarioDomainElementIf;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.VoluntaryRating;

import java.util.List;

/**
 * Definition of institution's data.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder( toBuilder = true )
@Entity
@Table( name = "institutions" )
public class Institution implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "Institution name must be defined." )
    private String name;

    @Column( length = 4000 )
    private String description;

    @Column
    @NotBlank( message = "Headquarters address of institution must be defined." )
    private String headquarters;

    @Column
    @NotBlank( message = "Localization of institution must be defined." )
    private String localization;

    @Nullable
    @Pattern( regexp = "\\d{10}", message = "Wrong format of KRS number" )
    private String krsNumber;

    @JsonIgnore
    @OneToMany( mappedBy = "institution",
                cascade = { CascadeType.PERSIST, CascadeType.MERGE } )
    private List< User > employees;

    @JsonIgnore
    @OneToMany( mappedBy = "institution" )
    private List< Offer > offers;

    @JsonIgnore
    @OneToMany( mappedBy = "institution" )
    private List< VoluntaryRating > voluntaryRatings;

    @JsonManagedReference
    @OneToOne( cascade = { CascadeType.PERSIST, CascadeType.REMOVE } )
    @JoinColumn
    private InstitutionContactPerson institutionContactPerson;

    @Column
    private String pathToImage; // TODO: look at InstitutionContactPersonDto.

    @Column
    private boolean isActive;

    @Column
    private String registrationToken;

    @Column
    private String institutionTags;

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append( id )
                .toHashCode();
    }

    @Override
    public boolean equals( final Object aObj )
    {
        if( aObj instanceof Institution institution )
        {
            return new EqualsBuilder()
                    .append( this.id, institution.id )
                    .isEquals();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Institution (id: " + id + ", name: " + name + ", krs: " + krsNumber + ")";
    }
}
