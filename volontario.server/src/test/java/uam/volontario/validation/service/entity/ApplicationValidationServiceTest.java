package uam.volontario.validation.service.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import uam.volontario.SampleDataUtil;
import uam.volontario.crud.service.ApplicationService;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.ApplicationState;
import uam.volontario.validation.ValidationResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith( MockitoJUnitRunner.Silent.class )
public class ApplicationValidationServiceTest
{
    @InjectMocks
    ApplicationValidationService applicationValidationService;
    @Spy
    ApplicationService applicationService;

    @Test
    public void shouldValidateCorrectApplicationEntity()
    {
        //given
        ApplicationState state = new ApplicationState(3L, "Oczekujące");
        User volunteer = SampleDataUtil.prepareUserBuilderWithCorrectVolunteerData().roles( List.of( new Role( 0L, "Wolontariusz(ka)",
                Collections.emptyList() ) ) ).build();
        Application application = new Application( 0L, volunteer, SampleDataUtil.getSampleOffer(), state,
                "Motivation", "Reason", false );
        assertThatApplicationRepositoryIsEmpty();

        //when
        ValidationResult validationResult = applicationValidationService.validateEntity( application );

        //then
        assertTrue( validationResult.isValidated() );
    }

    @Test
    public void shouldNotValidateApplicationWhenUserIsNotVolunteer()
    {
        //given
        ApplicationState state = new ApplicationState(3L, "Oczekujące");
        User user = SampleDataUtil.prepareGenericUserBuilderWithCorrectData().build();
        Application application = new Application( 0L, user, SampleDataUtil.getSampleOffer(), state,
                "Motivation", "Reason", false );
        assertThatApplicationRepositoryIsEmpty();

        //when
        ValidationResult validationResult = applicationValidationService.validateEntity( application );

        //then
        assertFalse( validationResult.isValidated() );
        assertEquals( 1, validationResult.getValidationViolations().size() );
        assertEquals( "User of id " + user.getId() + " is not a Volunteer" ,
                validationResult.getValidationViolations().get( "volunteer" ) );
    }

    private void assertThatApplicationRepositoryIsEmpty()
    {
        doReturn( new PageImpl<Application>( Collections.emptyList() ) ).when( applicationService )
                .findFiltered( any(), any() );
    }

}