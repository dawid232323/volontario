package uam.volontario.model.offer.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.offer.OfferIf;
import uam.volontario.model.volunteer.impl.ExperienceLevel;
import uam.volontario.model.volunteer.impl.InterestCategory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Definition of volunteering offer.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "offers" )
public class Offer implements OfferIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column( length = 100 )
    @Size( max = 100, message = "Offer title cannot be longer than 100 characters" )
    private String title;

    @Column( length = 3000 )
    @Size( max = 3000, message = "Offer description must be 500 characters long at most." )
    private String description;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn( name = "contact_person_id" )
    private User contactPerson;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn( name = "assigned_moderator_id" )
    @Nullable
    private User assignedModerator;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn( name = "institution_id" )
    private Institution institution;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn( name = "offer_type_id" )
    private OfferType offerType;

    @Column
    @NotNull( message = "Offer's start date has to be defined." )
    private Instant startDate;

    @Column
    @Nullable
    private Instant endDate;

    @Column
    @NotNull( message = "Indication whether we need experience needs to be provided." )
    private Boolean isExperienceRequired;

    @JsonManagedReference
    @ManyToOne
    @Nullable
    @JoinColumn( name = "minimum_experience_id" )
    private ExperienceLevel minimumExperience;

    @JsonManagedReference
    @ManyToOne
    @Nullable
    @JoinColumn( name = "offer_state_id" )
    private OfferState offerState;

    @JsonManagedReference
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "offer_categories",
            joinColumns = { @JoinColumn( name = "offer_id" ) },
            inverseJoinColumns = { @JoinColumn( name = "interest_category_id" ) }
    )
    private List< InterestCategory > interestCategories;

    @JsonManagedReference
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "offer_benefits",
            joinColumns = { @JoinColumn( name = "offer_id" ) },
            inverseJoinColumns = { @JoinColumn( name = "benefit_id" ) }
    )
    private List< Benefit > benefits;

    @JsonIgnore
    @OneToMany( mappedBy = "offer", cascade = CascadeType.PERSIST )
    private List< Application > applications;

    @JsonIgnore
    @OneToMany( mappedBy = "offer", cascade = CascadeType.PERSIST )
    private List< VoluntaryPresence > voluntaryPresences;

    @JsonIgnore
    @OneToMany( mappedBy = "offer" )
    private List< VoluntaryRating > voluntaryRatings;

    @Column
    private Instant expirationDate;

    @Column
    private String periodicDescription;

    @Column
    private String place;

    @Column
    private Boolean isPoznanOnly;

    @Column
    private Boolean isHidden;

    @Column( length = 500 )
    @Size( max = 500, message = "Other categories must be under 500 characters long" )
    @Nullable
    private String otherCategories;

    @Column( length = 500 )
    @Size( max = 500, message = "Other benefits must be under 500 characters long" )
    @Nullable
    private String otherBenefits;

    public Instant getStartDate()
    {
        if( startDate != null )
        {
            return startDate.truncatedTo( ChronoUnit.SECONDS );
        }

        return null;
    }

    public Instant getEndDate()
    {
        if ( this.endDate == null ) {
            return null;
        }
        return endDate.truncatedTo( ChronoUnit.SECONDS );
    }

    public Instant getExpirationDate()
    {
        if( expirationDate != null )
        {
            return expirationDate.truncatedTo( ChronoUnit.SECONDS );
        }

        return null;
    }

    @Override
    public OfferTypeEnum getOfferTypeAsEnum()
    {
        return OfferTypeEnum.mapOfferTypeToOfferTypeEnum( offerType );
    }

    @Override
    public OfferStateEnum getOfferStateAsEnum()
    {
        return OfferStateEnum.mapOfferStateToOfferStateEnum( offerState );
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append( id )
                .toHashCode();
    }

    @Override
    public boolean equals( final Object aObj )
    {
        if( aObj instanceof Offer offer )
        {
            return new EqualsBuilder()
                    .append( this.id, offer.id )
                    .isEquals();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Offer (id: " + id + ")";
    }
}
