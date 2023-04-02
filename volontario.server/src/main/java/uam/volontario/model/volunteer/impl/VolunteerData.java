package uam.volontario.model.volunteer.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.common.VolontarioDomainElementIf;
import uam.volontario.model.common.impl.User;

import java.util.Set;

/**
 * Definition of volunteer's data.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Data
@Builder
@Entity
@Table( name = "volunteer_data" )
public class VolunteerData implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @JsonBackReference
    @OneToOne( mappedBy = "volunteerData")
    @JoinColumn
    private User user;

    @Size( max = 1500 )
    @Column( length = 1500 )
    private String participationMotivation;

    @ManyToOne
    @JoinColumn( name = "volunteer_experience_id" )
    private ExperienceLevel experience;

    @ManyToMany( cascade =  { CascadeType.PERSIST }  )
    @JoinTable( name = "volunteer_interests",
                joinColumns = { @JoinColumn( name = "volunteer_data_id" ) },
                inverseJoinColumns = { @JoinColumn( name = "interest_category_id" ) }
    )
    private Set< InterestCategory > interestCategories;

    @Override
    public String toString()
    {
        return "VolunteerData(id=" + id + ", participationMotivation=" + participationMotivation +", " +
                "experience=" + experience.toString() + ", owningUserId=" + user.getId() + ")";
    }
}
