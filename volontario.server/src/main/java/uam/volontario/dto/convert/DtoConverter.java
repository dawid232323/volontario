package uam.volontario.dto.convert;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.crud.service.VolunteerExperienceService;
import uam.volontario.dto.InterestCategoryDto;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.dto.VolunteerExperienceDto;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.model.volunteer.impl.VolunteerExperience;

import java.util.List;

/**
 * Utility class used for converting DTOs.
 */
@Component
public class DtoConverter
{
    @Autowired
    private InterestCategoryService interestCategoryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VolunteerExperienceService volunteerExperienceService;

    /**
     * Creates Volunteer {@linkplain User} from DTO.
     *
     * @param aDto dto.
     *
     * @return Volunteer.
     */
    public User createVolunteerFromDto( final VolunteerDto aDto  )
    {
        final Role volunteerRole = Role.builder()
                .name( "role::volunteer" ) // TODO: roles are yet to be discussed.
                .build();

        final VolunteerExperience volunteerExperience = volunteerExperienceService.loadEntity( aDto.getExperienceId() );
        final List< InterestCategory > volunteerInterestCategories = interestCategoryService.findByIds( aDto.getInterestCategoriesIds() );

        final VolunteerData volunteerData = VolunteerData.builder()
                .experience( volunteerExperience )
                .participationMotivation( aDto.getParticipationMotivation() )
                .interestCategories( Sets.newHashSet( volunteerInterestCategories ) ).build();

        return User.builder().firstName( aDto.getFirstName() )
                .lastName( aDto.getLastName() )
                .hashedPassword( passwordEncoder.encode( aDto.getPassword() ) )
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

    /**
     * Maps {@linkplain VolunteerExperience} to its DTO.
     *
     * @param aVolunteerExperience volunteer experience level to map.
     *
     * @return dto.
     */
    public VolunteerExperienceDto volunteerExperienceToDto( final VolunteerExperience aVolunteerExperience )
    {
        return new VolunteerExperienceDto( aVolunteerExperience.getName(), aVolunteerExperience.getDefinition() );
    }
}
