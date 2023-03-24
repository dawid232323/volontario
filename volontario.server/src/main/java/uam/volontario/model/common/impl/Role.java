package uam.volontario.model.common.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.common.RoleIf;

/**
 * Basic implementation of {@linkplain RoleIf}.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Data
@Builder
@Entity
@Table( name = "roles" )
public class Role implements RoleIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "User must have defined role" )
    private String name;
}
