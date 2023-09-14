package uam.volontario.security.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility security class to encode and decode data using Base64.
 */
@UtilityClass
public class VolontarioBase64Coder
{
    private static final Base64.Encoder ENCODER = Base64.getEncoder().withoutPadding();

    private static final Base64.Decoder DECODER = Base64.getDecoder();

    /**
     * Encodes given String to Base64 format.
     *
     * @param aStringToEncode string to encode.
     *
     * @return encoded string.
     */
    public String encode( final String aStringToEncode )
    {
        return ENCODER.encodeToString( aStringToEncode.getBytes( StandardCharsets.UTF_8 ) );
    }

    /**
     * Decodes given String back from Base64 format.
     *
     * @param aStringToDecode string to decode.
     *
     * @return decoded string.
     */
    public String decode( final String aStringToDecode )
    {
        return new String( DECODER.decode( aStringToDecode.getBytes( StandardCharsets.UTF_8 ) ) );
    }
}
