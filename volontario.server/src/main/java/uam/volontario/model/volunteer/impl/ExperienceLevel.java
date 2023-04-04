package uam.volontario.model.volunteer.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
}
