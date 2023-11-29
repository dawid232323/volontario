package uam.volontario.model.common.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uam.volontario.model.common.UserIf;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.VoluntaryPresence;
import uam.volontario.model.offer.impl.VoluntaryRating;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.validation.annotation.Password;

import java.time.Instant;
import java.util.List;

/**
 * Basic implementation of {@linkplain UserIf}.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "users" )
public class User implements UserIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "First name must be defined" )
    private String firstName;

    @Column
    @NotBlank( message = "Last name must be defined" )
    private String lastName;

    @Transient
    @Password
    private String password;

    @Column
    @JsonIgnore
    private String hashedPassword;

    @Column
    @Email( message = "Contact email has incorrect syntax" )
    @NotBlank( message = "Contact email must be defined" )
    private String contactEmailAddress;

    @Column // TODO: phone number validation needs to be rethought.
    private String phoneNumber;

    @Column
    private boolean isVerified;

    @JsonManagedReference
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "user_roles",
            joinColumns = { @JoinColumn( name = "user_id" ) },
            inverseJoinColumns = { @JoinColumn( name = "role_id" ) }
    )
    private List< Role > roles;

    @Nullable
    @JsonManagedReference
    @OneToOne( cascade = CascadeType.ALL )
    @JoinColumn
    private VolunteerData volunteerData;

    @Nullable
    @JsonManagedReference
    @ManyToOne
    @JoinColumn( name = "institution_id" )
    private Institution institution;

    @Column
    @NotNull
    private Instant creationDate;

    @Column
    private String pathToImage;

    @JsonIgnore
    @OneToMany( mappedBy = "volunteer", cascade = CascadeType.PERSIST )
    private List< VoluntaryPresence > voluntaryPresences;

    @JsonIgnore
    @OneToMany( mappedBy = "volunteer", cascade = CascadeType.PERSIST )
    private List< Application > applications;

    @JsonIgnore
    @OneToMany( mappedBy = "volunteer" )
    private List< VoluntaryRating > voluntaryRatings;

    @Override
    public List< UserRole > getUserRoles()
    {
        return UserRole.mapRolesToUserRoles( roles );
    }

    @Override
    public boolean hasUserRole( final UserRole aUserRole )
    {
        return getUserRoles().stream()
                .anyMatch( userRole -> userRole.equals( aUserRole ) );
    }

    public boolean hasAssignedPicture()
    {
        return this.getPathToImage() != null && !StringUtils.isBlank( this.getPathToImage() );
    }

    public String getFullName()
    {
        return getFirstName().concat( " " ).concat( getLastName() );
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append( contactEmailAddress )
                .toHashCode();
    }

    @Override
    public boolean equals( final Object aObj )
    {
        if( aObj instanceof User user )
        {
            return new EqualsBuilder()
                    .append( this.contactEmailAddress, user.contactEmailAddress )
                    .isEquals();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "User (id: " + id + ", contact email: " + contactEmailAddress + ")";
    }
}
