package uam.volontario.model.offer.impl;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import uam.volontario.model.common.impl.User;

import java.time.Instant;

/**
 * Definition of volunteer's presence on given {@linkplain Offer}.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "voluntary_presences" )
public class VoluntaryPresence
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
    @ManyToOne( cascade = CascadeType.PERSIST )
    @Nullable
    @JoinColumn( name = "volunteer_reported_presence_state_id" )
    private VoluntaryPresenceState volunteerReportedPresenceState;

    @JsonManagedReference
    @ManyToOne( cascade = CascadeType.PERSIST )
    @Nullable
    @JoinColumn( name = "institution_reported_presence_state_id" )
    private VoluntaryPresenceState institutionReportedPresenceState;

    @Column
    private Instant volunteerReminderDate;

    @Column
    private int volunteerLeftReminderCount;

    @Column
    private boolean wasVolunteerReminded;

    @Column
    private Instant volunteerDecisionDate;

    @Column
    private Instant institutionReminderDate;

    @Column
    private int institutionLeftReminderCount;

    @Column
    private boolean wasInstitutionReminded;

    @Column
    private Instant institutionDecisionDate;

    /**
     * Returns 'volunteerReportedPresenceState' as corresponding instance of {@linkplain VoluntaryPresenceStateEnum}.
     *
     * @return corresponding enum.
     */
    public VoluntaryPresenceStateEnum getVolunteerReportedPresenceStateAsEnum()
    {
        return VoluntaryPresenceStateEnum.mapOfferTypeToOfferTypeEnum( volunteerReportedPresenceState );
    }

    /**
     * Returns 'institutionReportedPresenceState' as corresponding instance of {@linkplain VoluntaryPresenceStateEnum}.
     *
     * @return corresponding enum.
     */
    public VoluntaryPresenceStateEnum getInstitutionReportedPresenceStateAsEnum()
    {
        return VoluntaryPresenceStateEnum.mapOfferTypeToOfferTypeEnum( institutionReportedPresenceState );
    }

    /**
     * We consider Volunteer to have presence resolved when his presence was resolved by both him and Institution.
     *
     * @return true if presences were resolved, false otherwise.
     */
    public boolean isPresenceResolved()
    {
        return getInstitutionReportedPresenceStateAsEnum() != VoluntaryPresenceStateEnum.UNRESOLVED &&
                getVolunteerReportedPresenceStateAsEnum() != VoluntaryPresenceStateEnum.UNRESOLVED;
    }

    /**
     * We consider Volunteer to have presence confirmed when his presence was confirmed by both him and Institution.
     *
     * @return true if presences were confirmed, false otherwise.
     */
    public boolean isPresenceConfirmed()
    {
        return getInstitutionReportedPresenceStateAsEnum() == VoluntaryPresenceStateEnum.CONFIRMED &&
                getVolunteerReportedPresenceStateAsEnum() == VoluntaryPresenceStateEnum.CONFIRMED;
    }
}
