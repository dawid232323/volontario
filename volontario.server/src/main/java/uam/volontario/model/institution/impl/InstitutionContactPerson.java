package uam.volontario.model.institution.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uam.volontario.model.common.VolontarioDomainElementIf;

/**
 * Definition of institution's data.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "institution_contact_people" )
public class InstitutionContactPerson implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @JsonBackReference
    @OneToOne( mappedBy = "institutionContactPerson" )
    @JoinColumn
    private Institution institution;

    @Column
    @NotBlank
    private String firstName;

    @Column
    @NotBlank
    private String lastName;

    @Column
    private String phoneNumber;

    @Column
    @Email
    private String contactEmail;

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append( contactEmail )
                .toHashCode();
    }

    @Override
    public boolean equals( final Object aObj )
    {
        if( aObj instanceof InstitutionContactPerson institutionContactPerson )
        {
            return new EqualsBuilder()
                    .append( this.contactEmail, institutionContactPerson.contactEmail )
                    .isEquals();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Institution Contact Person (id: " + id + ", contact email: " + contactEmail + ")";
    }
}
