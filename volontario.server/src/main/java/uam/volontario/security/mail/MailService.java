package uam.volontario.security.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uam.volontario.dto.InstitutionContactPersonDto;
import uam.volontario.dto.InstitutionDto;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

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
        throws MessagingException, UnsupportedEncodingException
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

    private String buildMailContentForInstitutionRegistration( final Institution aInstitution )
    {
        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();
        final StringBuilder contentBuilder = new StringBuilder();

        // TODO: this email should be hold in separate file in /resources section.
        // TODO: once 'tags' are resolved in terms on how to store it in db, bring it back.
        contentBuilder.append( "Institution '|institutionName|' asks for verification:<br>" );
        contentBuilder.append( "Institution details: <br>" );
        contentBuilder.append( "<ul>" );
        contentBuilder.append( "  <li>Name: |institutionName|</li>" );
        contentBuilder.append( "  <li>KRS: |krsNumber|</li>" );
        contentBuilder.append( "  <li>Headquarters: |headQuartersAddress|</li>" );
        contentBuilder.append( "  <li>Localization: |localization|</li>" );
        contentBuilder.append( "  <li>Description: |description|</li>" );
//        contentBuilder.append( "  <li>Tags: |tags|</li>" );
        contentBuilder.append( "</ul> " );
        contentBuilder.append( "Contact person: <br>" );
        contentBuilder.append( "<ul>" );
        contentBuilder.append( "  <li>Name: |firstName| |lastName|</li>" );
        contentBuilder.append( "  <li>Contact email: |contactEmail|</li>" );
        contentBuilder.append( "  <li>Phone number: |phoneNumber|</li>" );
        contentBuilder.append( "</ul> " );
        contentBuilder.append( "<h3><a href=\"|acceptUrl|\" target=\"_self\">ACCEPT</a></h3>" );
        contentBuilder.append( "<h3><a href=\"|rejectUrl|\" target=\"_self\">REJECT</a></h3>" );

        // TODO: Urls need to be stored inside the env variable
        final String acceptUrl = "http://localhost:4200" + "/institution/verify?a=accept&t=" + aInstitution.getRegistrationToken();
        final String rejectUrl = "http://localhost:4200" + "/institution/verify/?a=reject&t" + aInstitution.getRegistrationToken();

        String content = contentBuilder.toString();

        content = content.replaceAll( "\\|institutionName\\|", aInstitution.getName() );
        content = content.replaceAll( "\\|krsNumber\\|", aInstitution.getKrsNumber() );
        content = content.replaceAll( "\\|headQuartersAddress\\|", aInstitution.getHeadquarters() );
        content = content.replaceAll( "\\|localization\\|", aInstitution.getLocalization() );
        content = content.replaceAll( "\\|description\\|", aInstitution.getDescription() );
//        content = content.replaceAll( "\\|tags\\|", StringUtils.join( aInstitution.getTags(), ", " ) );
        content = content.replaceAll( "\\|firstName\\|", contactPerson.getFirstName() );
        content = content.replaceAll( "\\|lastName\\|", contactPerson.getLastName() );
        content = content.replaceAll( "\\|contactEmail\\|", contactPerson.getContactEmail() );
        content = content.replaceAll( "\\|phoneNumber\\|", contactPerson.getPhoneNumber() );
        content = content.replaceAll( "\\|acceptUrl\\|", acceptUrl );
        content = content.replaceAll( "\\|rejectUrl\\|", rejectUrl );


        return content;
    }

}
