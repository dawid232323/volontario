package uam.volontario.model.volunteer.impl;

import jakarta.persistence.*;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;

/**
 * Definition of volunteer's experience.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table( name = "volunteer_experiences" )
public class VolunteerExperience implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    private String name;

    @Column( length = 500 )
    private String definition;
}
