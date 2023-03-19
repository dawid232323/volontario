package uam.volontario.model.common.impl;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import uam.volontario.model.common.UserIf;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.volunteer.impl.VolunteerData;

/**
 * Basic implementation of {@linkplain UserIf}.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table( name = "users" )
public class User implements UserIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String hashedPassword;

    @Column
    private String domainEmailAddress;

    @Column
    private String contactEmailAddress;

    @Column
    private String phoneNumber;

    @Column
    private boolean isVerified;

    @OneToOne
    @JoinColumn
    private Role role;

    @Nullable
    @OneToOne
    @JoinColumn
    private VolunteerData volunteerData;

    @Nullable
    @ManyToOne
    @JoinColumn( name = "institution_id" )
    private Institution institution;
}
