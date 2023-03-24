package uam.volontario.model.volunteer.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.common.VolontarioDomainElementIf;

/**
 * Definition of volunteer's experience.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Data
@Builder
@Entity
@Table( name = "volunteer_experiences" )
public class VolunteerExperience implements VolontarioDomainElementIf
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
}
