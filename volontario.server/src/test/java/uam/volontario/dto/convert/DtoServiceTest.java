package uam.volontario.dto.convert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import uam.volontario.SampleDataUtil;
import uam.volontario.crud.service.*;
import uam.volontario.dto.Application.ApplicationBaseInfoDto;
import uam.volontario.dto.Application.ApplicationDetailsDto;
import uam.volontario.dto.ExperienceLevelDto;
import uam.volontario.dto.Institution.InstitutionContactPersonDto;
import uam.volontario.dto.Institution.InstitutionDto;
import uam.volontario.dto.Institution.InterestCategoryDto;
import uam.volontario.dto.Offer.OfferBaseInfoDto;
import uam.volontario.dto.Offer.OfferDetailsDto;
import uam.volontario.dto.Offer.OfferDto;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.model.offer.impl.*;
import uam.volontario.model.volunteer.impl.ExperienceLevel;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.model.volunteer.impl.VolunteerData;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@RunWith( MockitoJUnitRunner.Silent.class )
class DtoServiceTest {

    @Spy
    ExperienceLevelService experienceLevelService;
    @Spy
    RoleService roleService;
    @Spy
    InterestCategoryService interestCategoryService;
    @Spy
    OfferTypeService offerTypeService;
    @Spy
    UserService userService;
    @Spy
    OfferStateService offerStateService;
    @InjectMocks
    DtoService dtoService;

