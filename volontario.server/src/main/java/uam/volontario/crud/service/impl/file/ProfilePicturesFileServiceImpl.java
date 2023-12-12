package uam.volontario.crud.service.impl.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.utils.IoUtils;
import uam.volontario.crud.service.FileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

@Service
@Qualifier(FileServiceQualifiers.PROFILE_PICTURE_FILES_SERVICE)
public class ProfilePicturesFileServiceImpl implements FileService< MultipartFile, byte[] >
{

    private final AmazonS3 simpleStorageClient;
    private final String bucketName;

    public ProfilePicturesFileServiceImpl( final AmazonS3 aAmazonS3,
                                           @Value("${cloud.aws.s3.bucket.name}") final String aBucketName )
    {
        this.simpleStorageClient = aAmazonS3;
        this.bucketName = aBucketName;
    }

    @Override
    public byte[] loadFile( final String aFileName ) throws IOException
    {
        if( !this.simpleStorageClient.doesObjectExist( this.bucketName, aFileName ) )
        {
            return null;
        }
        final S3Object s3Object = this.simpleStorageClient.getObject( this.bucketName, aFileName );
        return IoUtils.toByteArray( s3Object.getObjectContent() );
    }

    @Override
    public String saveFile( final MultipartFile aFileContent ) throws IOException
    {
        final String finalName = Arrays.toString( DigestUtils.sha256( RandomStringUtils.randomAlphabetic( 100 ) ) )
                .concat( ObjectUtils.defaultIfNull( aFileContent.getOriginalFilename(),
                        RandomStringUtils.random( 10 ).concat( ".jpeg" ) ) );
        return saveFile( finalName, aFileContent );
    }

    @Override
    public String saveFile( final String aFileName, final MultipartFile aFileContent ) throws IOException
    {
        final File convertedFile = this.convertMultipartToFile( aFileContent, aFileName );
        simpleStorageClient.putObject( this.bucketName,
                this.getImageDirectoryPrefix().concat( convertedFile.getName() ), convertedFile );
        convertedFile.delete();
        return this.getImageDirectoryPrefix().concat( convertedFile.getName() );
    }

    @Override
    public void deleteFile( final String aFileName )
    {
        this.simpleStorageClient.deleteObject( this.bucketName, aFileName );
    }

    @Override
    public boolean doesFileExist( final String aFileName )
    {
        final String finalName = getImageDirectoryPrefix().concat( aFileName );
        return simpleStorageClient.doesObjectExist( bucketName, finalName );
    }

    private String getImageDirectoryPrefix()
    {
        return "profilePictures/";
    }

    private File convertMultipartToFile( final MultipartFile aFile, final String aFileName ) throws IOException
    {
        final File newFile = new File( aFileName );
        newFile.createNewFile();
        try ( final FileOutputStream fileOutputStream = new FileOutputStream( newFile ) )
        {
            fileOutputStream.write( aFile.getBytes() );
        }
        return newFile;
    }
}
