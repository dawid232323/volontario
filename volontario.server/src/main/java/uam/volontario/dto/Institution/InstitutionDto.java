package uam.volontario.dto.Institution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for {@linkplain uam.volontario.model.institution.impl.Institution}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionDto
{
    private String name;

    private InstitutionContactPersonDto contactPerson;

    private String krsNumber;

    // TODO: how to handle images in backend? Path?
//    private String pathToImage;

    private String headquartersAddress;

    private List< String > tags; // TODO: how to save it in DB? Different table? Perhaps comma separation? I dont think there is nice way with relational DBs.

    private String description;

    private String localization;
}
