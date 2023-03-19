package uam.volontario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Runs and initializes Volontario's backend.
 */
@SpringBootApplication
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
