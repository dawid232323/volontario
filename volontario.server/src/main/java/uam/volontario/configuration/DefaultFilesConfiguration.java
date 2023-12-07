package uam.volontario.configuration;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import uam.volontario.configuration.provider.DefaultS3FilesProvider;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@DependsOn( "SimpleStorage" )
public class DefaultFilesConfiguration
{
    private final List< DefaultS3FilesProvider > s3FilesProviders;

    @Bean
    public void configureFiles() throws IOException
    {
        for ( final DefaultS3FilesProvider provider: s3FilesProviders ) {
            provider.provideDefaultFiles();
        }
    }
}
