package uam.volontario.model.volunteer.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uam.volontario.model.common.VolontarioDomainElementIf;
import uam.volontario.model.common.impl.User;
import uam.volontario.validation.annotation.DomainEmail;

import java.util.List;
import java.util.Set;

/**
 * Definition of volunteer's data.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "volunteer_data" )
public class VolunteerData implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @JsonIgnore
    @OneToOne( mappedBy = "volunteerData" )
    @JoinColumn
    private User user;

    @Column
    @Email( message = "Domain email has incorrect syntax" )
    @DomainEmail
    private String domainEmailAddress;

    @Size( max = 1500 )
    @Column( length = 1500 )
    private String participationMotivation;

    @Size( max = 100 )
    @Column( length = 100 )
    private String fieldOfStudy;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn( name = "volunteer_experience_id" )
    private ExperienceLevel experience;

    @JsonManagedReference
    @ManyToMany( cascade =  { CascadeType.PERSIST }, fetch = FetchType.EAGER )
    @JoinTable( name = "volunteer_interests",
                joinColumns = { @JoinColumn( name = "volunteer_data_id" ) },
                inverseJoinColumns = { @JoinColumn( name = "interest_category_id" ) }
    )
    private List< InterestCategory > interestCategories;

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append( domainEmailAddress )
                .toHashCode();
    }

    @Override
    public boolean equals( final Object aObj )
    {
        if( aObj instanceof VolunteerData volunteerData )
        {
            return new EqualsBuilder()
                    .append( this.domainEmailAddress, volunteerData.domainEmailAddress )
                    .isEquals();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "VolunteerData (domain email: " + domainEmailAddress + ")";
    }
}
