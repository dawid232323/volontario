package uam.volontario.model.institution.impl;

import jakarta.persistence.*;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;

/**
 * Definition of institution's data.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Data
@Builder
@EqualsAndHashCode
@Entity
@Table( name = "institution_contact_people" )
public class InstitutionContactPerson implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @OneToOne( mappedBy = "institutionContactPerson" )
    @JoinColumn
    private Institution institution;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String phoneNumber;

    @Column
    private String contactEmail;
}
