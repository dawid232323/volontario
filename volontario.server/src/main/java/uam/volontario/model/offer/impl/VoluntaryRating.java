package uam.volontario.model.offer.impl;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;

/**
 * Definition of ratings of Volunteer and Institution related to the Offer.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "voluntary_ratings" )
public class VoluntaryRating
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn( name = "volunteer_id" )
    private User volunteer;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn( name = "offer_id" )
    private Offer offer;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn( name = "institution_id" )
    private Institution institution;

    @Column
    @Min( value = 0, message = "Volunteer rating must not be lower than 0." )
    @Max( value = 5, message = "Volunteer rating must not be greater than 5." )
    private Integer volunteerRating;

    @Column
    @Nullable
    private String volunteerRatingReason;

    @Column
    @Min( value = 0, message = "Institution rating must not be lower than 0." )
    @Max( value = 5, message = "Institution rating must not be greater than 5." )
    private Integer institutionRating;

    @Column
    @Nullable
    private String institutionRatingReason;
}
