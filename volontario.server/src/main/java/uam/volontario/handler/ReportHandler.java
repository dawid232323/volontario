package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uam.volontario.security.mail.MailService;

import java.util.Arrays;

/**
 * Handler class for Report related business logic.
 */
@Service
public class ReportHandler
{
    private final MailService mailService;

    /**
     * CDI constructor.
     *
     * @param aMailService mail service.
     */
    @Autowired
    public ReportHandler( final MailService aMailService )
    {
        mailService = aMailService;
    }

    /**
     * Reports issue about problem in the system to system maintenance group by sending email messages with details
     * of issue.
     *
     * @param aReportName name of report.
     *
     * @param aReportDescription description of report.
     *
     * @param aAttachments attachments linked to the report.
     *
     *
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 400 if more than 5 attachments were provided.
     *        - Response Entity with code 503 when problem with emails could not be sent.
     *        - Response Entity with code 500 in case of any other unexpected server side error.
     */
    public ResponseEntity< ? > reportIssue( final String aReportName, final String aReportDescription,
                                            final MultipartFile[] aAttachments )
    {
        try
        {
            if( aAttachments.length > 5 )
            {
                return ResponseEntity.badRequest()
                        .body( "Issue can be reported with no more than 5 attachments." );
            }

            if( mailService.sendMailAboutReportBeingMade( aReportName, aReportDescription, Arrays.asList( aAttachments ) ) )
            {
                return ResponseEntity.ok()
                        .build();
            }

            return ResponseEntity.status( HttpStatus.SERVICE_UNAVAILABLE )
                    .body( "Server side error on sending email to administrators." );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
