package uam.volontario.security.mail;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.Benefit;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.VoluntaryPresence;
import uam.volontario.security.util.VolontarioBase64Coder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for sending emails from backend.
 */
@Service
public class MailService
{
    private final JavaMailSender mailSender;

    private final DateTimeFormatter instantFormatter;

    private final String noReplyVolontarioEmailAddress;

    private final String volontarioModeratorAddress; //TODO remove after implementing moderator roles properly

    private final InternetAddress[] maintenanceEmails;

    private final String volontarioHost;

    private final String emailSender = "Volontario";

    /**
     * CDI constructor.
     *
     * @param aJavaMailSender
     *                            mail sender.
     *
     * @param aNoReplyVolontarioEmailAddress no reply volontario email address.
     *
     * @param aMaintenanceEmails comma seperated email addresses of system maintenance group.
     *
     * @param aModeratorAddress email address of moderator.
     *
     * @throws AddressException on error when parsing maintenance email addresses.
     */
    @Autowired
    public MailService( final JavaMailSender aJavaMailSender,
                        final @Value("${volontarioNoReplyEmailAddress}") String aNoReplyVolontarioEmailAddress,
                        final @Value("${volontarioModeratorEmailPlaceholder}") String aModeratorAddress,
                        final @Value("${maintenanceEmails}") String aMaintenanceEmails,
                        final @Value( "${volontarioHost}" ) String aVolontarioHost,
                        final @Value( "${useHttps}" ) Boolean aShouldUseHttps) throws AddressException {
        mailSender = aJavaMailSender;
        noReplyVolontarioEmailAddress = aNoReplyVolontarioEmailAddress;
        instantFormatter = DateTimeFormatter.ofPattern( "dd.MM.yyyy" )
                .withZone( ZoneId.systemDefault() );
        volontarioModeratorAddress = aModeratorAddress;
        maintenanceEmails = InternetAddress.parse( aMaintenanceEmails );
        volontarioHost = this.resolveHostLink( aVolontarioHost, aShouldUseHttps );
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( MailService.class );

    private boolean trySendMail( final Runnable aMailRunnable )
    {
        try
        {
            aMailRunnable.run();
            return true;
        }
        catch ( Exception aE )
        {
            LOGGER.warn( "Error while sending mail: " + aE.getMessage() );
            LOGGER.error( aE );
            return false;
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

        final String mailSubject = aInstitution.getName() + " prosi o weryfikację.";

        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        helper.setTo( volontarioModeratorAddress );
        helper.setSubject( mailSubject );
        helper.setText( buildMailContentForInstitutionRegistration( aInstitution ), true );

        mailSender.send( message );
    }

    /**
     * Sends email to Volunteer and offer creator that he/she made an application for offer.
     *
     * @param aApplication
     *                            application.
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     *
     * @return true if email was successfully sent, false otherwise.
     */
    public boolean sendApplicationCreatedMail( final Application aApplication )
            throws MessagingException, IOException
    {
        final MimeMessage volunteerMessage = getApplicationCreatedVolunteerMessage( aApplication );
        final MimeMessage institutionMessage = getApplicationCreatedInstitutionMessage( aApplication );

        return trySendMail( () -> mailSender.send( volunteerMessage ) ) &&
                trySendMail( () -> mailSender.send( institutionMessage ) );
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

        final String mailSubject = "Twoja instytucja " + aInstitution.getName() + " została zweryfikowana przez Volontario.";

        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();

        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
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

        final String mailSubject = aInstitution.getName() + " - weryfikacja odrzucona.";

        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();

        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        helper.setTo( contactPerson.getContactEmail() );
        helper.setSubject( mailSubject );
        helper.setText( buildMailContentForInstitutionRejected( aInstitution ), true );

        mailSender.send( message );
    }

    /**
     * Sens Email to Volunteer about his/her Application being under recruitmen.
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
    public void sendEmailAboutApplicationUnderRecruitment( final String aVolunteerContactEmail, final String aOfferName )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        final String mailSubject = "Twoja aplikacja jest teraz w trakcie rekrutacji!";

        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        helper.setTo( aVolunteerContactEmail );
        helper.setSubject( mailSubject );

        String content = Resources.toString( Resources.getResource( "emails/applicationAccepted.html" ),
                StandardCharsets.UTF_8 );
        content = content.replaceAll( "\\|offerName\\|", aOfferName );

        helper.setText( content, true );

        mailSender.send( message );
    }

    /**
     * Sens Email to Volunteer about his/her Application being moved to reserve list.
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
    public void sendEmailAboutApplicationBeingMovedToReserveList( final String aVolunteerContactEmail, final String aOfferName )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        final String mailSubject = "Twoja aplikacja trafiła na listę rezerwową.";

        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        helper.setTo( aVolunteerContactEmail );
        helper.setSubject( mailSubject );

        String content = Resources.toString( Resources.getResource( "emails/applicationMovedToReserveList.html" ),
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
     * @param aDecisionReason reason why the application has been declined
     *
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendEmailAboutApplicationBeingDeclined( final String aVolunteerContactEmail, final String aOfferName,
                                                        final String aDecisionReason )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        final String mailSubject = "Niestety, odrzucono twoją aplikację...";

        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        helper.setTo( aVolunteerContactEmail );
        helper.setSubject( mailSubject );

        String content = Resources.toString( Resources.getResource( "emails/applicationDeclined.html" ),
                StandardCharsets.UTF_8 );
        content = content.replaceAll( "\\|offerName\\|", aOfferName );
        content = content.replaceAll( "\\|decisionReason\\|", aDecisionReason );

        helper.setText( content, true );

        mailSender.send( message );
    }

    /**
     * Send emails to all Contact People of expiring Offers.
     *
     * @param aExpiringOffers expiring Offers.
     *
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     *
     * @return true if email was successfully sent, false otherwise.
     */
    public void sendEmailsAboutOffersExpiringSoon( final List< Offer > aExpiringOffers )
            throws MessagingException, IOException
    {
        final List< String > contactEmailAddressesToWhichEmailsWereNotSend = Lists.newArrayList();
        for( final Offer offer : aExpiringOffers )
        {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper( message );

            final String mailSubject = "Twoja oferta wkrótce wygaśnie...";

            helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
            helper.setTo( offer.getContactPerson().getContactEmailAddress() );
            helper.setSubject( mailSubject );

            String content = Resources.toString( Resources.getResource( "emails/offerExpiring.html" ),
                    StandardCharsets.UTF_8 );
            content = content.replaceAll( "\\|offerName\\|", offer.getTitle() );
            content = content.replaceAll( "\\|expirationDate\\|", instantFormatter.format( offer.getStartDate() ) );
            content = content.replaceAll( "\\|offerEditUrl\\|",
                    this.volontarioHost.concat( "/advertisement/edit/ )" + offer.getId() ) );

            helper.setText( content, true );

            if ( !trySendMail( () -> mailSender.send( message ) ) )
            {
                contactEmailAddressesToWhichEmailsWereNotSend.add( offer.getContactPerson().getContactEmailAddress() );
            }
        }

        LOGGER.error( String.format( "Email failed to be sent to %o users", contactEmailAddressesToWhichEmailsWereNotSend.size() ) );
    }

    /**
     * Sends email to employee about his account being created by Institution and password to be set.
     *
     * @param aInstitutionEmployee institution employee.
     *
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     *
     * @return true if email was successfully sent, false if not.
     */
    public boolean sendMailAboutInstitutionEmployeeAccountBeingCreated( final User aInstitutionEmployee ) throws
            MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        final String sender = aInstitutionEmployee.getInstitution().getName();
        final String mailSubject = "Twoje konto zostało utworzone!";

        helper.setFrom( noReplyVolontarioEmailAddress, sender );
        helper.setTo( aInstitutionEmployee.getContactEmailAddress() );
        helper.setSubject( mailSubject );

        String content = Resources.toString( Resources.getResource( "emails/institutionEmployeeAccountCreated.html" ),
                StandardCharsets.UTF_8 );

        content = content.replaceAll( "\\|institutionName\\|", aInstitutionEmployee.getInstitution().getName() );
        content = content.replaceAll( "\\|employeeContactEmail\\|", aInstitutionEmployee.getContactEmailAddress() );
        content = content.replaceAll( "\\|changePasswordLink\\|", String.format( "%s/register-employee/%o?t=%s",
                this.volontarioHost, aInstitutionEmployee.getInstitution().getId(),
                VolontarioBase64Coder.encode( aInstitutionEmployee.getContactEmailAddress() ) ) );
        helper.setText( content, true );

        return trySendMail( () -> mailSender.send( message ) );
    }

    /**
     * Sens email about report with details to system maintenance group.
     *
     * @param aReportName name of report.
     *
     * @param aReportDescription description of report.
     *
     * @param aAttachments attachments linked to the report.
     *
     * @return true if email was successfully sent, false otherwise.
     *
     */
    public boolean sendMailAboutReportBeingMade( final String aReportName, final String aReportDescription,
                                                 final List< MultipartFile > aAttachments ) throws
            MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message, true );

