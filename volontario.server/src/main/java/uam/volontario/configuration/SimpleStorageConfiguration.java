package uam.volontario.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uam.volontario.configuration.provider.DefaultS3FilesProvider;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SimpleStorageConfiguration
{
    @Bean( "SimpleStorage" )
    public AmazonS3 configureAmazonS3Client(@Value("${cloud.aws.s3.endpoint}") String aServiceEndpoint,
                                            @Value("${cloud.aws.s3.path-style.enabled}") Boolean aPathStyleAccessEnabled,
                                            @Value("${cloud.aws.credentials.access-key}") String aAccessKey,
                                            @Value("${cloud.aws.credentials.secret-key}") String aSecretKey,

                                            @Value("${cloud.aws.region.static:null}") String aRegion,
                                            @Value("${cloud.aws.s3.bucket.name}") String aBucketName) throws IOException
    {
        final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration( getEndpointConfiguration( aServiceEndpoint, aRegion ) )
                .withPathStyleAccessEnabled( aPathStyleAccessEnabled )
                .withCredentials( getCredentialsProvider( aAccessKey, aSecretKey ) )
                .build();
        if( !amazonS3.doesBucketExistV2( aBucketName ) )
        {
            amazonS3.createBucket( aBucketName );
        }

        return amazonS3;
    }

    private AwsClientBuilder.EndpointConfiguration getEndpointConfiguration( final String aServiceEndpoint,
                                                                             final String aRegion )
    {
        return new AwsClientBuilder.EndpointConfiguration( aServiceEndpoint, aRegion );
    }

    private AWSCredentialsProvider getCredentialsProvider( final String aAccessKey, final String aSecretKey )
    {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials( aAccessKey, aSecretKey )
        );
    }
}
