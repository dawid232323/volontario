package uam.volontario.crud.service.impl.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.utils.IoUtils;
import uam.volontario.crud.service.FileService;
import uam.volontario.dto.templates.LandingPageDto;

import java.io.File;
import java.io.IOException;

import static uam.volontario.configuration.provider.DefaultConfigurationFIleNames.LANDING_PAGE_TITLE;
import static uam.volontario.crud.service.impl.file.FileServiceQualifiers.LANDING_PAGE_FILES_SERVICE;

@Service
@Qualifier( LANDING_PAGE_FILES_SERVICE )
public class LandingPageFileServiceImpl implements FileService< LandingPageDto, LandingPageDto >
{
    private final AmazonS3 simpleStorageClient;
    private final String bucketName;
    private final ObjectMapper mapper;
    private final String prefix = "configuration/";

    public LandingPageFileServiceImpl( final AmazonS3 aAmazonS3,
                                       @Value( "${cloud.aws.s3.bucket.name}" ) final String aBucketName )
    {
        this.simpleStorageClient = aAmazonS3;
        this.bucketName = aBucketName;
        mapper = new ObjectMapper();
    }

    @Override
    public LandingPageDto loadFile( final String aFileName ) throws IOException
    {
        if( !simpleStorageClient.doesObjectExist( bucketName, prefix.concat( LANDING_PAGE_TITLE ) ) )
        {
            return null;
        }
        final S3Object s3Object = simpleStorageClient.getObject( bucketName, prefix.concat( LANDING_PAGE_TITLE ) );
        final String objectContent = IoUtils.toUtf8String( s3Object.getObjectContent() );
        return mapper.readValue( objectContent, LandingPageDto.class );
    }

    @Override
    public String saveFile( final LandingPageDto aFileContent ) throws IOException
    {
        final File tmpFile = new File( LANDING_PAGE_TITLE );
        mapper.writeValue( tmpFile, aFileContent );
        saveFile( tmpFile );
        tmpFile.delete();
        return LANDING_PAGE_TITLE;
    }

    @Override
    public String saveFile( final String aFileName, final LandingPageDto aFileContent ) throws IOException
    {
        throw new UnsupportedOperationException( "Landing page file title cannot be changed" );
    }

    @Override
    public boolean doesFileExist( final String aFileName )
    {
        final String finalName = prefix.concat( aFileName );
        return simpleStorageClient.doesObjectExist( bucketName, finalName );
    }

    @Override
    public void deleteFile( final String aFileName )
    {
        throw new UnsupportedOperationException( "Landing page configuration file cannot be deleted" );
    }

    private void saveFile( final File fileContent )
    {
        if( simpleStorageClient.doesObjectExist( bucketName, LANDING_PAGE_TITLE ) )
        {
            simpleStorageClient.deleteObject( bucketName, prefix.concat( LANDING_PAGE_TITLE ) );
        }
        simpleStorageClient.putObject( bucketName, prefix.concat( LANDING_PAGE_TITLE ), fileContent );
    }
}
