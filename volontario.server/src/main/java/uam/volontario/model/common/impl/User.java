package uam.volontario.model.common.impl;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import uam.volontario.model.common.UserIf;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.validation.annotation.DomainEmail;
import uam.volontario.validation.annotation.Password;

import java.util.Set;

/**
 * Basic implementation of {@linkplain UserIf}.
 */
@EqualsAndHashCode( exclude = {"volunteerData"} )
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Data
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
    private String hashedPassword;

    @Column
    @Email( message = "Domain email has incorrect syntax" )
    @DomainEmail
    private String domainEmailAddress;

    @Column
    @Email( message = "Contact email has incorrect syntax" )
    @NotBlank( message = "Contact email must be defined" )
    private String contactEmailAddress;

    @Column // TODO: phone number validation needs to be rethought.
    private String phoneNumber;

    @Column
    private boolean isVerified;


    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "user_roles",
            joinColumns = { @JoinColumn( name = "user_id" ) },
            inverseJoinColumns = { @JoinColumn( name = "role_id" ) }
    )
    private Set< Role > roles;

    @JsonManagedReference
    @Nullable
    @OneToOne( cascade = CascadeType.ALL )
    @JoinColumn
    private VolunteerData volunteerData;

    @Nullable
    @ManyToOne
    @JoinColumn( name = "institution_id" )
    private Institution institution;
}
