package uam.volontario.model.offer.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;

/**
 * Definition of {@linkplain VoluntaryPresence} state.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "voluntary_presence_states" )
public class VoluntaryPresenceState implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "Presence state must be defined." )
    private String state;
}
