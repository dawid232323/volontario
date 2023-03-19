package uam.volontario.model.volunteer.impl;

import jakarta.persistence.*;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;

import java.util.Set;

/**
 * Definition of interest category.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table( name = "interest_categories" )
public class InterestCategory implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    private String name;

    @Column( length = 750 )
    private String description;

    @ManyToMany( mappedBy = "interestCategories" )
    private Set< VolunteerData > volunteerData;
}
