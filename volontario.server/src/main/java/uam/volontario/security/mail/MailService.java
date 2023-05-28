package uam.volontario.security.mail;

import com.google.common.io.Resources;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.Benefit;
import uam.volontario.model.offer.impl.Offer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Service for sending emails from backend.
 */
@Service
public class MailService
{
    private final JavaMailSender mailSender;

    private final DateTimeFormatter instantFormatter;

    private final String noReplyVolontarioEmailAddress;

    /**
     * CDI constructor.
     * 
     * @param aJavaMailSender
     *                            mail sender.
     * @param aNoReplyVolontarioEmailAddress no reply volontario email address
     */
    @Autowired
    public MailService( final JavaMailSender aJavaMailSender,
                        final @Value("${volontarioNoReplyEmailAddress}") String aNoReplyVolontarioEmailAddress )
    {
        mailSender = aJavaMailSender;
        noReplyVolontarioEmailAddress = aNoReplyVolontarioEmailAddress;
        instantFormatter = DateTimeFormatter.ofPattern( "dd.MM.yyyy" )
                .withZone( ZoneId.systemDefault() );
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( MailService.class );

    private void trySendMail( final Runnable aMailRunnable )
    {
        try
        {
            aMailRunnable.run();
        }
        catch ( Exception aE )
        {
            LOGGER.warn( "Error while sending mail: " + aE.getMessage() );
        }
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
        final String sender = "Volontario";
        final String mailSubject = aInstitution.getName() + " asks for verification.";

        helper.setFrom( noReplyVolontarioEmailAddress, sender );
        helper.setTo( moderatorEmail );
        helper.setSubject( mailSubject );
        helper.setText( buildMailContentForInstitutionRegistration( aInstitution ), true );

        mailSender.send( message );
    }

    /**
     * Sends email to Volunteer after he/she made an application for offer.
     *
     * @param aApplication
     *                            application.
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendApplicationCreatedMailToVolunteer( final Application aApplication )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        final Offer offer = aApplication.getOffer();

        final String volunteerContactEmail = aApplication.getVolunteer()
                .getContactEmailAddress();
        final String sender = "Volontario";
        final String mailSubject = "Application for offer: " + offer.getTitle() + " has been made!";

        helper.setFrom( noReplyVolontarioEmailAddress, sender );
        helper.setTo( volunteerContactEmail );
        helper.setSubject( mailSubject );
        helper.setText( createContentForApplicationMadeEmail( aApplication.getOffer() ), true );

        trySendMail( () -> mailSender.send( message ) );
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

        final String sender = "Volontario";
        final String mailSubject = "Your institution " + aInstitution.getName() + " has been verified by Volontario.";

        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();

        helper.setFrom( noReplyVolontarioEmailAddress, sender );
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

        final String sender = "Volontario";
        final String mailSubject = aInstitution.getName() + " verification rejected.";

        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();

        helper.setFrom( noReplyVolontarioEmailAddress, sender );
        helper.setTo( contactPerson.getContactEmail() );
        helper.setSubject( mailSubject );
        helper.setText( buildMailContentForInstitutionRejected( aInstitution ), true );

        mailSender.send( message );
    }

    /**
     * Sens Email to Volunteer about his/her Application being accepted.
     *
     * @param aVolunteerContactEmail Volunteer's contact email.
     *
     * @param aOfferName name of offer from Application.
     *
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendEmailAboutApplicationBeingAccepted( final String aVolunteerContactEmail, final String aOfferName )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        final String sender = "Volontario";
        final String mailSubject = "Your application has been accepted!";

        helper.setFrom( noReplyVolontarioEmailAddress, sender );
        helper.setTo( aVolunteerContactEmail );
        helper.setSubject( mailSubject );

        String content = Resources.toString( Resources.getResource( "emails/applicationAccepted.html" ),
                StandardCharsets.UTF_8 );
        content = content.replaceAll( "\\|offerName\\|", aOfferName );

        helper.setText( content, true );

        mailSender.send( message );
    }

    /**
     * Sens Email to Volunteer about his/her Application being declined.
     *
     * @param aVolunteerContactEmail Volunteer's contact email.
     *
     * @param aOfferName name of offer from Application.
     *
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendEmailAboutApplicationBeingDeclined( final String aVolunteerContactEmail, final String aOfferName )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        final String sender = "Volontario";
        final String mailSubject = "Sorry, your application has been declined...";

        helper.setFrom( noReplyVolontarioEmailAddress, sender );
        helper.setTo( aVolunteerContactEmail );
        helper.setSubject( mailSubject );

        String content = Resources.toString( Resources.getResource( "emails/applicationDeclined.html" ),
                StandardCharsets.UTF_8 );
        content = content.replaceAll( "\\|offerName\\|", aOfferName );

        helper.setText( content, true );

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

        String content = createContentForInstitutionAcceptanceMail(aInstitution, contactPerson, url);
        content = content.replaceAll( "\\|verificationUrl\\|", acceptUrl);
        content = content.replaceAll( "\\|rejectUrl\\|", rejectUrl);

        return content;
    }

    private String buildMailContentForInstitutionAccepted( Institution aInstitution )
            throws IOException
    {
        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();
        final String proceedUrl = "http://localhost:4200" + "/institution/register-contact-person?t=" + aInstitution.getRegistrationToken();

        URL url = Resources.getResource( "emails/institutionRegistrationAccepted.html" );

        String content = createContentForInstitutionAcceptanceMail(aInstitution, contactPerson, url );
        content = content.replaceAll( "\\|proceedUrl\\|", proceedUrl );

        return content;
    }

    private String buildMailContentForInstitutionRejected( Institution aInstitution )
            throws IOException
    {
        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();
        final String rejectionReason = "Rejection reason will be passed here"; //TODO

        URL url = Resources.getResource( "emails/institutionRegistrationRejected.html" );

        String content = createContentForInstitutionAcceptanceMail( aInstitution, contactPerson, url );
        content = content.replaceAll( "\\|rejectionReason\\|", rejectionReason );

        return content;
    }


    private String createContentForInstitutionAcceptanceMail( Institution aInstitution, InstitutionContactPerson aContactPerson,
                                                             URL aUrl ) throws IOException
    {
        String content = Resources.toString(aUrl, StandardCharsets.UTF_8 );
        content = content.replaceAll( "\\|institutionName\\|", aInstitution.getName() );
        content = content.replaceAll( "\\|krsNumber\\|", aInstitution.getKrsNumber() );
        content = content.replaceAll( "\\|headQuartersAddress\\|", aInstitution.getHeadquarters() );
        content = content.replaceAll( "\\|localization\\|", aInstitution.getLocalization() );
        content = content.replaceAll( "\\|description\\|", aInstitution.getDescription() );
        content = content.replaceAll( "\\|firstName\\|", aContactPerson.getFirstName() );
        content = content.replaceAll( "\\|lastName\\|", aContactPerson.getLastName() );
        content = content.replaceAll( "\\|contactEmail\\|", aContactPerson.getContactEmail() );
        content = content.replaceAll( "\\|phoneNumber\\|", aContactPerson.getPhoneNumber() );
        return content;
    }

    private String createContentForApplicationMadeEmail( final Offer aOffer ) throws IOException
    {
        String content = Resources.toString( Resources.getResource( "emails/applicationMade.html" ),
                StandardCharsets.UTF_8 );

        content = content.replaceAll( "\\|offerName\\|", aOffer.getTitle() );
        content = content.replaceAll( "\\|offerDescription\\|", aOffer.getDescription() );
        content = content.replaceAll( "\\|institutionName\\|", aOffer.getInstitution().getName() );
        content = content.replaceAll( "\\|offerTypeName\\|", aOffer.getOfferType().getName() );
        content = content.replaceAll( "\\|offerStartDate\\|", instantFormatter.format( aOffer.getStartDate() ) );
        content = content.replaceAll( "\\|offerEndDate\\|", aOffer.getEndDate() != null ? instantFormatter.format( aOffer.getEndDate() ) : "No end date defined in the offer." );
        content = content.replaceAll( "\\|offerBenefits\\|", String.join( ",", aOffer.getBenefits().stream()
                .map( Benefit::getName )
                .toList() ) );
        content = content.replaceAll( "\\|offerPlace\\|", Optional.ofNullable( aOffer.getPlace() )
                .orElse( aOffer.getIsPoznanOnly() ? "Poznań" : StringUtils.EMPTY ) );
        content = content.replaceAll( "\\|periodicDescription\\|", Optional.ofNullable( aOffer.getPeriodicDescription() )
                .orElse( StringUtils.EMPTY ) );
        return content;
    }
}
