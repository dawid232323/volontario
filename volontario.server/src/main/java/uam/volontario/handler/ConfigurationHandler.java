package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uam.volontario.crud.service.FileService;
import uam.volontario.dto.templates.LandingPageDto;
import uam.volontario.dto.templates.RegulationsDto;

import java.io.IOException;

import static uam.volontario.configuration.provider.DefaultConfigurationFIleNames.RODO_REGULATION_TITLE;
import static uam.volontario.configuration.provider.DefaultConfigurationFIleNames.USE_REGULATION_TITLE;
import static uam.volontario.crud.service.impl.file.FileServiceQualifiers.LANDING_PAGE_FILES_SERVICE;
import static uam.volontario.crud.service.impl.file.FileServiceQualifiers.REGULATIONS_FILE_SERVICE_QUALIFIER;

@Component
public class ConfigurationHandler
{
    private final FileService< LandingPageDto, LandingPageDto > landingPageFileService;
    private final FileService< String, String > regulationsFileService;

    public ConfigurationHandler( @Qualifier( LANDING_PAGE_FILES_SERVICE )
                                 final FileService< LandingPageDto, LandingPageDto > aFileService,
                                 @Qualifier( REGULATIONS_FILE_SERVICE_QUALIFIER )
                                 final FileService< String, String > aRegulationsFileService )
    {
        landingPageFileService = aFileService;
        regulationsFileService = aRegulationsFileService;
    }

    public LandingPageDto resolveLandingPageData() throws IOException
    {
        return landingPageFileService.loadFile( null );
    }

    public RegulationsDto resolveRegulationsData() throws IOException
    {
        final String useRegulations = getRegulationContent( USE_REGULATION_TITLE );
        final String rodoRegulations = getRegulationContent( RODO_REGULATION_TITLE );
        return RegulationsDto.builder()
                .useRegulation( useRegulations )
                .rodoRegulation( rodoRegulations )
                .build();
    }

    public LandingPageDto soreNewLandingPageConfiguration( final LandingPageDto configurationData ) throws IOException
    {
        this.landingPageFileService.saveFile( configurationData );
        return landingPageFileService.loadFile( null );
    }

    public RegulationsDto storeNewRegulationsConfiguration( final RegulationsDto aRegulationsDto ) throws IOException
    {
        regulationsFileService.saveFile( USE_REGULATION_TITLE, aRegulationsDto.getUseRegulation() );
        regulationsFileService.saveFile( RODO_REGULATION_TITLE, aRegulationsDto.getRodoRegulation() );
        return aRegulationsDto;
    }

    private String getRegulationContent( final String aRegulationFileName ) throws IOException
    {
        return regulationsFileService.loadFile( aRegulationFileName );
    }
}
