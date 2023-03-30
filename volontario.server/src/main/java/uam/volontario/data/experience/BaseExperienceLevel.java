package uam.volontario.data.experience;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uam.volontario.model.volunteer.impl.VolunteerExperience;

/**
 * Enum holding basic mappings for volunteer experience categories data.
 *
 * @deprecated needs to be removed ASAP when database topics are resolved.
 */
@RequiredArgsConstructor
@Deprecated
public enum BaseExperienceLevel
{

    NO_EXP( new VolunteerExperience( 1L, "Brak", "Brak historii uczestnictwa w wolontariacie" ) ),
    NOVICE( new VolunteerExperience( 2L, "Nowicjusz", "Uczestnictwo w jednej lub kilku akcjach w przeszłości" ) ),
    MEDIUM( new VolunteerExperience( 3L, "Średniozaawansowany", "Uczestnictwo w znacznej ilości akcji" ) ),
    VETERAN( new VolunteerExperience( 4L, "Weteran", "Bardzo duże doświadczenie i regularne uczestnictwo" +
            "w akcjach" ));

    @Getter
    private final VolunteerExperience volunteerExperience;
}
