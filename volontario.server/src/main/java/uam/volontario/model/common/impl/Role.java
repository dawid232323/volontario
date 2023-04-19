package uam.volontario.model.common.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uam.volontario.model.common.RoleIf;
import uam.volontario.model.volunteer.impl.ExperienceLevel;

import java.util.List;
import java.util.Set;

/**
 * Basic implementation of {@linkplain RoleIf}.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "roles" )
public class Role implements RoleIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "User must have defined role" )
    private String name;

    @JsonIgnore
    @ManyToMany( mappedBy = "roles", fetch = FetchType.LAZY )
    private List< User > users;

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
        if( aObj instanceof Role role )
        {
            return new EqualsBuilder()
                    .append( this.name, role.name )
                    .isEquals();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Role (name: " + name + ")";
    }
}
