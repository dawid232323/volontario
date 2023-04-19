package uam.volontario.model.volunteer.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uam.volontario.model.common.VolontarioDomainElementIf;

/**
 * Definition of volunteer's experience.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "experience_level" )
public class ExperienceLevel implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "User needs to have experience defined" )
    private String name;

    @Size( max = 500 )
    @Column( length = 500 )
    private String definition;

    /**
     * Indication of how large experience is. Bigger the level, bigger the value.
     */
    @NotNull
    @Column
    private Long value;

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append( name )
                .append( value )
                .toHashCode();
    }

    @Override
    public boolean equals( final Object aObj )
    {
        if( aObj instanceof ExperienceLevel experienceLevel )
        {
            return new EqualsBuilder()
                    .append( this.name, experienceLevel.name )
                    .append( this.value, experienceLevel.value )
                    .isEquals();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Experience Level (id: " + id + ", name: " + name + ")";
    }
}
