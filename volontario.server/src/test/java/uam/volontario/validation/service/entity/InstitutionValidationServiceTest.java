package uam.volontario.validation.service.entity;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import uam.volontario.crud.service.InstitutionContactPersonService;
import uam.volontario.crud.service.InstitutionService;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.validation.ValidationResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@RunWith( MockitoJUnitRunner.Silent.class )
public class InstitutionValidationServiceTest
{
    @Spy
    InstitutionService institutionService;
    @Spy
    InstitutionContactPersonService institutionContactPersonService;
    @Mock
    InstitutionContactPersonValidationService cpValidationService;
    @InjectMocks
    InstitutionValidationService institutionValidationService;

    @BeforeEach
    void init()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldValidateCorrectInstitutionObject()
    {
        //given
        InstitutionContactPerson contactPerson = new InstitutionContactPerson( 0L, null, "Test",
                "Test", "000000000", "test@test.test");

        Institution institution = new Institution(0L, "Name", "Desc", "HQ", "Loc",
                "1234567891", Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), contactPerson, null,
                true, null, null );
        contactPerson.setInstitution( institution );

        //when
        assertThatInstitutionServiceRepositoryIsEmpty();
        ValidationResult validationResult = institutionValidationService.validateEntity( institution );

        //then
        assertTrue( validationResult.isValidated() );
    }

    @Test
    public void shouldValidateInstitutionWithoutKrs()
    {
        //given
        InstitutionContactPerson contactPerson = new InstitutionContactPerson( 0L, null, "Test",
                "Test", "000000000", "test@test.test");

        Institution institution = new Institution(0L, "Name", "Desc", "HQ", "Loc",
                null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), contactPerson, null,
                true, null, null );
        contactPerson.setInstitution( institution );

        //when
        assertThatInstitutionServiceRepositoryIsEmpty();
        ValidationResult validationResult = institutionValidationService.validateEntity( institution );

        //then
        assertTrue( validationResult.isValidated() );
    }

    @Test
    public void shouldNotValidateInstitutionWithDuplicateKrs()
    {
        //given
        InstitutionContactPerson expectedCPEntity = new InstitutionContactPerson( 0L, null, "Test",
                "Test", "000000000", "test@test.test");
        String krs = "1234567891";

        Institution institution = new Institution(1L, "Name2", "Desc2", "HQ2", "Loc2",
                "1234567891", Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), expectedCPEntity, null,
                true, null, null );
        expectedCPEntity.setInstitution( institution );

        //when
        assertThatInstitutionServiceRepositoryContainsEntity();
        ValidationResult validationResult = institutionValidationService.validateEntity(institution);

        //then
        assertFalse( validationResult.isValidated() );
        assertEquals( 1, validationResult.getValidationViolations().size() );
        assertEquals( krs + " belongs to already registered Institution.",
                validationResult.getValidationViolations().get( "krsNumber") );
    }

    private void assertThatInstitutionServiceRepositoryIsEmpty()
    {
        doReturn( Collections.emptyList() ).when( institutionService )
                .loadAllEntities();
    }

    private void assertThatInstitutionServiceRepositoryContainsEntity()
    {
        InstitutionContactPerson contactPerson = new InstitutionContactPerson( 0L, null, "Test",
                "Test", "000000000", "test@test.test");

        Institution institution = new Institution(0L, "Name", "Desc", "HQ", "Loc",
                "1234567891", Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), contactPerson, null,
                true, null, null );
        contactPerson.setInstitution( institution );

        doReturn(List.of( institution ) ).when( institutionService )
                .loadAllEntities();
    }
}