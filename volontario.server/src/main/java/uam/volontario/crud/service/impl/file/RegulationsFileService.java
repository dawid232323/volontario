package uam.volontario.crud.service.impl.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.FileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static uam.volontario.configuration.provider.DefaultConfigurationFIleNames.*;
import static uam.volontario.crud.service.impl.file.FileServiceQualifiers.REGULATIONS_FILE_SERVICE_QUALIFIER;

@Service
@Qualifier( REGULATIONS_FILE_SERVICE_QUALIFIER )
public class RegulationsFileService implements FileService< String, String >
{
    private final String bucketName;
    private final AmazonS3 storageClient;
    private final Set<String> allowedFileNames = Set.of( USE_REGULATION_TITLE, RODO_REGULATION_TITLE );

    public RegulationsFileService( final AmazonS3 aAmazonS3,
                                   @Value("${cloud.aws.s3.bucket.name}") final String aBucketName )
    {
        this.storageClient = aAmazonS3;
        this.bucketName = aBucketName;
    }

    @Override
    public String loadFile( final String aFileName ) throws IOException
    {
        checkFileNameValidity( aFileName );
        final String storageName = getStorageFileName( aFileName );
        if( !storageClient.doesObjectExist( bucketName, storageName ) )
        {
            throw new IOException( "File with " + aFileName + " name does not exist" );
        }
        final S3Object fileObject = storageClient.getObject( bucketName, storageName );
        return IOUtils.toString( fileObject.getObjectContent() );
    }

    @Override
    public String saveFile( final String aFileName, final String aFileContent ) throws IOException
    {
        checkFileNameValidity( aFileName );
        final File tmpConfFile = createFileWithContent( aFileName, aFileContent );
        final String storageFileName = getStorageFileName( aFileName );
        storageClient.putObject( bucketName, storageFileName, tmpConfFile );
        tmpConfFile.delete();
        return aFileName;
    }

    @Override
    public String saveFile( final String aFileContent ) throws IOException
    {
        throw new UnsupportedOperationException( "You must define which of the regulations file should be saved" );
    }

    @Override
    public void deleteFile( final String aFileName )
    {
        throw new UnsupportedOperationException( "Configuration files cannot be removed" );
    }

    @Override
    public boolean doesFileExist( final String aFileName )
    {
        final String finalName = getStorageFileName( aFileName );
        return storageClient.doesObjectExist( bucketName, finalName );
    }

    private void checkFileNameValidity( final String aFileName )
    {
        if( !allowedFileNames.contains( aFileName ) )
        {
            throw new IllegalArgumentException( "Not allowed file name" );
        }
    }

    private String getStorageFileName( final String aFileName )
    {
        return S3_CONFIGURATION_DIRECTORY.concat( aFileName );
    }

    private File createFileWithContent( final String aFileName, final String aFileContent ) throws IOException
    {
        final File contentFile = new File( aFileName );
        contentFile.createNewFile();
        try ( final FileOutputStream fileOutputStream = new FileOutputStream( contentFile ) )
        {
            fileOutputStream.write( aFileContent.getBytes( StandardCharsets.UTF_8 ) );
        }
        return contentFile;
    }
}
