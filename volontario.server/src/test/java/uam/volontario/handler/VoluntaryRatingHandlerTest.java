package uam.volontario.handler;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uam.volontario.crud.service.InstitutionService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.UserService;
import uam.volontario.crud.service.VoluntaryRatingService;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.dto.rating.InstitutionRatingDataDto;
import uam.volontario.dto.rating.InstitutionRatingDto;
import uam.volontario.dto.rating.VolunteerRatingDataDto;
import uam.volontario.dto.rating.VolunteerRatingDto;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.VoluntaryPresence;
import uam.volontario.model.offer.impl.VoluntaryPresenceStateEnum;
import uam.volontario.model.offer.impl.VoluntaryRating;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@RunWith( MockitoJUnitRunner.Silent.class )
class VoluntaryRatingHandlerTest
{
    @Spy
    private UserService userService;

    @Spy
    private OfferService offerService;

    @Spy
    private VoluntaryRatingService voluntaryRatingService;

    @Spy
    private InstitutionService institutionService;

    private DtoService dtoService = spy( new DtoService( null, null,
            null, null, null, null ) );

    @Spy
    @InjectMocks
    private VoluntaryRatingHandler voluntaryRatingHandler;

    @BeforeEach
    public void injectMocks()
    {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    public void rateVolunteerTest()
    {
        // given
        final User volunteer = spy( User.builder().build() );

        volunteer.setId( 1L );
        doReturn( Optional.of( volunteer ) ).when( userService ).tryToFindById( 1L );
        doReturn( List.of( UserRole.VOLUNTEER ) ).when( volunteer ).getUserRoles();

        final Institution institution = Institution.builder()
                .id( 1L )
                .build();

        final Offer offer = spy( Offer.builder()
                .institution( institution )
                .build() );

        offer.setId( 1L );
        doReturn( Optional.of( offer ) ).when( offerService ).tryLoadEntity( 1L );

        final VoluntaryPresence voluntaryPresence = spy( VoluntaryPresence.builder()
                .offer( offer )
                .volunteer( volunteer )
                .build() );

        doReturn( VoluntaryPresenceStateEnum.CONFIRMED ).when( voluntaryPresence ).getVolunteerReportedPresenceStateAsEnum();
        doReturn( VoluntaryPresenceStateEnum.CONFIRMED ).when( voluntaryPresence ).getInstitutionReportedPresenceStateAsEnum();

        doReturn( List.of( voluntaryPresence ) ).when( volunteer ).getVoluntaryPresences();

        doReturn( Optional.empty() ).when( voluntaryRatingService ).findByOfferAndVolunteer( offer, volunteer );

        final List< VoluntaryRating > savedRatings = Lists.newArrayList();
        doAnswer( inv -> {
            savedRatings.add( inv.getArgument( 0 ) );
            return savedRatings.get( 0 );
        } ).when( voluntaryRatingService ).saveOrUpdate( any( VoluntaryRating.class ) );

        // when
        voluntaryRatingHandler.rateVolunteer( volunteer.getId(), offer.getId(), 3, "testRating", true );

        // then
        final VoluntaryRating voluntaryRating = savedRatings.get( 0 );

        assertEquals( volunteer, voluntaryRating.getVolunteer() );
        assertEquals( offer, voluntaryRating.getOffer() );
        assertEquals( institution, voluntaryRating.getInstitution() );
        assertEquals( 3, voluntaryRating.getVolunteerRating() );
        assertEquals( "testRating", voluntaryRating.getVolunteerRatingReason() );
    }

    @Test
    public void rateInstitutionTest()
    {
        // given
        final User volunteer = spy( User.builder().build() );

        volunteer.setId( 1L );
        doReturn( Optional.of( volunteer ) ).when( userService ).tryToFindById( 1L );
        doReturn( List.of( UserRole.VOLUNTEER ) ).when( volunteer ).getUserRoles();

        final Institution institution = Institution.builder()
                .id( 1L )
                .build();

        doReturn( Optional.of( institution ) ).when( institutionService ).tryLoadEntity( 1L );

        final Offer offer = spy( Offer.builder()
                .institution( institution )
                .build() );

        offer.setId( 1L );
        doReturn( Optional.of( offer ) ).when( offerService ).tryLoadEntity( 1L );

        final VoluntaryPresence voluntaryPresence = spy( VoluntaryPresence.builder()
                .offer( offer )
                .volunteer( volunteer )
                .build() );

        doReturn( VoluntaryPresenceStateEnum.CONFIRMED ).when( voluntaryPresence ).getVolunteerReportedPresenceStateAsEnum();
        doReturn( VoluntaryPresenceStateEnum.CONFIRMED ).when( voluntaryPresence ).getInstitutionReportedPresenceStateAsEnum();

        doReturn( List.of( voluntaryPresence ) ).when( volunteer ).getVoluntaryPresences();

        doReturn( Optional.empty() ).when( voluntaryRatingService ).findByOfferAndVolunteer( offer, volunteer );

        final List< VoluntaryRating > savedRatings = Lists.newArrayList();
        doAnswer( inv -> {
            savedRatings.add( inv.getArgument( 0 ) );
            return savedRatings.get( 0 );
        } ).when( voluntaryRatingService ).saveOrUpdate( any( VoluntaryRating.class ) );

        // when
        voluntaryRatingHandler.rateInstitution( volunteer.getId(), institution.getId(), offer.getId(), 3, "testRating", true );

        // then
        final VoluntaryRating voluntaryRating = savedRatings.get( 0 );

        assertEquals( volunteer, voluntaryRating.getVolunteer() );
        assertEquals( offer, voluntaryRating.getOffer() );
        assertEquals( institution, voluntaryRating.getInstitution() );
        assertEquals( 3, voluntaryRating.getInstitutionRating() );
        assertEquals( "testRating", voluntaryRating.getInstitutionRatingReason() );
    }

    @Test
    public void resolveVolunteerRatingData()
    {
        // given
        final User volunteer = spy( User.builder().build() );

        volunteer.setId( 1L );
        volunteer.setFirstName( "X" );
        volunteer.setLastName( "Y" );
        doReturn( Optional.of( volunteer ) ).when( userService ).tryToFindById( 1L );
        doReturn( List.of( UserRole.VOLUNTEER ) ).when( volunteer ).getUserRoles();

        final Institution institution = Institution.builder()
                .id( 1L )
                .build();

        doReturn( Optional.of( institution ) ).when( institutionService ).tryLoadEntity( 1L );

        final VoluntaryRating voluntaryRating1 = VoluntaryRating.builder()
                .institution( institution )
                .volunteer( volunteer )
                .volunteerRating( 5 )
                .offer( Offer.builder().title( "O1" ).id( 1L ).contactPerson( User.builder().firstName( "x" ).lastName( "y" ).build() ).build() )
                .build();

        final VoluntaryRating voluntaryRating2 = VoluntaryRating.builder()
                .institution( institution )
                .volunteerRating( 3 )
                .volunteer( volunteer )
                .offer( Offer.builder().title( "O2" ).id( 2L ).contactPerson( User.builder().firstName( "x" ).lastName( "y" ).build() ).build() )
                .build();

        final VoluntaryRating voluntaryRating3 = VoluntaryRating.builder()
                .institution( institution )
                .institutionRating( 5 )
                .volunteer( volunteer )
                .offer( Offer.builder().title( "O3" ).id( 3L ).contactPerson( User.builder().firstName( "x" ).lastName( "y" ).build() ).build() )
                .build();

        final VoluntaryRating voluntaryRating4 = VoluntaryRating.builder()
                .institution( institution )
                .volunteer( volunteer )
                .offer( Offer.builder().title( "O4" ).id( 4L ).contactPerson( User.builder().firstName( "x" ).lastName( "y" ).build() ).build() )
                .build();

        doReturn( List.of( voluntaryRating1, voluntaryRating2, voluntaryRating3, voluntaryRating4 ) ).when( voluntaryRatingService )
                .findAllByVolunteer( volunteer );

        // when
        final ResponseEntity< ? > responseEntity = voluntaryRatingHandler.resolveVolunteerRatingData( volunteer.getId() );

        // then
        if( responseEntity.getBody() instanceof VolunteerRatingDataDto volunteerRatingDataDto )
        {
            assertEquals( 4, volunteerRatingDataDto.getAverageRating(), 0.0001 );
            assertEquals( 2, volunteerRatingDataDto.getVolunteerRatings().size() );

            final List< VolunteerRatingDto > volunteerRatingDtos = volunteerRatingDataDto.getVolunteerRatings();

            assertEquals( 5, volunteerRatingDtos.get( 0 ).getRating() );
            assertEquals( "O1", volunteerRatingDtos.get( 0 ).getOfferName() );

            assertEquals( 3, volunteerRatingDtos.get( 1 ).getRating() );
            assertEquals( "O2", volunteerRatingDtos.get( 1 ).getOfferName() );
        }
        else
        {
            fail();
        }
    }

    @Test
    public void resolveInstitutionRatingData()
    {
        // given
        final User volunteer = spy( User.builder().build() );

        volunteer.setId( 1L );
        volunteer.setFirstName( "" );
        volunteer.setLastName( "Y" );
        doReturn( Optional.of( volunteer ) ).when( userService ).tryToFindById( 1L );
        doReturn( List.of( UserRole.VOLUNTEER ) ).when( volunteer ).getUserRoles();

        final Institution institution = Institution.builder()
                .id( 1L )
                .build();

        doReturn( Optional.of( institution ) ).when( institutionService ).tryLoadEntity( 1L );

        final VoluntaryRating voluntaryRating1 = VoluntaryRating.builder()
                .institution( institution )
                .volunteer( volunteer )
                .institutionRating( 5 )
                .offer( Offer.builder().title( "O1" ).id( 1L ).contactPerson( User.builder().firstName( "x" ).lastName( "y" ).build() ).build() )
                .build();

        final VoluntaryRating voluntaryRating2 = VoluntaryRating.builder()
                .institution( institution )
                .institutionRating( 3 )
                .volunteer( volunteer )
                .offer( Offer.builder().title( "O2" ).id( 2L ).contactPerson( User.builder().firstName( "x" ).lastName( "y" ).build() ).build() )
                .build();

        final VoluntaryRating voluntaryRating3 = VoluntaryRating.builder()
                .institution( institution )
                .volunteerRating( 5 )
                .volunteer( volunteer )
                .offer( Offer.builder().title( "O3" ).id( 3L ).contactPerson( User.builder().firstName( "x" ).lastName( "y" ).build() ).build() )
                .build();

        final VoluntaryRating voluntaryRating4 = VoluntaryRating.builder()
                .institution( institution )
                .volunteer( volunteer )
                .offer( Offer.builder().title( "O4" ).id( 4L ).contactPerson( User.builder().firstName( "x" ).lastName( "y" ).build() ).build() )
                .build();

        doReturn( List.of( voluntaryRating1, voluntaryRating2, voluntaryRating3, voluntaryRating4 ) ).when( voluntaryRatingService )
                .findAllByInstitution( institution );

        // when
        final ResponseEntity< ? > responseEntity = voluntaryRatingHandler.resolveInstitutionRatingData( institution.getId() );

        // then
        if( responseEntity.getBody() instanceof InstitutionRatingDataDto institutionRatingDataDto )
        {
            assertEquals( 4, institutionRatingDataDto.getAverageRating(), 0.0001 );
            assertEquals( 2, institutionRatingDataDto.getInstitutionRatings().size() );

            final List< InstitutionRatingDto > institutionRatingDtos = institutionRatingDataDto.getInstitutionRatings();

            assertEquals( 5, institutionRatingDtos.get( 0 ).getRating() );
            assertEquals( "O1", institutionRatingDtos.get( 0 ).getOfferName() );
            assertEquals( volunteer.getFullName(), institutionRatingDtos.get( 0 ).getVolunteerName() );

            assertEquals( 3, institutionRatingDtos.get( 1 ).getRating() );
            assertEquals( "O2", institutionRatingDtos.get( 1 ).getOfferName() );
            assertEquals( volunteer.getFullName(), institutionRatingDtos.get( 0 ).getVolunteerName() );
        }
        else
        {
            fail();
        }
    }

    @Test
    public void resolveOffersLeftToRateForVolunteer()
    {
        // given
        final User volunteer = spy( User.builder().build() );

        volunteer.setId( 1L );
        volunteer.setFirstName( "" );
        volunteer.setLastName( "Y" );
        doReturn( Optional.of( volunteer ) ).when( userService ).tryToFindById( 1L );
        doReturn( List.of( UserRole.VOLUNTEER ) ).when( volunteer ).getUserRoles();

        final Offer offer1 = spy( Offer.builder().id( 1L ).title( "OFFER 1" ).build() );
        final Offer offer2 = spy( Offer.builder().id( 2L ).title( "OFFER 2" ).build() );
        final Offer offer3 = spy( Offer.builder().id( 3L ).title( "OFFER 3" ).build() );
        final Offer offer4 = spy( Offer.builder().id( 4L ).title( "OFFER 4" ).build() );
        final Offer offer5 = spy( Offer.builder().id( 5L ).title( "OFFER 5" ).build() );

        final VoluntaryPresence voluntaryPresence1 = spy( VoluntaryPresence.builder().volunteer( volunteer ).offer( offer1 ).build() );
        final VoluntaryPresence voluntaryPresence2 = spy( VoluntaryPresence.builder().volunteer( volunteer ).offer( offer2 ).build() );
        final VoluntaryPresence voluntaryPresence3 = spy( VoluntaryPresence.builder().volunteer( volunteer ).offer( offer3 ).build() );
        final VoluntaryPresence voluntaryPresence4 = spy( VoluntaryPresence.builder().volunteer( volunteer ).offer( offer4 ).build() );
        final VoluntaryPresence voluntaryPresence5 = spy( VoluntaryPresence.builder().volunteer( volunteer ).offer( offer5 ).build() );

        doReturn( List.of( voluntaryPresence1, voluntaryPresence2, voluntaryPresence3, voluntaryPresence4, voluntaryPresence5 ) )
                .when( volunteer ).getVoluntaryPresences();

        List.of( voluntaryPresence1, voluntaryPresence2, voluntaryPresence3, voluntaryPresence4, voluntaryPresence5 ).forEach( voluntaryPresence -> {
            doReturn( VoluntaryPresenceStateEnum.CONFIRMED ).when( voluntaryPresence ).getVolunteerReportedPresenceStateAsEnum();
            doReturn( VoluntaryPresenceStateEnum.CONFIRMED ).when( voluntaryPresence ).getInstitutionReportedPresenceStateAsEnum();
        } );

        doReturn( VoluntaryPresenceStateEnum.DENIED ).when( voluntaryPresence5 ).getInstitutionReportedPresenceStateAsEnum();

        final VoluntaryRating voluntaryRating1 = spy( VoluntaryRating.builder().volunteer( volunteer ).offer( offer1 ) ).build();
        final VoluntaryRating voluntaryRating2 = spy( VoluntaryRating.builder().volunteer( volunteer ).offer( offer2 ) ).build();
        final VoluntaryRating voluntaryRating3 = spy( VoluntaryRating.builder().volunteer( volunteer ).offer( offer3 ) ).build();
        final VoluntaryRating voluntaryRating4 = spy( VoluntaryRating.builder().volunteer( volunteer ).offer( offer4 ) ).build();

        doReturn( List.of( voluntaryRating1 ) ).when( offer1 ).getVoluntaryRatings();
        doReturn( List.of( voluntaryRating2 ) ).when( offer2 ).getVoluntaryRatings();
        doReturn( List.of( voluntaryRating3 ) ).when( offer3 ).getVoluntaryRatings();
        doReturn( List.of( voluntaryRating4 ) ).when( offer4 ).getVoluntaryRatings();

        voluntaryRating4.setInstitutionRating( 5 );

        // when
        final ResponseEntity< ? > response = voluntaryRatingHandler.resolveOffersLeftToRateForVolunteer( volunteer.getId() );

        // then
        if( response.getBody() instanceof Map aMap )
        {
            final Set< Offer > offersLeftToRate = ((List<Offer>) aMap.get( "offersLeftToRate" )).stream().collect(Collectors.toSet());
            assertEquals( Set.of( offer1, offer2, offer3 ), offersLeftToRate );
        }
        else
        {
            fail();
        }
    }

    @Test
    public void resolveOffersLeftToRateForInstitution()
    {
        // given
        final User volunteer = spy( User.builder().build() );

        volunteer.setId( 1L );
        volunteer.setFirstName( "" );
        volunteer.setLastName( "Y" );
        doReturn( Optional.of( volunteer ) ).when( userService ).tryToFindById( 1L );
        doReturn( List.of( UserRole.VOLUNTEER ) ).when( volunteer ).getUserRoles();

        final Institution institution = Institution.builder()
                .id( 1L )
                .build();

        doReturn( Optional.of( institution ) ).when( institutionService ).tryLoadEntity( 1L );

        final Offer offer1 = spy( Offer.builder().id( 1L ).institution( institution ).title( "OFFER 1" ).build() );
        final Offer offer2 = spy( Offer.builder().id( 2L ).institution( institution ).title( "OFFER 2" ).build() );
        final Offer offer3 = spy( Offer.builder().id( 3L ).institution( mock( Institution.class ) ).title( "OFFER 3" ).build() );
        final Offer offer4 = spy( Offer.builder().id( 4L ).institution( institution ).title( "OFFER 4" ).build() );
        final Offer offer5 = spy( Offer.builder().id( 5L ).institution( institution ).title( "OFFER 5" ).build() );

        final VoluntaryPresence voluntaryPresence1 = spy( VoluntaryPresence.builder().volunteer( volunteer ).offer( offer1 ).build() );
        final VoluntaryPresence voluntaryPresence2 = spy( VoluntaryPresence.builder().volunteer( volunteer ).offer( offer2 ).build() );
        final VoluntaryPresence voluntaryPresence3 = spy( VoluntaryPresence.builder().volunteer( volunteer ).offer( offer3 ).build() );
        final VoluntaryPresence voluntaryPresence4 = spy( VoluntaryPresence.builder().volunteer( volunteer ).offer( offer4 ).build() );
        final VoluntaryPresence voluntaryPresence5 = spy( VoluntaryPresence.builder().volunteer( volunteer ).offer( offer5 ).build() );

        doReturn( List.of( voluntaryPresence1, voluntaryPresence2, voluntaryPresence3, voluntaryPresence4, voluntaryPresence5 ) )
                .when( volunteer ).getVoluntaryPresences();

        List.of( voluntaryPresence1, voluntaryPresence2, voluntaryPresence3, voluntaryPresence4, voluntaryPresence5 ).forEach( voluntaryPresence -> {
            doReturn( VoluntaryPresenceStateEnum.CONFIRMED ).when( voluntaryPresence ).getVolunteerReportedPresenceStateAsEnum();
            doReturn( VoluntaryPresenceStateEnum.CONFIRMED ).when( voluntaryPresence ).getInstitutionReportedPresenceStateAsEnum();
        } );

        doReturn( VoluntaryPresenceStateEnum.DENIED ).when( voluntaryPresence5 ).getInstitutionReportedPresenceStateAsEnum();

        final VoluntaryRating voluntaryRating1 = spy( VoluntaryRating.builder().volunteer( volunteer ).offer( offer1 ) ).build();
        final VoluntaryRating voluntaryRating2 = spy( VoluntaryRating.builder().volunteer( volunteer ).offer( offer2 ) ).build();
        final VoluntaryRating voluntaryRating3 = spy( VoluntaryRating.builder().volunteer( volunteer ).offer( offer3 ) ).build();
        final VoluntaryRating voluntaryRating4 = spy( VoluntaryRating.builder().volunteer( volunteer ).offer( offer4 ) ).build();

        doReturn( List.of( voluntaryRating1 ) ).when( offer1 ).getVoluntaryRatings();
        doReturn( List.of( voluntaryRating2 ) ).when( offer2 ).getVoluntaryRatings();
        doReturn( List.of( voluntaryRating3 ) ).when( offer3 ).getVoluntaryRatings();
        doReturn( List.of( voluntaryRating4 ) ).when( offer4 ).getVoluntaryRatings();

        voluntaryRating4.setVolunteerRating( 5 );

        // when
        final ResponseEntity< ? > response = voluntaryRatingHandler.resolveOffersLeftToRateForInstitutionForGivenVolunteer(
                institution.getId(), volunteer.getId() );

        // then
        if( response.getBody() instanceof Map aMap )
        {
            final Set< Offer > offersLeftToRate = ((List<Offer>) aMap.get( "offersLeftToRate" )).stream().collect(Collectors.toSet());
            assertEquals( Set.of( offer1, offer2 ), offersLeftToRate );
        }
        else
        {
            fail();
        }
    }
}