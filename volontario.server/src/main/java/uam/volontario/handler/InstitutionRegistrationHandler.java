package uam.volontario.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.InstitutionService;
import uam.volontario.dto.InstitutionDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.security.mail.MailService;

import java.util.Optional;

/**
 * Handler class for {@linkplain Institution} related operations.
 */
@Service
public class InstitutionRegistrationHandler
{
    private final DtoService dtoService;

    private final MailService mailService;

    private final InstitutionService institutionService;

    /**
     * CDI constructor.
     *
     * @param aDtoService dto service.
     *
     * @param aMailService mail service.
     *
     * @param aInstitutionService institution service.
     */
    @Autowired
    public InstitutionRegistrationHandler( final DtoService aDtoService, final MailService aMailService,
                                          final InstitutionService aInstitutionService )
    {
        dtoService = aDtoService;
        mailService = aMailService;
        institutionService = aInstitutionService;
    }

    private static final Logger LOGGER = LogManager.getLogger( InstitutionRegistrationHandler.class );

    /**
     * Registers Institution.
     *
     * @param aInstitutionDto dto containing Institution registration data.
     *
     * @return if Institution passes validation, then ResponseEntity with 201 status and institution. If institution did not
     *         pass validation then ResponseEntity with 401 status and constraints violated. If there was an error,
     *         then ResponseEntity with 500 status and error message.
     */
    public ResponseEntity< ? > registerInstitution( final InstitutionDto aInstitutionDto )
    {
        try
        {
            final Institution institution = dtoService.createInstitutionFromDto( aInstitutionDto );

            // TODO: add server-side validation for institution similar to the volunteer one and return 401 if validation failed.
            institutionService.saveOrUpdate( institution );

            mailService.sendInstitutionVerificationMailToModerator( institution );

            return ResponseEntity.status( HttpStatus.CREATED )
                    .body( institution );

        }
        catch ( Exception aE )
        {
            LOGGER.error( "Exception occurred during registration of institution: {}", aE.getMessage(), aE );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Accepts Institution.
     *
     * @param aRegistrationToken Institution's registration token.
     *
     * @return Response Entity with code 200 if registration token was valid, Response Entity with code 400 if
     *         registration token was invalid, and if any error occurs then Response Entity with code 500 and
     *         error message.
     */
    public ResponseEntity< ? > acceptInstitution( final String aRegistrationToken )
    {
        // TODO: this endpoint should only by accessible for administrator. Adjust once role system is implemented.
        try
        {
            final Optional< Institution > optionalInstitution =
                    institutionService.loadByRegistrationToken( aRegistrationToken );

            if( optionalInstitution.isPresent() )
            {
                final Institution institution = optionalInstitution.get();
                institution.setActive( true );
                institutionService.saveOrUpdate( institution );

                // TODO: send email to contact person for creating an account.

                return ResponseEntity.ok().
                        build();
            }

            return ResponseEntity.badRequest()
                    .build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Rejects and removes Institution from the system.
     *
     * @param aRegistrationToken Institution's registration token.
     *
     * @return Response Entity with code 200 if registration token was valid, Response Entity with code 400 if
     *         registration token was invalid, and if any error occurs then Response Entity with code 500 and
     *         error message.
     */
    public ResponseEntity< ? > rejectInstitution( final String aRegistrationToken )
    {
        // TODO: this endpoint should only by accessible for administrator. Adjust once role system is implemented.
        try
        {
            final Optional< Institution > optionalInstitution =
                    institutionService.loadByRegistrationToken( aRegistrationToken );

            if( optionalInstitution.isPresent() )
            {
                final Institution institution = optionalInstitution.get();
                institutionService.deleteEntity( institution.getId() );

                // TODO: send email to contact person that his attempt was rejected
                return ResponseEntity.ok().
                        build();
            }

            return ResponseEntity.badRequest()
                    .build();
        }
        catch( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
