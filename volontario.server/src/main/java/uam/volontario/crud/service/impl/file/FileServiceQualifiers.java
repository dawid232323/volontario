package uam.volontario.crud.service.impl.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

/**
 * Utility class that stores constants with file service qualifiers
 */
@NoArgsConstructor( access = AccessLevel.PRIVATE )
public final class FileServiceQualifiers
{
    public static final String PROFILE_PICTURE_FILES_SERVICE = "profilePicturesFileService";
    public static final String LANDING_PAGE_FILES_SERVICE = "landingPageFileService";
    public static final String REGULATIONS_FILE_SERVICE_QUALIFIER = "regulationsFileService";
}