    @BeforeEach
    void init()
    {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void shouldCreateVolunteerFromDto() {

        //given
        VolunteerDto dto = new VolunteerDto( "Name", "Surname", "pas", "test@amu.edu.pl", "test@test.pl", "000000000",
                "motivation", 0L, "informatyka", Collections.emptyList() );

        //when
        assertThatExperienceLevelIsReturned();
        User volunteerFromDto = dtoService.createVolunteerFromDto(dto);

        //then
        assertEquals( "Name", volunteerFromDto.getFirstName() );
        assertEquals( "Surname", volunteerFromDto.getLastName() );
        assertNull( volunteerFromDto.getInstitution() );
        assertEquals( "test@test.pl", volunteerFromDto.getContactEmailAddress() );
        assertEquals( "000000000", volunteerFromDto.getPhoneNumber() );
        assertEquals( "test@amu.edu.pl", volunteerFromDto.getVolunteerData().getDomainEmailAddress() );
        assertEquals( "motivation", volunteerFromDto.getVolunteerData().getParticipationMotivation() );

         ExperienceLevel level = volunteerFromDto.getVolunteerData().getExperience();
        assertEquals( "exLevel", level.getName() );
        assertEquals( "def", level.getDefinition() );
        assertEquals( 0L, level.getId() );
    }

    @Test
    void shouldCreateInstitutionFromDto() {

        //given
        InstitutionContactPersonDto cpDto = new InstitutionContactPersonDto( null, "Name", "Surname", "000000000", "test@test.test" );
        InstitutionDto dto = new InstitutionDto( 0L, "Name", cpDto, "123", "HQ", List.of( "Tag" ), "Desc", "Loc" );

        //when
        Institution institutionFromDto = dtoService.createInstitutionFromDto(dto);

        //then
        assertEquals( "Name", institutionFromDto.getName() );
        assertEquals( "123", institutionFromDto.getKrsNumber() );
        assertEquals( "HQ", institutionFromDto.getHeadquarters() );
        assertEquals( "Desc", institutionFromDto.getDescription())  ;
        assertEquals( "Loc", institutionFromDto.getLocalization() );

        InstitutionContactPerson contactPerson = institutionFromDto.getInstitutionContactPerson();
        assertEquals( "Name", contactPerson.getFirstName() );
        assertEquals( "Surname", contactPerson.getLastName() );
        assertEquals( "000000000", contactPerson.getPhoneNumber() );
        assertEquals( "test@test.test", contactPerson.getContactEmail() );
    }

    @Test
    void shouldConvertInterestCategoryToDto()
    {
        //given
        InterestCategory ic = new InterestCategory( 0L, "Name", "Desc", Collections.emptyList(), Collections.emptyList(), true );

        //when
        InterestCategoryDto interestCategoryDto = dtoService.interestCategoryToDto(ic);

        //then
        assertEquals( 0L, interestCategoryDto.getId() );
        assertEquals( "Name", interestCategoryDto.getName() );
        assertEquals( "Desc", interestCategoryDto.getDescription() );
    }

    @Test
    void shouldConvertVolunteerExperienceToDto() {
        //given
        ExperienceLevel ex = new ExperienceLevel( 0L, "Name", "Def", 1L, true );

        //when
        ExperienceLevelDto dto = dtoService.volunteerExperienceToDto( ex );

        //then
        assertEquals( 0L, dto.getId() );
        assertEquals( "Name", dto.getName() );
        assertEquals( "Def", dto.getDefinition() );
        assertEquals( dto.getValue(), 1L );
    }

    @Test
    void shouldCreateOfferFromDto()
    {
        Instant now = Instant.now();
        //given
        OfferDto dto = new OfferDto( "Title", Date.from( now.plusSeconds( 1000L ) ), 0L, 0L,
                Date.from( now ), Date.from( now.plusSeconds( 2000L ) ), Collections.emptyList(),
                false, null, "desc", "place", "periodicDesc", true,
                Collections.emptyList(), null, null );

        //when
        assertThatTypeIsReturned();
        assertThatStateIsReturned();
        assertThatContactPersonIsReturned();
        Offer offerFromDto = dtoService.createOfferFromDto(dto);

        //then
        assertEquals( "Title", offerFromDto.getTitle() );
        assertEquals( "Type", offerFromDto.getOfferType().getName() );
        assertEquals( Date.from( now ).toInstant().truncatedTo(ChronoUnit.SECONDS ), offerFromDto.getStartDate() );
        assertEquals( Date.from( now.plusSeconds( 2000L ) ).toInstant().truncatedTo(ChronoUnit.SECONDS ),
                offerFromDto.getEndDate() );
        assertEquals( Date.from( now.plusSeconds( 1000L ) ).toInstant().truncatedTo(ChronoUnit.SECONDS ) ,
                offerFromDto.getExpirationDate() );
        assertEquals( "desc", offerFromDto.getDescription() );
        assertEquals( "place", offerFromDto.getPlace() );
        assertEquals( "periodicDesc", offerFromDto.getPeriodicDescription() );
        assertTrue( offerFromDto.getIsPoznanOnly() );

    }

    @Test
    void shouldCreateBaseInfoDtoOfOffer()
    {
        Instant now = Instant.now();

        //given
        OfferType type = new OfferType( 0L, "Type", Collections.emptyList() );
        Offer offer = new Offer( 0L, "Title", "Desc", null, null,
                SampleDataUtil.getSampleInstitution(), type, now, now.plusSeconds( 2000L ), false,
                null, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, null,
                now.plusSeconds( 1000L ), "periodicDesc", "Place", false, false, null, null);

        //when
        OfferBaseInfoDto baseInfoDtoOfOffer = dtoService.createBaseInfoDtoOfOffer(offer);

        //then
        assertEquals( 0L, baseInfoDtoOfOffer.getId());
        assertEquals( "Title", baseInfoDtoOfOffer.getOfferTitle() );
        assertEquals( "Place", baseInfoDtoOfOffer.getOfferPlace() );
        assertEquals( "Type", baseInfoDtoOfOffer.getOfferTypeName() );
        assertEquals( "Name", baseInfoDtoOfOffer.getInstitutionName() );
        assertEquals( false, baseInfoDtoOfOffer.getIsPoznanOnly() );
        assertEquals( Date.from( now ).toInstant().truncatedTo(ChronoUnit.SECONDS ),
                baseInfoDtoOfOffer.getStartDate().toInstant().truncatedTo(ChronoUnit.SECONDS ) );
        assertEquals( Date.from( now.plusSeconds( 2000L ) ).toInstant().truncatedTo(ChronoUnit.SECONDS ),
                baseInfoDtoOfOffer.getEndDate().toInstant().truncatedTo(ChronoUnit.SECONDS ) );
        assertEquals( Date.from( now.plusSeconds( 1000L ) ).toInstant().truncatedTo(ChronoUnit.SECONDS ),
                baseInfoDtoOfOffer.getOfferExpirationDate().toInstant().truncatedTo(ChronoUnit.SECONDS ) );
    }

    @Test
    void shouldCreateOfferDetailsDto()
    {
        Instant now = Instant.now();

        //given
        User user = new User();
        user.setInstitution( SampleDataUtil.getSampleInstitution() );

        OfferType type = new OfferType( 0L, "Type", Collections.emptyList() );
        Offer offer = new Offer( 0L, "Title", "Desc", user, null,
                SampleDataUtil.getSampleInstitution(), type, now, now.plusSeconds( 2000L ), false,
                null, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                null, null,
                now.plusSeconds( 1000L ), "periodicDesc", "Place", false, false, null, null);

        //when
        OfferDetailsDto offerDetailsDto = dtoService.createOfferDetailsDto( offer );

        //then
        assertEquals( 0L, offerDetailsDto.getId());
        assertEquals( "Title", offerDetailsDto.getOfferTitle() );
        assertEquals( "Title", offerDetailsDto.getOfferTitle() );
        assertEquals( "Place", offerDetailsDto.getOfferPlace() );
        assertEquals( "Type", offerDetailsDto.getOfferType().getName() );
        assertEquals( "Name", offerDetailsDto.getInstitutionName() );
        assertEquals( false, offerDetailsDto.getIsPoznanOnly() );
        assertEquals( "periodicDesc", offerDetailsDto.getPeriodicDescription() );
        assertFalse( offerDetailsDto.getIsPoznanOnly() );
        assertEquals( "Desc", offerDetailsDto.getOfferDescription() );
        assertEquals( Date.from( now ).toInstant().truncatedTo(ChronoUnit.SECONDS ),
                offerDetailsDto.getStartDate().toInstant().truncatedTo(ChronoUnit.SECONDS ) );
        assertEquals( Date.from( now.plusSeconds( 2000L ) ).toInstant().truncatedTo(ChronoUnit.SECONDS ),
                offerDetailsDto.getEndDate().toInstant().truncatedTo(ChronoUnit.SECONDS ) );
        assertEquals( Date.from( now.plusSeconds( 1000L ) ).toInstant().truncatedTo(ChronoUnit.SECONDS ),
                offerDetailsDto.getOfferExpirationDate().toInstant().truncatedTo(ChronoUnit.SECONDS ) );
    }

    @Test
    void shouldCreateApplicationDetailsDto()
    {
        Instant now = Instant.now();

        //given
        OfferType type = new OfferType( 0L, "Type", Collections.emptyList() );
        Offer offer = new Offer( 0L, "Title", "Desc", User.builder().id( 0L ).build(), null,
                SampleDataUtil.getSampleInstitution(), type, now, now.plusSeconds( 2000L ), false,
                null, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, null,
                now.plusSeconds( 1000L ), "periodicDesc", "Place", false, false, null, null);
        VolunteerData volunteerData = VolunteerData.builder().id( 0l ).domainEmailAddress( "test@amu.edu.pl" )
                .participationMotivation( "motivation ").fieldOfStudy( "informatyka" )
                .experience( getSampleExperienceLevel() ).interestCategories( Collections.emptyList() ).build();

        User user = User.builder().id( 1L ).firstName( "name" ).lastName( "surname" ).contactEmailAddress("test@test.pl")
                .volunteerData( volunteerData ).phoneNumber( "000000000" ).build();

        ApplicationState state = new ApplicationState( 3L, "Oczekujące" );
        Application application = new Application( 0L, user, offer, state, "motivation", "reason", false );

        //when

        ApplicationDetailsDto applicationDetailsDto = dtoService.createApplicationDetailsDto(application);

        //then
        assertEquals( 0L, applicationDetailsDto.getId() );
        assertEquals( "Oczekujące", applicationDetailsDto.getState() );
        assertEquals( 0L, applicationDetailsDto.getOfferInfo().getId() );
        assertEquals( "motivation", applicationDetailsDto.getParticipationMotivation() );
        assertEquals( "name", applicationDetailsDto.getFirstName() );
        assertEquals( "surname", applicationDetailsDto.getLastName() );
        assertEquals( "test@test.pl", applicationDetailsDto.getContactEmail() );
        assertEquals( "test@amu.edu.pl", applicationDetailsDto.getDomainEmail() );
        assertEquals( "reason", applicationDetailsDto.getDecisionReason() );
        assertEquals( "000000000", applicationDetailsDto.getPhoneNumber() );
    }

    @Test
    void shouldCreateApplicationBaseInfosDto()
    {
        Instant now = Instant.now();

        //given
        OfferType type = new OfferType( 0L, "Type", Collections.emptyList() );
        Offer offer = new Offer( 0L, "Title", "Desc", null, null,
                SampleDataUtil.getSampleInstitution(), type, now, now.plusSeconds( 2000L ), false,
                null, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, null,
                now.plusSeconds( 1000L ), "periodicDesc", "Place", false, false, null, null);

        User user = User.builder().id( 1L ).firstName( "name" ).lastName( "surname" ).build();

        ApplicationState state = new ApplicationState( 3L, "Oczekujące" );
        Application application = new Application( 0L, user, offer, state, "motivation", "reason", false );

        //when
        ApplicationBaseInfoDto applicationBaseInfosDto = dtoService.createApplicationBaseInfosDto( application );

        //then
        assertEquals( 0L, applicationBaseInfosDto.getId() );
        assertEquals( "Oczekujące", applicationBaseInfosDto.getState() );
        assertEquals( 0L, applicationBaseInfosDto.getOffer().getId() );
        assertEquals( "motivation", applicationBaseInfosDto.getParticipationMotivation() );
        assertEquals( "name", applicationBaseInfosDto.getFirstName() );
        assertEquals( "surname", applicationBaseInfosDto.getLastName() );
    }

    private void assertThatExperienceLevelIsReturned()
    {
        doReturn( Optional.of( getSampleExperienceLevel() ) )
                .when( experienceLevelService ).tryLoadEntity( 0L );
    }

    private static ExperienceLevel getSampleExperienceLevel() {
        return new ExperienceLevel( 0L, "exLevel", "def", 0L, true );
    }

    private void assertThatTypeIsReturned()
    {
        doReturn(new OfferType( 0L, "Type", Collections.emptyList() ) )
                .when( offerTypeService ).loadEntity( 0L );
    }

    private void assertThatStateIsReturned()
    {
        doReturn( Optional.of( new OfferState( 0L, "New" ) ) )
                .when( offerStateService  ).tryLoadByState( OfferStateEnum
                .mapOfferStateEnumToOfferStateName( OfferStateEnum.NEW ) );
    }

    private void assertThatContactPersonIsReturned()
    {
        User user = new User();
        user.setInstitution( SampleDataUtil.getSampleInstitution() );
        doReturn( user ).when( userService ).loadEntity( 0L );
    }
}