package uam.volontario.dto.convert;

import com.google.common.collect.Sets;
import lombok.experimental.UtilityClass;
import uam.volontario.dto.InterestCategoryDto;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.model.volunteer.impl.VolunteerExperience;

import java.util.List;
import java.util.function.Function;

/**
 * Utility class used for converting DTOs.
 */
@UtilityClass
public class DtoConverter
{
    /**
     * Creates Volunteer {@linkplain User} from DTO.
     *
     * @param aDto dto.
     *
     * @return Volunteer.
     */
    public User createVolunteerFromDto( final VolunteerDto aDto, final Function< List< Long >,
            List< InterestCategory > > aInterestCategoryFetcher, final Function< String, String > aEncodePassword )
    {
        final Role volunteerRole = Role.builder()
                .name( "role::volunteer" ) // TODO: roles are yet to be discussed.
                .build();

        final VolunteerExperience volunteerExperience = VolunteerExperience.builder()
                .name( aDto.getExperience().getName() )
                .definition( aDto.getExperience().getDefinition() )
                .build();

        final List< InterestCategory > volunteerInterestCategories
                = aInterestCategoryFetcher.apply( aDto.getInterestCategoriesIds() );

        final VolunteerData volunteerData = VolunteerData.builder()
                .experience( volunteerExperience )
                .participationMotivation( aDto.getParticipationMotivation() )
                .interestCategories( Sets.newHashSet( volunteerInterestCategories ) ).build();

        return User.builder().firstName( aDto.getFirstName() )
                .lastName( aDto.getLastName() )
                .hashedPassword( aEncodePassword.apply( aDto.getPassword() ) )
                .domainEmailAddress( aDto.getDomainEmail() )
                .contactEmailAddress( aDto.getContactEmail() )
                .phoneNumber( aDto.getPhoneNumber() )
                .role( volunteerRole )
                .isVerified( true ) // TODO: for now until email verification is implemented.
                .volunteerData( volunteerData )
                .build();
    }

    /**
     * Maps {@linkplain InterestCategory} to its DTO.
     *
     * @param aInterestCategory interest category to map.
     *
     * @return dto.
     */
    public InterestCategoryDto interestCategoryToDto( final InterestCategory aInterestCategory )
    {
        return new InterestCategoryDto( aInterestCategory.getId(),
                aInterestCategory.getName(), aInterestCategory.getDescription() );
    }
}
