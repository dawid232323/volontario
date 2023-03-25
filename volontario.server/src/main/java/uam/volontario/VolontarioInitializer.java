package uam.volontario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Runs and initializes Volontario's backend.
 */
@SpringBootApplication( exclude = { SecurityAutoConfiguration.class } )
public class VolontarioInitializer
{
	/**
	 * Runs Volontario's backend.
     *
	 * @param aArgs arguments.
	 */
	public static void main( final String[] aArgs )
	{
        SpringApplication.run( VolontarioInitializer.class, aArgs );
    }
}
