package uam.volontario.model.offer.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;

/**
 * Definition of offer's benefit.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "offer_states" )
public class OfferState implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "Offer state must be defined." )
    private String state;
}
