package uam.volontario.model.common.impl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.common.UserIf;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.validation.annotation.DomainEmail;

/**
 * Basic implementation of {@linkplain UserIf}.
 */
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

    @Column
    @NotBlank( message = "Password must be defined" )
    private String hashedPassword; // TODO: add validation for password.

    @Column
    @Email( message = "Domain email has incorrect syntax" )
    @DomainEmail
    private String domainEmailAddress;

    @Column
    @Email( message = "Contact email has incorrect syntax" )
    @NotBlank( message = "Contact email must be defined" )
    private String contactEmailAddress;

    @Column
    @Pattern( regexp = "([0-9]{9})", message = "Phone number has incorrect syntax." )
    @NotBlank( message = "Phone number must be defined" )
    private String phoneNumber;

    @Column
    private boolean isVerified;

    @OneToOne( cascade = CascadeType.ALL )
    @JoinColumn
    @Nonnull
    private Role role;

    @Nullable
    @OneToOne( cascade = CascadeType.ALL )
    @JoinColumn
    private VolunteerData volunteerData;

    @Nullable
    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = "institution_id" )
    private Institution institution;
}
