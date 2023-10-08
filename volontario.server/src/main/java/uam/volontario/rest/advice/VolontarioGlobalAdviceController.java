package uam.volontario.rest.advice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uam.volontario.exception.user.RoleMismatchException;

/**
 * Catches and handles all specified exception by returning
 * {@linkplain ResponseEntity} with appropriate message and status code
 */
@ControllerAdvice
public class VolontarioGlobalAdviceController extends ResponseEntityExceptionHandler
{
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
    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity< ? > handleEntityNotFoundException( final EntityNotFoundException aNotFoundException )
    {
        return ResponseEntity.badRequest()
                .body( "Object with given id does not exist: " + aNotFoundException.getMessage() );
    }
}
