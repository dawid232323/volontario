package uam.volontario.model.configuration;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import uam.volontario.model.common.VolontarioDomainElementIf;

/**
 * Definition of Configuration Entry.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Getter
@Setter
@Builder
@Entity
@Table( name = "configuration_entries" )
public class ConfigurationEntry implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column( length = 150 )
    @Size( max = 150, message = "Configuration Entry's key cannot be longer than 150 characters" )
    private String key;

    @Column( length = 10_000 )
    @Size( max = 10_000, message = "Configuration Entry's value cannot be longer than 10 000 characters." )
    private String value;

    @Column( length = 300 )
    @Size( max = 300, message = "Configuration Entry's description cannot be longer than 300 characters." )
    private String description;
}
