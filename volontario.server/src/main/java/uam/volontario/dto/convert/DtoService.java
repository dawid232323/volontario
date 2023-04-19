package uam.volontario.dto.convert;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.ExperienceLevelService;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.crud.service.RoleService;
import uam.volontario.dto.*;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.model.volunteer.impl.ExperienceLevel;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.model.volunteer.impl.VolunteerData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for DTO operations.
 */
@Service
public class DtoService
{
    private final InterestCategoryService interestCategoryService;

    private final ExperienceLevelService experienceLevelService;

    private final RoleService roleService;

    /**
     * CDI constructor.
     *
     * @param aInterestCategoryService interest category service.
     *
     * @param aExperienceLevelService experience level service.
     */
    @Autowired
    public DtoService( final InterestCategoryService aInterestCategoryService, final ExperienceLevelService
            aExperienceLevelService, final RoleService aRoleService )
    {
        interestCategoryService = aInterestCategoryService;
        experienceLevelService = aExperienceLevelService;
        roleService = aRoleService;
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
        final List< Role > roles = roleService.findByNameIn( UserRole.mapUserRolesToRoleNames( List.of( UserRole.VOLUNTEER ) ) );

        final ExperienceLevel experienceLevel = experienceLevelService.tryLoadEntity( aDto.getExperienceId() )
                .orElseThrow();

        final List< InterestCategory > volunteerInterestCategories = interestCategoryService.findByIds(
                aDto.getInterestCategoriesIds() );

        final VolunteerData volunteerData = VolunteerData.builder()
                .experience( experienceLevel )
                .participationMotivation( aDto.getParticipationMotivation() )
                .domainEmailAddress( aDto.getDomainEmail() )
                .interestCategories( volunteerInterestCategories ).build();

        final User user = User.builder().firstName( aDto.getFirstName() )
                .lastName( aDto.getLastName() )
                .password( aDto.getPassword() )
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
     * Creates Volunteer {@linkplain Institution} from DTO.
     *
     * @param aDto dto.
     *
     * @return Institution.
     */
    public Institution createInstitutionFromDto( final InstitutionDto aDto )
    {
        final InstitutionContactPersonDto contactPersonDto = aDto.getContactPerson();
        final InstitutionContactPerson contactPerson = InstitutionContactPerson.builder()
                .firstName( contactPersonDto.getFirstName() )
                .lastName( contactPersonDto.getLastName() )
                .contactEmail( contactPersonDto.getContactEmail() )
                .phoneNumber( contactPersonDto.getPhoneNumber() )
                .build();

        return Institution.builder()
                .name( aDto.getName() )
                .description( aDto.getDescription() )
                .localization( aDto.getLocalization() )
                .headquarters( aDto.getHeadquartersAddress() )
                .krsNumber( aDto.getKrsNumber() )
                .isActive( false )
                .registrationToken( RandomStringUtils.randomAlphanumeric( 64 ) )
                .institutionContactPerson( contactPerson )
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
     * Maps {@linkplain ExperienceLevel} to its DTO.
     *
     * @param aExperienceLevel volunteer experience level to map.
     *
     * @return dto.
     */
    public ExperienceLevelDto volunteerExperienceToDto( final ExperienceLevel aExperienceLevel )
    {
        return new ExperienceLevelDto(aExperienceLevel.getId(), aExperienceLevel.getName(),
                aExperienceLevel.getDefinition(), aExperienceLevel.getValue() );
    }
}
