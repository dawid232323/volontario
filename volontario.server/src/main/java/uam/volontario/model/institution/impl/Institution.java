package uam.volontario.model.institution.impl;

import jakarta.persistence.*;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;
import uam.volontario.model.common.impl.User;

import java.util.List;

/**
 * Definition of institution's data.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
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
    private String name;

    @Column( length = 4000 )
    private String description;

    @Column
    private String headquarters;

    @Column
    private String localization;

    @Column( length = 10 )
    private String krsNumber;

    @OneToMany( mappedBy = "institution", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY )
    private List< User > employees;
}
