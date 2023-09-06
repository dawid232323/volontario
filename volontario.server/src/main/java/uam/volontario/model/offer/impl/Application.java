package uam.volontario.model.offer.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;
import uam.volontario.model.common.impl.User;

/**
 * Definition of volunteer's application.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "applications" )
public class Application implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @JsonManagedReference
    @ManyToOne
    @Nullable
    @JoinColumn( name = "volunteer_id" )
    private User volunteer;

    @JsonManagedReference
    @ManyToOne
    @Nullable
    @JoinColumn( name = "offer_id" )
    private Offer offer;

    @JsonManagedReference
    @ManyToOne
    @Nullable
    @JoinColumn( name = "state_id" )
    private ApplicationState state;

    @Column
    @NotBlank( message = "User must motivate his application." )
    @Size( max = 300, message = "Motivation must not exceed 300 letters." )
    private String participationMotivation;

    @Column
    @Size( max = 300, message = "Decision reason must not exceed 300 letters" )
    private String decisionReason;

    @Column
    private boolean isStarred;

    @JsonIgnore
    public ApplicationState getState()
    {
        return state;
    }

    public ApplicationStateEnum getStateAsEnum()
    {
        return ApplicationStateEnum.mapApplicationStateToApplicationStateEnum( state );
    }
}
