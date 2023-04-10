package uam.volontario.model.institution.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;
import uam.volontario.model.common.impl.User;

import java.util.List;

/**
 * Definition of institution's data.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Data
@Builder
@EqualsAndHashCode
@Entity
@Table( name = "institutions" )
public class Institution implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "Institution name must be defined." )
    private String name;

    @Column( length = 4000 )
    private String description;

    @Column
    @NotBlank( message = "Headquarters address of institution must be defined." )
    private String headquarters;

    @Column
    @NotBlank( message = "Localization of institution must be defined." )
    private String localization;

    @Pattern( regexp = "\\d{10}", message = "Wrong format of KRS number" )
    private String krsNumber;

    @OneToMany( mappedBy = "institution",
                cascade = { CascadeType.PERSIST, CascadeType.MERGE },
                fetch = FetchType.LAZY )
    private List< User > employees;

    @Column
    private String pathToImage; // TODO: look at InstitutionContactPersonDto.

    @Column
    private boolean isActive;
}
