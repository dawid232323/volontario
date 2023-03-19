package uam.volontario.model.volunteer.impl;

import jakarta.persistence.*;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;
import uam.volontario.model.common.impl.User;

import java.util.Set;

/**
 * Definition of volunteer's data.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table( name = "volunteer_data" )
public class VolunteerData implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @OneToOne
    @JoinColumn
    private User user;

    @Column( length = 1500 )
    private String participationMotivation;

    @OneToOne
    @JoinColumn
    private VolunteerExperience experience;

    @ManyToMany( cascade = { CascadeType.ALL } )
    @JoinTable( name = "volunteer_interests",
                joinColumns = { @JoinColumn( name = "volunteer_data_id" ) },
                inverseJoinColumns = { @JoinColumn( name = "interest_category_id" ) }
    )
    private Set< InterestCategory > interestCategories;
}
