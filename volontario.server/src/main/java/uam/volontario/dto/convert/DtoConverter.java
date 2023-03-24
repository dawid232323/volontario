package uam.volontario.dto.convert;

import lombok.experimental.UtilityClass;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.model.volunteer.impl.VolunteerExperience;

import java.util.Set;
import java.util.stream.Collectors;

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
    public User createVolunteerFromDto( final VolunteerDto aDto )
    {
        final Role volunteerRole = Role.builder()
                .name( "role::volunteer" ) // TODO: roles are yet to be discussed.
                .build();

        final Set< InterestCategory > volunteerInterestCategories = aDto.getInterestCategories().stream()
                .map( dto -> InterestCategory.builder()
                        .name( dto.getName() )
                        .description( dto.getDescription() )
                        .build() )
                .collect( Collectors.toSet() );

        final VolunteerExperience volunteerExperience = VolunteerExperience.builder()
                .name( aDto.getExperience().getName() )
                .definition( aDto.getExperience().getDefinition() )
                .build();

        final VolunteerData volunteerData = VolunteerData.builder()
                .experience( volunteerExperience )
                .participationMotivation( aDto.getParticipationMotivation() )
                .interestCategories( volunteerInterestCategories ).build();

        return User.builder().firstName( aDto.getFirstName() )
                .lastName( aDto.getLastName() )
                .hashedPassword( aDto.getPassword() ) // TODO: once Spring Security is brought back, we need to pass hashed password here.
                .domainEmailAddress( aDto.getDomainEmail() )
                .contactEmailAddress( aDto.getContactEmail() )
                .phoneNumber( aDto.getPhoneNumber() )
                .role( volunteerRole )
                .isVerified( true ) // TODO: for now until email verification is implemented.
                .volunteerData( volunteerData )
                .build();
    }
}
