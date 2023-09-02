package uam.volontario.dto.Institution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for {@linkplain uam.volontario.model.institution.impl.Institution}.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionDto
{
    private Long id;
    private String name;

    private InstitutionContactPersonDto contactPerson;

    private String krsNumber;

    // TODO: how to handle images in backend? Path?
//    private String pathToImage;

    private String headquartersAddress;

    private List< String > tags;

    private String description;

    private String localization;
}
