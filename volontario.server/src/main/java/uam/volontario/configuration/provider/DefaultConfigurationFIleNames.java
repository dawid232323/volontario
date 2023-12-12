package uam.volontario.configuration.provider;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor( access = AccessLevel.PRIVATE )
public final class DefaultConfigurationFIleNames
{
    public static final String S3_CONFIGURATION_DIRECTORY = "configuration/";
    public static final String LANDING_PAGE_TITLE = "landing-page-template.json";
    public static final String USE_REGULATION_TITLE = "use-regulation.html";
    public static final String RODO_REGULATION_TITLE = "rodo-regulation.html";
}
