package uam.volontario;

import lombok.experimental.UtilityClass;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.OfferState;
import uam.volontario.model.offer.impl.OfferType;
import uam.volontario.model.volunteer.impl.ExperienceLevel;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.model.volunteer.impl.VolunteerData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class SampleDataUtil
{
    public static Institution getSampleInstitution()
    {
        InstitutionContactPerson expectedCPEntity = new InstitutionContactPerson( 0L, null, "Test", "Test", "000000000", "test@test.test");

        Institution institution = new Institution(0L, "Name", "Desc", "HQ", "Loc", "123",
                Collections.emptyList(), Collections.emptyList(), expectedCPEntity, null, false, null, null);
        expectedCPEntity.setInstitution( institution );
        return institution;
    }

    public static OfferState getSampleOfferState()
    {
        return new OfferState( 0L, "Nowe" );
    }

    public static InterestCategory getSampleInterestCategory()
    {
        return new InterestCategory( 0L, "Testowa kategoria", "Test", Collections.emptyList(),
                Collections.emptyList(), true );
    }

    public static ExperienceLevel getSampleExperienceLeve()
    {
        return new ExperienceLevel( 0L, "Początkujący", "Początkujący", 0L, true );
    }

    public static User.UserBuilder prepareUserBuilderWithCorrectVolunteerData()
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
                .roles( List.of( new Role( 0L, "Wolontariusz(ka)", new ArrayList<>() ) ) )
                .isVerified( true );
    }

    public static User.UserBuilder prepareGenericUserBuilderWithCorrectData()
    {
        return User.builder().firstName( "Jan" )
                .lastName( "Kowalski" )
                .hashedPassword( "aafsaios" )
                .contactEmailAddress( "contact@wp.pl" )
                .password( "ABCdef123_" )
                .phoneNumber( "123456789" )
                .roles( Collections.emptyList() )
                .isVerified( true );
    }

    public static Offer getSampleOffer()
    {
        Instant now = Instant.now();

        User user = new User();
        user.setInstitution( getSampleInstitution() );
        OfferType type = new OfferType( 0L, "Type", Collections.emptyList() );

        return new Offer( 0L, "Title", "Desc", user, null,
                getSampleInstitution(), type, now, now.plusSeconds( 1000L ), false,
                null, SampleDataUtil.getSampleOfferState(), List.of( SampleDataUtil.getSampleInterestCategory() ),
                Collections.emptyList(), null, null, now.plusSeconds( 2000L ), "periodicDesc",
                "Place", false, false);
    }
}
