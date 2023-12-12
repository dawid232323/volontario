package uam.volontario.rest.advice;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uam.volontario.exception.VolontarioEntityNotFoundException;
import uam.volontario.exception.user.RoleMismatchException;

import java.io.IOException;

/**
 * Catches and handles all specified exception by returning
 * {@linkplain ResponseEntity} with appropriate message and status code
 */
@ControllerAdvice
public class VolontarioGlobalAdviceController extends ResponseEntityExceptionHandler
{

    final Logger logger = LogManager.getLogger( VolontarioGlobalAdviceController.class );

    /**
     * Catches and handles {@linkplain RoleMismatchException}.
     *
     * @param aRoleMismatchException thrown exception
     *
     * @return response entity with status 400 and exception message body
     */
    @ExceptionHandler({ RoleMismatchException.class } )
    public ResponseEntity< ? > handleRoleMismatchError( final RoleMismatchException aRoleMismatchException )
    {
        return ResponseEntity.badRequest()
                .body( aRoleMismatchException.getMessage() );
    }

    /**
     * Catches and handles {@linkplain EntityNotFoundException}.
     *
     * @param aNotFoundException thrown exception
     *
     * @return response entity with status 400 and exception message body
     */
    @ExceptionHandler({ VolontarioEntityNotFoundException.class })
    public ResponseEntity< ? > handleEntityNotFoundException( final VolontarioEntityNotFoundException aNotFoundException )
    {
        if( aNotFoundException.getEntityId() != null )
        {
            return ResponseEntity.badRequest()
                    .body( String.format( "Instance of class %s of id: %o was not found in the database",
                            aNotFoundException.getEntityType(), aNotFoundException.getEntityId() ) );
        }

        return ResponseEntity.badRequest()
                .body( String.format( "Instance of class %s of name: %s was not found in the database",
                        aNotFoundException.getEntityType(), aNotFoundException.getEntityName() ) );
    }

    /**
     * Handles system IO Exceptions.
     *
     * @param aIOException thrown exception
     *
     * @return ResponseEntity with status code 500 and exception message
     */
    @ExceptionHandler({ IOException.class })
    public ResponseEntity< ? > handleIOException( final IOException aIOException )
    {
        logger.error( aIOException );
        return ResponseEntity.internalServerError().body( aIOException.getMessage() );
    }

    /**
     * Handles system unsupported operation Exceptions.
     *
     * @param aException thrown exception
     *
     * @return ResponseEntity with status code 400 and exception message
     */
    @ExceptionHandler({ UnsupportedOperationException.class })
    public ResponseEntity< ? > handleUnsupportedOperationException( final UnsupportedOperationException aException )
    {
        logger.warn( aException );
        return ResponseEntity.badRequest().body( aException.getMessage() );
    }

    /**
     * Handles system illegal argument exception Exceptions.
     *
     * @param aException thrown exception
     *
     * @return ResponseEntity with status code 400 and exception message
     */
    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity< ? > handleIllegalArgumentException( final IllegalArgumentException aException )
    {
        logger.warn( aException );
        return ResponseEntity.badRequest().body( aException.getMessage() );
    }
}
