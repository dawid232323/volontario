package uam.volontario.model.offer.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;

/**
 * Definition of {@linkplain Application}'s state.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "application_states" )
public class ApplicationState implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "Application state must be defined." )
    private String name;
}
