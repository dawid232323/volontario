package uam.volontario.validation.service.entity;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import uam.volontario.crud.service.InstitutionContactPersonService;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.validation.ValidationResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@RunWith( MockitoJUnitRunner.Silent.class )
public class InstitutionContactPersonValidationServiceTest {
    @InjectMocks
    InstitutionContactPersonValidationService institutionContactPersonValidationService;
    @Spy
    InstitutionContactPersonService institutionContactPersonService;
    @Spy
    UserService userService;

    @BeforeEach
    void init()
    {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void shouldValidateCorrectContactPersonEntity()
    {
        //given
        InstitutionContactPerson contactPerson = new InstitutionContactPerson( 0L, null, "Name",
                "Surname", "000000000", "test@test.test");

        //when
        assertThatInstitutionContactPersonServiceRepositoryIsEmpty();
        assertThatUserServiceRepositoryIsEmpty();
        ValidationResult validationResult = institutionContactPersonValidationService.validateEntity(contactPerson);

        //then
        assertTrue( validationResult.isValidated() );
    }

    @Test
    public void shouldNotValidateContactPersonWhenEmailIsTaken()
    {
        //given
        String email = "test@test.test";
        InstitutionContactPerson contactPerson = new InstitutionContactPerson( 1L, null, "Name",
                "Surname", "000000001", email );

        //when
        assertThatUserServiceRepositoryIsEmpty();
        assertThatInstitutionContactPersonServiceRepositoryContainsSomeone();
        ValidationResult validationResult = institutionContactPersonValidationService.validateEntity(contactPerson);

        //then
        assertFalse( validationResult.isValidated() );
        assertEquals( 1, validationResult.getValidationViolations().size() );
        assertEquals( email + " belongs to already registered User or Contact Person.",
                validationResult.getValidationViolations().get( "contactEmailAddress" ) );
    }

    @Test
    public void shouldNotValidateContactPersonWhenPhoneNumberIsTaken()
    {
        //given
        String phoneNumber = "000000000";
        InstitutionContactPerson contactPerson = new InstitutionContactPerson( 1L, null, "Name",
                "Surname", phoneNumber, "test2@test.test" );

        //when
        assertThatUserServiceRepositoryIsEmpty();
        assertThatInstitutionContactPersonServiceRepositoryContainsSomeone();
        ValidationResult validationResult = institutionContactPersonValidationService.validateEntity(contactPerson);

        //then
        assertFalse( validationResult.isValidated() );
        assertEquals( 1, validationResult.getValidationViolations().size() );
        assertEquals( phoneNumber + " belongs to already registered User or Contact Person.",
                validationResult.getValidationViolations().get( "phoneNumber" ) );
    }


    private void assertThatInstitutionContactPersonServiceRepositoryIsEmpty()
    {
        doReturn( Collections.emptyList() ).when( institutionContactPersonService )
                .loadAllEntities();
    }
    private void assertThatUserServiceRepositoryIsEmpty()
    {
        doReturn( Collections.emptyList() ).when( userService )
                .loadAllEntities();
    }

    private void assertThatInstitutionContactPersonServiceRepositoryContainsSomeone()
    {
        InstitutionContactPerson contactPerson = new InstitutionContactPerson( 0L, null, "Name",
                "Surname", "000000000", "test@test.test");
        doReturn(List.of( contactPerson ) ).when( institutionContactPersonService )
                .loadAllEntities();
    }
}