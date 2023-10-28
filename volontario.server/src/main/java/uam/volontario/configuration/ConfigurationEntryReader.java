package uam.volontario.configuration;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uam.volontario.crud.service.ConfigurationEntryService;
import uam.volontario.model.configuration.ConfigurationEntry;
import uam.volontario.model.utils.ModelUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Utility class for easy readability of values from {@linkplain ConfigurationEntry} instances.
 */
@UtilityClass
public class ConfigurationEntryReader
{
    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( ConfigurationEntryReader.class );

    /**
     * Reads Configuration Entry's value as Integer.
     *
     * @param aConfigurationEntryKey configuration entry key name.
     *
     * @param aConfigurationEntryService configuration entry service.
     *
     * @return value of given key as Integer.
     */
    public Integer readValueAsInteger( final ConfigurationEntryKeySet aConfigurationEntryKey, final ConfigurationEntryService aConfigurationEntryService )
    {
        final ConfigurationEntry configurationEntry = ModelUtils.resolveConfigurationEntry( aConfigurationEntryKey.getKeyName(),
                aConfigurationEntryService );

        return Integer.parseInt( configurationEntry.getValue() );
    }

    /**
     * Reads Configuration Entry's value as Duration.
     *
     * @param aConfigurationEntryKeyName configuration entry key name.
     *
     * @param aDurationUnit unit of Duration.
     *
     * @param aConfigurationEntryService configuration entry service.
     *
     * @return value of given key as Duration.
     */
    public Duration readValueAsDuration( final String aConfigurationEntryKeyName, final ChronoUnit aDurationUnit,
                                         final ConfigurationEntryService aConfigurationEntryService  )
    {
        final ConfigurationEntry configurationEntry = ModelUtils.resolveConfigurationEntry( aConfigurationEntryKeyName,
                aConfigurationEntryService );

        return Duration.of( Long.parseLong( configurationEntry.getValue() ), aDurationUnit );
    }

    /**
     * Reads Configuration Entry's value as Duration or returns default value in case of an error.
     *
     * @param aConfigurationEntryKey configuration entry key name.
     *
     * @param aDurationUnit unit of Duration.
     *
     * @param aDefaultValue default value.
     *
     * @param aConfigurationEntryService configuration entry service.
     *
     * @return value of given key as Duration or default.
     */
    public Duration readValueAsDurationOrDefault( final ConfigurationEntryKeySet aConfigurationEntryKey, final ChronoUnit aDurationUnit,
                                                  final Duration aDefaultValue,
                                                  final ConfigurationEntryService aConfigurationEntryService )
    {
        final Optional< ConfigurationEntry > optionalSingleTimeOfferBufferConfigurationKey =
                aConfigurationEntryService.findByKey( aConfigurationEntryKey.getKeyName() );

        if( optionalSingleTimeOfferBufferConfigurationKey.isEmpty() )
        {
            LOGGER.warn( String.format( "Value of %s is undefined. Value will be set to %s.", aConfigurationEntryKey, aDefaultValue ) );
            return aDefaultValue;
        }

        try
        {
            final long numericValue = Long.parseLong( optionalSingleTimeOfferBufferConfigurationKey.get().
                    getValue() );

            return Duration.of( numericValue, aDurationUnit );
        }
        catch ( Exception aE )
        {
            LOGGER.error( String.format( "Value of %s was not read properly." +
                    " Value will be set to %s.", aConfigurationEntryKey, aDefaultValue ) );

            return aDefaultValue;
        }
    }
}
