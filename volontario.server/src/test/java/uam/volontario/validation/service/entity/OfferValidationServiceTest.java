package uam.volontario.validation.service.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import uam.volontario.SampleDataUtil;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.OfferType;
import uam.volontario.validation.ValidationResult;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith( MockitoJUnitRunner.Silent.class )
class OfferValidationServiceTest {

    @InjectMocks
    OfferValidationService offerValidationService;

    @BeforeEach
    void init()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldValidateCorrectOffer()
    {
        Instant now = Instant.now();

        //given
        User user = new User();
        user.setInstitution( SampleDataUtil.getSampleInstitution() );

        OfferType type = new OfferType( 0L, "Type", Collections.emptyList() );
        Offer offer = new Offer( 0L, "Title", "Desc", user, null,
                SampleDataUtil.getSampleInstitution(), type, now, now.plusSeconds( 2000L ), false,
                null, SampleDataUtil.getSampleOfferState(), List.of( SampleDataUtil.getSampleInterestCategory() ),
                Collections.emptyList(), Collections.emptyList(), null, null, now.plusSeconds( 1000L ), "periodicDesc",
                "Place", false, false);

        //when
        ValidationResult validationResult = offerValidationService.validateEntity(offer);

        //then
        assertTrue( validationResult.isValidated() );
    }

    @Test
    void shouldNotValidateOfferWithEndDateBeforeExpirationDate()
    {
        Instant now = Instant.now();

        //given
        User user = new User();
        user.setInstitution( SampleDataUtil.getSampleInstitution() );

        OfferType type = new OfferType( 0L, "Type", Collections.emptyList() );
        Offer offer = new Offer( 0L, "Title", "Desc", user, null,
                SampleDataUtil.getSampleInstitution(), type, now, now.plusSeconds( 1000L ), false,
                null, SampleDataUtil.getSampleOfferState(), List.of( SampleDataUtil.getSampleInterestCategory() ),
                Collections.emptyList(), Collections.emptyList(),null, null, now.plusSeconds( 2000L ), "periodicDesc",
                "Place", false, false);

        //when
        ValidationResult validationResult = offerValidationService.validateEntity(offer);

        //then
        assertFalse( validationResult.isValidated() );
        assertEquals( 1, validationResult.getValidationViolations().size() );
        assertEquals( "End date should not be earlier than Expiration date",
                validationResult.getValidationViolations().get( "offerDates" ) );
    }

    @Test
    void shouldNotValidateOfferWithRequiredExperienceTrueButNoExperienceObject()
    {
        Instant now = Instant.now();

        //given
        User user = new User();
        user.setInstitution( SampleDataUtil.getSampleInstitution() );

        OfferType type = new OfferType( 0L, "Type", Collections.emptyList() );
        Offer offer = new Offer( 0L, "Title", "Desc", user, null,
                SampleDataUtil.getSampleInstitution(), type, now, now.plusSeconds( 2000L ), true,
                null, SampleDataUtil.getSampleOfferState(), List.of( SampleDataUtil.getSampleInterestCategory() ),
                Collections.emptyList(), Collections.emptyList(), null, null, now.plusSeconds( 1000L ), "periodicDesc",
                "Place", false, false);

        //when
        ValidationResult validationResult = offerValidationService.validateEntity(offer);

        //then
        assertFalse( validationResult.isValidated() );
        assertEquals( 1, validationResult.getValidationViolations().size() );
        assertEquals( "Experience should be specified",
                validationResult.getValidationViolations().get( "experience" ) );
    }

    @Test
    void shouldNotValidateOfferWithRequiredExperienceFalseButWithExperienceObject()
    {
        Instant now = Instant.now();

        //given
        User user = new User();
        user.setInstitution( SampleDataUtil.getSampleInstitution() );

        OfferType type = new OfferType( 0L, "Type", Collections.emptyList() );
        Offer offer = new Offer( 0L, "Title", "Desc", user, null,
                SampleDataUtil.getSampleInstitution(), type, now, now.plusSeconds( 2000L ), false,
                SampleDataUtil.getSampleExperienceLeve(), SampleDataUtil.getSampleOfferState(), List.of( SampleDataUtil.getSampleInterestCategory() ),
                Collections.emptyList(), Collections.emptyList(), null, null, now.plusSeconds( 1000L ), "periodicDesc",
                "Place", false, false);

        //when
        ValidationResult validationResult = offerValidationService.validateEntity(offer);

        //then
        assertFalse( validationResult.isValidated() );
        assertEquals( 1, validationResult.getValidationViolations().size() );
        assertEquals( "Experience should not be specified",
                validationResult.getValidationViolations().get( "experience" ) );
    }
}