        final String sender = "VOLONTARIO REPORTS";

        helper.setFrom( noReplyVolontarioEmailAddress, sender );
        helper.setTo( maintenanceEmails );
        helper.setSubject( aReportName );

        helper.setText( aReportDescription, true );

        for( final MultipartFile attachment : aAttachments )
        {
            helper.addAttachment( ObjectUtils.defaultIfNull( attachment.getOriginalFilename(), "attachment" ),
                    attachment, ObjectUtils.defaultIfNull( attachment.getContentType(), "image/*" ) );
        }

        return trySendMail( () -> mailSender.send( message ) );
    }

    /**
     * Sends email to Volunteer to confirm/negate/postpone his presence at given Offer.
     *
     * @param aVoluntaryPresence voluntary presence.
     *
     * @param aDecisionDeadline decision deadline.
     *
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendMailToVolunteerAboutPresenceConfirmation( final VoluntaryPresence aVoluntaryPresence,
                                                              final Instant aDecisionDeadline )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message, true );


        final Offer offer = aVoluntaryPresence.getOffer();
        final User volunteer = aVoluntaryPresence.getVolunteer();
        final Application application = volunteer.getApplications().stream()
                .filter( app -> app.getOffer()
                        .equals( offer ) )
                .findAny()
                .orElseThrow( IllegalStateException::new );

        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        helper.setTo( volunteer.getContactEmailAddress() );
        helper.setSubject( String.format( "Potwierdź obecność w wolontariacie: %s", offer.getTitle() ) );

        String content = Resources.toString( Resources.getResource( "emails/volunteerPresenceReminder.html" ),
                StandardCharsets.UTF_8 );

        content = content.replaceAll( "\\|decisionDeadline\\|", instantFormatter.format( aDecisionDeadline ) );
        content = content.replaceAll( "\\|offerName\\|", offer.getTitle() );
        content = content.replaceAll( "\\|confirmationLink\\|",
                String.format( "%s/advertisement/%o",
                        volontarioHost,
                        offer.getId() ) );

        helper.setText( content, true );

        mailSender.send( message );
    }

    /**
     * Sends email to Offer's Contact Person to confirm/negate/postpone Volunteers' presences at given Offer.
     *
     * @param aVoluntaryPresences voluntary presences.
     *
     * @param aDecisionDeadline decision deadline.
     *
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendMailToOfferContactPersonAboutPresenceConfirmation( final List< VoluntaryPresence > aVoluntaryPresences,
                                                                       final Instant aDecisionDeadline )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message, true );


        final VoluntaryPresence anyVoluntaryPresence = aVoluntaryPresences.stream()
                .findAny()
                .orElseThrow( IllegalStateException::new );

        final Offer offer = anyVoluntaryPresence.getOffer();
        final List< User > volunteers = aVoluntaryPresences.stream()
                .map( VoluntaryPresence::getVolunteer )
                .toList();

        final String volunteersIdSeperatedBySemicolon = volunteers.stream()
                .map( user -> String.valueOf( user.getId() ) )
                .collect( Collectors.joining( ";" ) );


        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        helper.setTo( offer.getContactPerson().getContactEmailAddress() );
        helper.setSubject( String.format( "Potwierdź obecność wolontariuszy na wydarzeniu: %s", offer.getTitle() ) );

        String content = Resources.toString( Resources.getResource( "emails/institutionPresenceReminder.html" ),
                StandardCharsets.UTF_8 );

        content = content.replaceAll( "\\|decisionDeadline\\|", instantFormatter.format( aDecisionDeadline ) );
        content = content.replaceAll( "\\|offerName\\|", offer.getTitle() );
        content = content.replaceAll( "\\|confirmationLink\\|",
                String.format( "%s/institution/%o/confirm-presence?o=%o&t=%s",
                        volontarioHost,
                        offer.getInstitution().getId(),
                        offer.getId(),
                        VolontarioBase64Coder.encode( volunteersIdSeperatedBySemicolon ) ) );

        helper.setText( content, true );

        mailSender.send( message );
    }

    /**
     * Sends email to Volunteer to confirm his registration.
     *
     * @param aVolunteer volunteer.
     *
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendMailToVolunteerAboutRegistrationConfirmation( final User aVolunteer )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message, true );

        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        helper.setTo( aVolunteer.getVolunteerData().getDomainEmailAddress() );
        helper.setSubject( "Potwierdzenie rejestracji wolontariusza" );

        String content = Resources.toString( Resources.getResource( "emails/volunteerRegistration.html" ),
                StandardCharsets.UTF_8 );

        content = content.replaceAll( "\\|volunteerFirstName\\|", aVolunteer.getFirstName() );
        content = content.replaceAll( "\\|volunteerLastName\\|", aVolunteer.getLastName() );
        content = content.replaceAll( "\\|confirmationLink\\|",
                String.format( "%s/user/%d/confirm-registration?t=%s",
                        volontarioHost,
                        aVolunteer.getId().intValue(),
                        VolontarioBase64Coder.encode( aVolunteer.getVolunteerData().getDomainEmailAddress() ) ) );

        helper.setText( content, true );

        mailSender.send( message );
    }

    /**
     * Sends email to Volunteer about Institution rating being possible.
     *
     * @param aVoluntaryPresence confirmed voluntary presence.
     *
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendMailToVolunteerAboutPossibilityToRateInstitution( final VoluntaryPresence aVoluntaryPresence )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message, true );

        final User volunteer = aVoluntaryPresence.getVolunteer();
        final Offer offer = aVoluntaryPresence.getOffer();
        final Institution institution = offer.getInstitution();

        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        helper.setTo( volunteer.getContactEmailAddress() );
        helper.setSubject( String.format( "Oceń instytucję %s za ofertę %s", institution.getName(), offer.getTitle() ) );

        String content = Resources.toString( Resources.getResource( "emails/institutionRatingReady.html" ),
                StandardCharsets.UTF_8 );

        content = content.replaceAll( "\\|offerName\\|", offer.getTitle() );
        content = content.replaceAll( "\\|institutionName\\|", institution.getName() );
        content = content.replaceAll( "\\|ratingLink\\|", String.format( "%s/institution/%d?tab=1",
                volontarioHost, institution.getId() ) );

        helper.setText( content, true );

        mailSender.send( message );
    }

    /**
     * Sends email to Offer's contact person about Volunteer rating being possible.
     *
     * @param aVoluntaryPresence confirmed voluntary presence.
     *
     * @throws MessagingException
     *                                          in case of message syntax errors.
     * @throws UnsupportedEncodingException
     *                                          in case of wrong encoding of email.
     */
    public void sendMailToInstitutionAboutPossibilityToRateVolunteer( final VoluntaryPresence aVoluntaryPresence )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message, true );

        final User volunteer = aVoluntaryPresence.getVolunteer();
        final Offer offer = aVoluntaryPresence.getOffer();
        final Institution institution = offer.getInstitution();

        helper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        helper.setTo( offer.getContactPerson().getContactEmailAddress() );
        helper.setSubject( String.format( "Oceń wolontariusza %s za ofertę %s", volunteer.getFullName(), offer.getTitle() ) );

        String content = Resources.toString( Resources.getResource( "emails/volunteerRatingReady.html" ),
                StandardCharsets.UTF_8 );

        content = content.replaceAll( "\\|volunteerFullName\\|", volunteer.getFullName() );
        content = content.replaceAll( "\\|offerName\\|", institution.getName() );
        content = content.replaceAll( "\\|ratingLink\\|", String.format( "%s/user/%d?tab=1",
                volontarioHost, volunteer.getId() ) );

        helper.setText( content, true );

        mailSender.send( message );
    }

    private String buildMailContentForInstitutionRegistration( final Institution aInstitution )
            throws IOException
    {
        final InstitutionContactPerson contactPerson = aInstitution.getInstitutionContactPerson();

        final String acceptUrl = this.volontarioHost + "/institution/verify?a=accept&t=" + aInstitution.getRegistrationToken();
        final String rejectUrl = this.volontarioHost + "/institution/verify?a=reject&t=" + aInstitution.getRegistrationToken();

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
        final String proceedUrl = this.volontarioHost + "/institution/register-contact-person?t=" + aInstitution.getRegistrationToken();

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
        content = content.replaceAll( "\\|krsNumber\\|",
                aInstitution.getKrsNumber() != null ? aInstitution.getKrsNumber() : "Brak" );
        content = content.replaceAll( "\\|headQuartersAddress\\|", aInstitution.getHeadquarters() );
        content = content.replaceAll( "\\|localization\\|", aInstitution.getLocalization() );
        content = content.replaceAll( "\\|description\\|", aInstitution.getDescription() );
        content = content.replaceAll( "\\|tags\\|", aInstitution.getInstitutionTags() );
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
        content = content.replaceAll( "\\|offerDescription\\|", ObjectUtils.defaultIfNull( aOffer.getDescription(),
                StringUtils.EMPTY ) );
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
        content = content.replaceAll( "\\|displayStyle\\|", Optional.ofNullable( aOffer.getPeriodicDescription() )
                .orElse( "none" ) );
        return content;
    }

    private String resolveHostLink( final String aHost, final Boolean aShouldUseHttps ) {
        final String prefix = aShouldUseHttps ? "https://" : "http://";
        return prefix.concat( aHost );
    }

    private MimeMessage getApplicationCreatedVolunteerMessage( final Application aApplication )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper( message );

        final Offer offer = aApplication.getOffer();

        final String volunteerContactEmail = aApplication.getVolunteer()
                .getContactEmailAddress();
        final String sender = "Volontario";
        final String mailSubject = "Aplikacja na ofertę: " + offer.getTitle() + " została utworzona!";

        helper.setFrom( noReplyVolontarioEmailAddress, sender );
        helper.setTo( volunteerContactEmail );
        helper.setSubject( mailSubject );
        helper.setText( createContentForApplicationMadeEmail( aApplication.getOffer() ), true );

        return message;
    }

    private MimeMessage getApplicationCreatedInstitutionMessage( final Application aApplication )
            throws MessagingException, IOException
    {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper messageHelper = new MimeMessageHelper( message );

        final Offer offer = aApplication.getOffer();
        final String contactPersonEmail = offer.getContactPerson()
                .getContactEmailAddress();
        final String mailSubject = String
                .format( "Pojawiła się nowa aplikacja na twoje ogłoszenie %s", offer.getTitle() );

        final String volunteerName = aApplication.getVolunteer().getFullName();
        final String offerLink = String.format( "%s/advertisement/%d",
                volontarioHost, offer.getId().intValue() );

        String messageContent = Resources.toString( Resources
                        .getResource( "emails/applicationMadeInstitution.html" ),
                        StandardCharsets.UTF_8 );
        messageContent = messageContent.replaceAll( "\\|volunteerName\\|", volunteerName );
        messageContent = messageContent.replaceAll( "\\|offerTitle\\|", offer.getTitle() );
        messageContent = messageContent.replaceAll( "\\|offerLink\\|", offerLink );

        messageHelper.setFrom( noReplyVolontarioEmailAddress, emailSender );
        messageHelper.setTo( contactPersonEmail );
        messageHelper.setSubject( mailSubject );
        messageHelper.setText( messageContent, true );

        return message;
    }

}
