package uam.volontario.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uam.volontario.handler.ReportHandler;

/**
 * Controller for handling report action.
 */
@RestController
@RequestMapping( value = "/api/report",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.MULTIPART_FORM_DATA_VALUE )
public class ReportController
{
    private final ReportHandler reportHandler;

    /**
     * CDI constructor.
     *
     * @param aReportHandler report handler.
     */
    @Autowired
    public ReportController( final ReportHandler aReportHandler )
    {
        reportHandler = aReportHandler;
    }

    /**
     * Reports issue about problem in the system to system maintenance group by sending email messages with details
     * of issue. Email is sent with 'reportName' as email subject, 'reportDescription' as email content and 'attachments'
     * as attachments to the email.
     *
     * @param aReportName name of report.
     *
     * @param aReportDescription description of report.
     *
     * @param aAttachments attachments linked to the report.
     *
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 400 if more than 5 attachments were provided.
     *        - Response Entity with code 503 when problem with emails could not be sent.
     *        - Response Entity with code 500 in case of any other unexpected server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    @PostMapping( value = "/with-attachments" )
    public ResponseEntity< ? > reportIssueWithAttachments( @RequestParam( "reportName" ) final String aReportName,
                                            @RequestParam( "reportDescription" ) final String aReportDescription,
                                            @RequestParam( "attachments" ) final MultipartFile[] aAttachments )
    {
        return reportHandler.reportIssue( aReportName, aReportDescription, aAttachments );
    }

    /**
     * Reports issue about problem in the system to system maintenance group by sending email messages with details
     * of issue. Email is sent with 'reportName' as email subject, 'reportDescription' as email content.
     *
     * @param aReportName name of report.
     *
     * @param aReportDescription description of report.
     *
     *
     * @return
     *        - Response Entity with code 200 if everything went as expected.
     *        - Response Entity with code 503 when problem with emails could not be sent.
     *        - Response Entity with code 500 in case of any other unexpected server side error.
     */
    @PreAuthorize( "@permissionEvaluator.allowForEveryone()" )
    @PostMapping
    public ResponseEntity< ? > reportIssue( @RequestParam( "reportName" ) final String aReportName,
                                            @RequestParam( "reportDescription" ) final String aReportDescription ) throws InterruptedException
    {
        return reportIssueWithAttachments( aReportName, aReportDescription, new MultipartFile[]{} );
    }
}
