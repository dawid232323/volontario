package uam.volontario.security.mail;

import com.google.common.io.Resources;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Service for sending emails from backend.
 */
@Service
public class MailService
{
    private final JavaMailSender mailSender;

    /**
     * CDI constructor.
     * 
     * @param aJavaMailSender
     *                            mail sender.
     */
    @Autowired
    public MailService( final JavaMailSender aJavaMailSender )
    {
        mailSender = aJavaMailSender;
    }

    /**
     * Sends verification email to moderator regarding accepting newly registered Institution.
     *
     * @param aInstitution
     *                            institution.
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendInstitutionVerificationMailToModerator( final Institution aInstitution )
        throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        // TODO: email fields need to be verified with prostecki.
        final String moderatorEmail = "s464891@wmi.amu.edu.pl";
        final String volontarioAddress = "no-reply@volontario.com";
        final String sender = "Volontario";
        final String mailSubject = aInstitution.getName() + " asks for verification.";

        helper.setFrom( volontarioAddress, sender );
        helper.setTo( moderatorEmail );
        helper.setSubject( mailSubject );
        helper.setText( buildMailContentForInstitutionRegistration( aInstitution ), true );

        mailSender.send( message );
    }

    /**
     * Sends email notifying institution of verification acceptance.
     *
     * @param aInstitution
     *                            institution.
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendInstitutionAcceptedMail( Institution aInstitution )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        final String volontarioAddress = "no-reply@volontario.com";
        final String sender = "Volontario";
        final String mailSubject = "Your institution " + aInstitution.getName() + " has been verified by Volontario.";

        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();

        helper.setFrom( volontarioAddress, sender );
        helper.setTo( contactPerson.getContactEmail() );
        helper.setSubject( mailSubject );
        helper.setText( buildMailContentForInstitutionAccepted( aInstitution ), true );

        mailSender.send( message );
    }

    /**
     * Sends email notifying institution of verification rejection.
     *
     * @param aInstitution
     *                            institution.
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendInstitutionRejectedMail( Institution aInstitution )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        final String volontarioAddress = "no-reply@volontario.com";
        final String sender = "Volontario";
        final String mailSubject = aInstitution.getName() + " verification rejected.";

        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();

        helper.setFrom( volontarioAddress, sender );
        helper.setTo( contactPerson.getContactEmail() );
        helper.setSubject( mailSubject );
        helper.setText( buildMailContentForInstitutionRejected( aInstitution ), true );

        mailSender.send( message );
    }

    private String buildMailContentForInstitutionRegistration( final Institution aInstitution )
            throws IOException
    {
        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();

        // TODO: Urls need to be stored inside the env variable
        final String acceptUrl = "http://localhost:4200" + "/institution/verify?a=accept&t=" + aInstitution.getRegistrationToken();
        final String rejectUrl = "http://localhost:4200" + "/institution/verify?a=reject&t=" + aInstitution.getRegistrationToken();

        URL url = Resources.getResource( "emails/institutionRegistration.html" );

        String content = formatSharedMailContent(aInstitution, contactPerson, url);
        content = content.replaceAll( "\\|verificationUrl\\|", acceptUrl);
        content = content.replaceAll( "\\|rejectUrl\\|", rejectUrl);

        return content;
    }

    private String buildMailContentForInstitutionAccepted( Institution aInstitution )
            throws IOException
    {
        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();
        final String proceedUrl = "http://localhost:8080" + "/institution/register-contact-person?t=" + aInstitution.getRegistrationToken();

        URL url = Resources.getResource( "emails/institutionRegistrationAccepted.html" );

        String content = formatSharedMailContent (aInstitution, contactPerson, url );
        content = content.replaceAll( "\\|proceedUrl\\|", proceedUrl );

        return content;
    }

    private String buildMailContentForInstitutionRejected( Institution aInstitution )
            throws IOException
    {
        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();
        final String rejectionReason = "Rejection reason will be passed here"; //TODO

        URL url = Resources.getResource( "emails/institutionRegistrationRejected.html" );

        String content = formatSharedMailContent( aInstitution, contactPerson, url );
        content = content.replaceAll( "\\|rejectionReason\\|", rejectionReason );

        return content;
    }


    private String formatSharedMailContent( Institution aInstitution, InstitutionContactPerson aContactPerson,
                                           URL aUrl ) throws IOException
    {
        String content = Resources.toString(aUrl, StandardCharsets.UTF_8 );
        content = content.replaceAll( "\\|institutionName\\|", aInstitution.getName() );
        content = content.replaceAll( "\\|krsNumber\\|", aInstitution.getKrsNumber() );
        content = content.replaceAll( "\\|headQuartersAddress\\|", aInstitution.getHeadquarters() );
        content = content.replaceAll( "\\|localization\\|", aInstitution.getLocalization() );
        content = content.replaceAll( "\\|description\\|", aInstitution.getDescription() );
//        content = content.replaceAll( "\\|tags\\|", StringUtils.join( aInstitution.getTags(), ", " ) );
        content = content.replaceAll( "\\|firstName\\|", aContactPerson.getFirstName() );
        content = content.replaceAll( "\\|lastName\\|", aContactPerson.getLastName() );
        content = content.replaceAll( "\\|contactEmail\\|", aContactPerson.getContactEmail() );
        content = content.replaceAll( "\\|phoneNumber\\|", aContactPerson.getPhoneNumber() );
        return content;
    }
}
