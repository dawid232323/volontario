package uam.volontario.model.volunteer.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uam.volontario.model.common.VolontarioDomainElementIf;

import java.util.List;
import java.util.Set;

/**
 * Definition of interest category.
 */
@AllArgsConstructor
@NoArgsConstructor // for Hibernate.
@Data
@Builder
@Entity
@Table( name = "interest_categories" )
public class InterestCategory implements VolontarioDomainElementIf
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column
    private Long id;

    @Column
    @NotBlank( message = "Interest category must be named" )
    private String name;

    @Column( length = 750 )
    @Size( max = 750 )
    private String description;

    @JsonIgnore
    @ManyToMany( mappedBy = "interestCategories", fetch = FetchType.LAZY )
    private List< VolunteerData > volunteerData;
}
