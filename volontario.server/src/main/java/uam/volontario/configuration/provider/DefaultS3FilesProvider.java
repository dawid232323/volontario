package uam.volontario.configuration.provider;

import com.amazonaws.services.s3.AmazonS3;

import java.io.IOException;

/**
 * Interface used inherited by classes that create files that require to exist since the system startup.
 */
public interface DefaultS3FilesProvider
{
    void provideDefaultFiles() throws IOException;
}
