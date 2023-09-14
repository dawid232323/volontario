package uam.volontario.validation.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.UserValidationService;
import uam.volontario.validation.service.entity.VolunteerDataValidationService;

import java.time.Instant;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.doReturn;

/**
 * Tests for creating/validating Volunteer.
 */
@RunWith( MockitoJUnitRunner.Silent.class )
public class VolunteerCreationValidationTest
{
    @Spy
    private UserService userService;

    @Spy
    private VolunteerDataValidationService volunteerDataValidationService;

    @InjectMocks
    private UserValidationService userValidationService;

    @Test
    public void volunteerWithBlankFirstNameShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().firstName( "" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "firstName" ) );
    }

    @Test
    public void volunteerWithNullFirstNameShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().firstName( null ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "firstName" ) );
    }

    @Test
    public void volunteerWithBlankLastNameShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().lastName( "" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "lastName" ) );
    }

    @Test
    public void volunteerWithNullLastNameShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().lastName( null ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "lastName" ) );
    }

    @Test
    public void volunteerWithBlankPasswordShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().password( "" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "password" ) );
    }

    @Test
    public void volunteerWithNullPasswordShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().password( null ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "password" ) );
    }

    @Test
    public void volunteerWithBlankDomainEmailShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().build();
        volunteer.getVolunteerData().setDomainEmailAddress( "" );

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "domainEmailAddress" ) );
    }

    @Test
    public void volunteerWithNullDomainEmailShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().build();
        volunteer.getVolunteerData().setDomainEmailAddress( null );

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "domainEmailAddress" ) );
    }

    @Test
    public void volunteerWithDomainEmailHavingWrongEmailSyntaxShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().build();
        volunteer.getVolunteerData().setDomainEmailAddress( "ihsgi" );

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "domainEmailAddress" ) );
    }

    @Test
    public void volunteerWithBlankContactEmailShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().contactEmailAddress( "" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "contactEmailAddress" ) );
    }

    @Test
    public void volunteerWithNullContactEmailShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().contactEmailAddress( null ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "contactEmailAddress" ) );
    }

    @Test
    public void volunteerWithContactEmailHavingWrongEmailSyntaxShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().contactEmailAddress( "asuafus" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "contactEmailAddress" ) );
    }

    @Test
    public void volunteerWithDomainEmailNotHavingUniversityDomainShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().build();
        volunteer.getVolunteerData().setDomainEmailAddress( "student@wp.pl" );


        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "domainEmailAddress" ) );
    }

    @Test
    @Ignore // TODO: once phone number validation is back again.

    public void volunteerWithNullPhoneNumberShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().phoneNumber( null ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "phoneNumber" ) );
    }

    @Test
    @Ignore // TODO: once phone number validation is back again.
    public void volunteerWithBlankPhoneNumberShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().phoneNumber( "" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "phoneNumber" ) );
    }

    @Test( timeout = 15_000 )
    @Ignore // TODO: once phone number validation is back again.
    public void volunteerWithPhoneNumberHavingOtherThanNumericCharactersShouldNotBeValidated()
    {
        for( int i = 0; i < 1000; i++ )
        {
            // given
            final User volunteer = prepareUserBuilderWithCorrectData()
                    .phoneNumber( RandomStringUtils.randomAlphabetic( new Random().nextInt( 1000 ) ) )
                    .build();

            // when
            assertThatUserServiceRepositoryIsEmpty();
            final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

            // then
            assertFalse( validationResult.isValidated() );
            assertNotEquals( null, validationResult.getValidationViolations().get( "phoneNumber" ) );
        }
    }

    @Test( timeout = 15_000 )
    @Ignore // TODO: once phone number validation is back again.
    public void volunteerWithPhoneNumberHavingMoreOrLessThan9DigitsShouldNotBeValidated()
    {
        for( int i = 0; i < 1000; i++ )
        {
            // given
            final String phoneNumber = RandomStringUtils.randomNumeric( 0, 1000 );
            if( phoneNumber.length() == 9 ) continue;

            final User volunteer = prepareUserBuilderWithCorrectData()
                    .phoneNumber( phoneNumber )
                    .build();

            // when
            assertThatUserServiceRepositoryIsEmpty();
            final ValidationResult validationResult = userValidationService.validateEntity( volunteer );

            // then
            assertFalse( validationResult.isValidated() );
            assertNotEquals( null, validationResult.getValidationViolations().get( "phoneNumber" ) );
        }
    }

    private void assertThatUserServiceRepositoryIsEmpty()
    {
        doReturn( Collections.emptyList() ).when( userService )
                .loadAllEntities();
    }

    private User.UserBuilder prepareUserBuilderWithCorrectData()
    {
        final VolunteerData volunteerData = VolunteerData.builder()
                .domainEmailAddress( "student@st.amu.edu.pl" )
                .build();


        return User.builder().firstName( "Jan" )
                .lastName( "Kowalski" )
                .hashedPassword( "aafsaios" )
                .contactEmailAddress( "contact@wp.pl" )
                .password( "ABCdef123_" )
                .phoneNumber( "123456789" )
                .volunteerData( volunteerData )
                .creationDate( Instant.now() )
                .isVerified( true );
    }
}