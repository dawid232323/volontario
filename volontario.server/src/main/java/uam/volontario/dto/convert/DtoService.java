package uam.volontario.dto.convert;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.ExperienceLevelService;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.dto.ExperienceLevelDto;
import uam.volontario.dto.InterestCategoryDto;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.volunteer.impl.ExperienceLevel;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.model.volunteer.impl.VolunteerData;

import java.util.List;
import java.util.Set;

/**
 * Service for DTO operations.
 */
@Service
public class DtoService
{
    private final InterestCategoryService interestCategoryService;

    private final ExperienceLevelService experienceLevelService;

    /**
     * CDI constructor.
     *
     * @param aInterestCategoryService interest category service.
     *
     * @param aExperienceLevelService experience level service.
     */
    @Autowired
    public DtoService( final InterestCategoryService aInterestCategoryService, final ExperienceLevelService
            aExperienceLevelService )
    {
        interestCategoryService = aInterestCategoryService;
        experienceLevelService = aExperienceLevelService;
    }

    /**
     * Creates Volunteer {@linkplain User} from DTO.
     *
     * @param aDto dto.
     *
     * @return Volunteer.
     */
    public User createVolunteerFromDto( final VolunteerDto aDto )
    {
        final Set< Role > roles = Sets.newHashSet(); // TODO: change once roles are imported in the system.

        final ExperienceLevel experienceLevel = experienceLevelService.tryLoadEntity( aDto.getExperienceId() )
                .orElse( null ); // TODO: change once basic experience levels are imported in the system.

        final List< InterestCategory > volunteerInterestCategories = interestCategoryService.findByIds(
                aDto.getInterestCategoriesIds() );

        final VolunteerData volunteerData = VolunteerData.builder()
                .experience( experienceLevel )
                .participationMotivation( aDto.getParticipationMotivation() )
                .interestCategories( Sets.newHashSet( volunteerInterestCategories ) ).build();

        final User user = User.builder().firstName( aDto.getFirstName() )
                .lastName( aDto.getLastName() )
                .password( aDto.getPassword() )
                .domainEmailAddress( aDto.getDomainEmail() )
                .contactEmailAddress( aDto.getContactEmail() )
                .phoneNumber( aDto.getPhoneNumber() )
                .roles( roles )
                .isVerified( true ) // TODO: for now until email verification is implemented.
                .volunteerData( volunteerData )
                .build();

        user.getVolunteerData().setUser( user );

        return user;
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
     * Maps {@linkplain ExperienceLevel} to its DTO.
     *
     * @param aExperienceLevel volunteer experience level to map.
     *
     * @return dto.
     */
    public ExperienceLevelDto volunteerExperienceToDto( final ExperienceLevel aExperienceLevel )
    {
        return new ExperienceLevelDto( aExperienceLevel.getName(), aExperienceLevel.getDefinition(),
                aExperienceLevel.getValue() );
    }
}
