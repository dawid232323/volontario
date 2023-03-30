package uam.volontario.data.interests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uam.volontario.model.volunteer.impl.InterestCategory;

import java.util.HashSet;

/**
 * Enum holding basic mappings for volunteer interest categories data.
 *
 * @deprecated needs to be removed ASAP when database topics are resolved.
 */
@RequiredArgsConstructor
@Deprecated
public enum BaseInterestType
{
    ELDERDLY( new InterestCategory(1L, "Praca z osobami starszymi", "Praca z osobami starszymi", new HashSet<>()) ),
    CHILDREN( new InterestCategory(2L, "Praca z dziećmi", "Praca z dziećmi", new HashSet<>()) ),
    EXCLUDED( new InterestCategory( 3L, "Praca z osobami wykluczonymi",
            "Pomoc osobom biednym, bezdomnym etc.", new HashSet<>() ) ),
    PHYSICALLY_DISABLED(new InterestCategory( 4L, "Praca z osobami niepełnosprawnymi fizycznie",
            "Praca z osobami niepełnosprawnymi fizycznie", new HashSet<>() )),
    MENTALLY_DISABLED(new InterestCategory( 5L, "Praca z osobami niepełnosprawnymi intelektualnie",
            "Praca z osobami niepełnosprawnymi intelektualnie", new HashSet<>() )),
    ANIMALS(new InterestCategory( 4L, "Praca ze zwierzętami",
            "Praca ze zwierzętami", new HashSet<>() )),
    ORGANIZATIONAL( new InterestCategory( 6L, "Pomoc organizacyjna przy wydarzeniach",
            "Pomoc organizacyjna przy wydarzeniach", new HashSet<>() ) ),
    KNOWLEDGE_SHARING( new InterestCategory( 7L, "Dzielenie się wiedzą",
            "Dzielenie się wiedzą", new HashSet<>() ));

    @Getter
    private final InterestCategory interestCategory;
}
