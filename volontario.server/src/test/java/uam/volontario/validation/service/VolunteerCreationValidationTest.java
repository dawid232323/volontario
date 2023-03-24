package uam.volontario.validation.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.impl.User;
import uam.volontario.validation.ValidationResult;

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
    @Mock
    private UserService userService;

    @InjectMocks
    private UserValidationService userValidationService;

    @Test
    public void volunteerWithBlankFirstNameShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().firstName( "" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

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
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

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
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

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
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "lastName" ) );
    }

    @Test
    public void volunteerWithBlankPasswordShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().hashedPassword( "" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "hashedPassword" ) );
    }

    @Test
    public void volunteerWithNullPasswordShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().hashedPassword( null ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "hashedPassword" ) );
    }

    @Test
    public void volunteerWithBlankDomainEmailShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().domainEmailAddress( "" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "domainEmailAddress" ) );
    }

    @Test
    public void volunteerWithNullDomainEmailShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().domainEmailAddress( null ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "domainEmailAddress" ) );
    }

    @Test
    public void volunteerWithDomainEmailHavingWrongEmailSyntaxShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().domainEmailAddress( "asuafus" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

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
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

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
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

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
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "contactEmailAddress" ) );
    }

    @Test
    public void volunteerWithDomainEmailNotHavingUniversityDomainShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().domainEmailAddress( "student@wp.pl" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "domainEmailAddress" ) );
    }

    @Test
    public void volunteerWithNullPhoneNumberShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().phoneNumber( null ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "phoneNumber" ) );
    }

    @Test
    public void volunteerWithBlankPhoneNumberShouldNotBeValidated()
    {
        // given
        final User volunteer = prepareUserBuilderWithCorrectData().phoneNumber( "" ).build();

        // when
        assertThatUserServiceRepositoryIsEmpty();
        final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

        // then
        assertFalse( validationResult.isValidated() );
        assertNotEquals( null, validationResult.getValidationViolations().get( "phoneNumber" ) );
    }

    @Test( timeout = 15_000 )
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
            final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

            // then
            assertFalse( validationResult.isValidated() );
            assertNotEquals( null, validationResult.getValidationViolations().get( "phoneNumber" ) );
        }
    }

    @Test( timeout = 15_000 )
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
            final ValidationResult validationResult = userValidationService.validateVolunteerUser( volunteer );

            // then
            assertFalse( validationResult.isValidated() );
            assertNotEquals( null, validationResult.getValidationViolations().get( "phoneNumber" ) );
        }
    }

    private void assertThatUserServiceRepositoryIsEmpty()
    {
        doReturn( Collections.emptyList() ).when( userService ).loadAllEntities();
    }

    private User.UserBuilder prepareUserBuilderWithCorrectData()
    {
        return User.builder().firstName( "Jan" )
                .lastName( "Kowalski" )
                .hashedPassword( "aafsaios" )
                .domainEmailAddress( "student@st.amu.edu.pl")
                .contactEmailAddress( "contact@wp.pl" )
                .phoneNumber( "123456789" )
                .isVerified( true );
    }
}