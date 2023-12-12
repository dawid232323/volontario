package uam.volontario.configuration.provider.impl;

import com.google.common.io.Resources;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uam.volontario.configuration.provider.DefaultS3FilesProvider;
import uam.volontario.crud.service.FileService;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static uam.volontario.configuration.provider.DefaultConfigurationFIleNames.RODO_REGULATION_TITLE;
import static uam.volontario.configuration.provider.DefaultConfigurationFIleNames.USE_REGULATION_TITLE;
import static uam.volontario.crud.service.impl.file.FileServiceQualifiers.REGULATIONS_FILE_SERVICE_QUALIFIER;

@Component
public class RegulationFilesProviderImpl implements DefaultS3FilesProvider
{
    private final FileService< String, String > fileService;
    private final String resourcesPrefix = "regulations/";

    public RegulationFilesProviderImpl( @Qualifier( REGULATIONS_FILE_SERVICE_QUALIFIER ) final FileService< String, String > aFileService )
    {
        this.fileService = aFileService;
    }

    @Override
    public void provideDefaultFiles() throws IOException
    {
        provideUseRegulations();
        provideRodoRegulations();
    }

    private void provideUseRegulations() throws IOException
    {
        if( fileService.doesFileExist( USE_REGULATION_TITLE ) )
        {
            return;
        }
        final URL regulationUrl = Resources.getResource( resourcesPrefix.concat( USE_REGULATION_TITLE ) );
        final String regulationContent = Resources.toString( regulationUrl, StandardCharsets.UTF_8 );
        fileService.saveFile( USE_REGULATION_TITLE, regulationContent );
    }

    private void provideRodoRegulations() throws IOException
    {
        if( fileService.doesFileExist( RODO_REGULATION_TITLE ) )
        {
            return;
        }
        final URL regulationUrl = Resources.getResource( resourcesPrefix.concat( RODO_REGULATION_TITLE ) );
        final String regulationContent = Resources.toString( regulationUrl, StandardCharsets.UTF_8 );
        fileService.saveFile( RODO_REGULATION_TITLE, regulationContent );
    }

}
