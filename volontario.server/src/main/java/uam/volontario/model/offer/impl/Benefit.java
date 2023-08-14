package uam.volontario.model.offer.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uam.volontario.model.common.SoftDeletable;
import uam.volontario.model.common.VolontarioDomainElementIf;

import java.util.List;

/**
 * Definition of offer's benefit.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "benefits" )
public class Benefit implements VolontarioDomainElementIf, SoftDeletable
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotNull( message = "Benefit name must be defined." )
    @Size( max = 250, message = "Benefit name must not exceed 250 characters." )
    private String name;

    @JsonIgnore
    @ManyToMany( mappedBy = "benefits" )
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
        if( aObj instanceof Benefit benefit )
        {
            return new EqualsBuilder()
                    .append( this.name, benefit.name )
                    .isEquals();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Benefit (name: " + name + ")";
    }
}
