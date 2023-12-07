package uam.volontario.configuration.provider.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.google.common.io.Resources;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uam.volontario.configuration.provider.DefaultS3FilesProvider;
import uam.volontario.crud.service.FileService;
import uam.volontario.crud.service.impl.file.LandingPageFileServiceImpl;
import uam.volontario.dto.templates.LandingPageDto;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static uam.volontario.configuration.provider.DefaultConfigurationFIleNames.LANDING_PAGE_TITLE;
import static uam.volontario.crud.service.impl.file.FileServiceQualifiers.LANDING_PAGE_FILES_SERVICE;

@Component
public class LandingPageTemplateProvider implements DefaultS3FilesProvider
{
    private final String bucketName;
    private final AmazonS3 amazonS3;
    private final FileService< LandingPageDto, LandingPageDto > fileService;
    private final String prefix = "configuration/";

    public LandingPageTemplateProvider( @Value("${cloud.aws.s3.bucket.name}") final String aBucketName,
                                        final FileService< LandingPageDto, LandingPageDto > aFileService,
                                        final AmazonS3 aAmazonS3 )
    {
        bucketName = aBucketName;
        fileService = aFileService;
        amazonS3 = aAmazonS3;
    }

    @Override
    public void provideDefaultFiles() throws IOException
    {
        if( amazonS3.doesObjectExist( bucketName, prefix.concat( LANDING_PAGE_TITLE ) ) )
        {
            return;
        }
        final LandingPageDto initialFileValue = getLandingPageDto();
        fileService.saveFile( initialFileValue );
    }

    private LandingPageDto getLandingPageDto() throws IOException
    {
        final URL url = Resources.getResource( "landingPage/".concat( LANDING_PAGE_TITLE ) );
        final String fileJsonString = Resources.toString( url, StandardCharsets.UTF_8 );
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue( fileJsonString, LandingPageDto.class );
    }
}
