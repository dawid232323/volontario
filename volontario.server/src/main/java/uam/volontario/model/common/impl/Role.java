package uam.volontario.model.common.impl;

import jakarta.persistence.*;
import lombok.*;
import uam.volontario.model.common.RoleIf;

/**
 * Basic implementation of {@linkplain RoleIf}.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table( name = "roles" )
public class Role implements RoleIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    private String name;
}
