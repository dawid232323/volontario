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
     * @param aInstitutionDto
     *                            institution dto.
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendInstitutionVerificationMailToModerator( final InstitutionDto aInstitutionDto )
        throws MessagingException, UnsupportedEncodingException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        // TODO: email fields need to be verified with prostecki.
        final String moderatorEmail = "s464846@wmi.amu.edu.pl";
        final String volontarioAddress = "no-reply@volontario.com";
        final String sender = "Volontario";
        final String mailSubject = aInstitutionDto.getName() + " asks for verification.";

        helper.setFrom( volontarioAddress, sender );
        helper.setTo( moderatorEmail );
        helper.setSubject( mailSubject );
        helper.setText( buildMailContentForInstitutionRegistration( aInstitutionDto ), true );

        mailSender.send( message );
    }

    private String buildMailContentForInstitutionRegistration( final InstitutionDto aInstitutionDto )
    {
        final InstitutionContactPersonDto contactPersonDto = aInstitutionDto.getContactPerson();
        final StringBuilder contentBuilder = new StringBuilder();

        // TODO: perhaps definition of content could be defined in separate file?
        contentBuilder.append( "Institution '|institutionName|' asks for verification:<br>" );
        contentBuilder.append( "Institution details: <br>" );
        contentBuilder.append( "<ul>" );
        contentBuilder.append( "  <li>Name: |institutionName|</li>" );
        contentBuilder.append( "  <li>KRS: |krsNumber|</li>" );
        contentBuilder.append( "  <li>Headquarters: |headQuartersAddress|</li>" );
        contentBuilder.append( "  <li>Localization: |localization|</li>" );
        contentBuilder.append( "  <li>Description: |description|</li>" );
        contentBuilder.append( "  <li>Tags: |tags|</li>" );
        contentBuilder.append( "</ul> " );
        contentBuilder.append( "Contact person: <br>" );
        contentBuilder.append( "<ul>" );
        contentBuilder.append( "  <li>Name: |firstName| |lastName|</li>" );
        contentBuilder.append( "  <li>Contact email: |contactEmail|</li>" );
        contentBuilder.append( "  <li>Phone number: |phoneNumber|</li>" );
        contentBuilder.append( "</ul> " );
        contentBuilder.append( "<h3><a href=\"|verificationUrl|\" target=\"_self\">VERIFY</a></h3>" );

        final String verifyURL =
            "http://URLneedsToBeChoosen:port" + "/accept?krs=" + aInstitutionDto.getKrsNumber();

        String content = contentBuilder.toString();

        content = content.replaceAll( "\\|institutionName\\|", aInstitutionDto.getName() );
        content = content.replaceAll( "\\|krsNumber\\|", aInstitutionDto.getKrsNumber() );
        content = content.replaceAll( "\\|headQuartersAddress\\|", aInstitutionDto.getHeadquartersAddress() );
        content = content.replaceAll( "\\|localization\\|", aInstitutionDto.getLocalization() );
        content = content.replaceAll( "\\|description\\|", aInstitutionDto.getDescription() );
        content = content.replaceAll( "\\|tags\\|", StringUtils.join( aInstitutionDto.getTags(), ", " ) );
        content = content.replaceAll( "\\|firstName\\|", contactPersonDto.getFirstName() );
        content = content.replaceAll( "\\|lastName\\|", contactPersonDto.getLastName() );
        content = content.replaceAll( "\\|contactEmail\\|", contactPersonDto.getContactEmail() );
        content = content.replaceAll( "\\|phoneNumber\\|", contactPersonDto.getPhoneNumber() );
        content = content.replaceAll( "\\|verificationUrl\\|", verifyURL );

        return content;
    }

}
