package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uam.volontario.crud.service.FileService;
import uam.volontario.dto.templates.LandingPageDto;

import java.io.IOException;

import static uam.volontario.crud.service.impl.file.FileServiceQualifiers.LANDING_PAGE_FILES_SERVICE;

@Component
public class ConfigurationHandler
{
    private final FileService< LandingPageDto, LandingPageDto > landingPageFileService;

    public ConfigurationHandler( @Qualifier( LANDING_PAGE_FILES_SERVICE )
                                 final FileService< LandingPageDto, LandingPageDto > aFileService )
    {
        landingPageFileService = aFileService;
    }

    public LandingPageDto resolveLandingPageData() throws IOException
    {
        return landingPageFileService.loadFile( null );
    }

    public LandingPageDto soreNewLandingPageConfiguration( final LandingPageDto configurationData ) throws IOException
    {
        this.landingPageFileService.saveFile( configurationData );
        return landingPageFileService.loadFile( null );
    }
}
