package uam.volontario.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.crud.service.VolunteerExperienceService;
import uam.volontario.data.experience.BaseExperienceLevel;
import uam.volontario.data.interests.BaseInterestType;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.model.volunteer.impl.VolunteerExperience;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Loads basic example data into the database on initialization.
 *
 * @deprecated needs to be removed ASAP when database topics are resolved.
 */
@Configuration
@Deprecated
public class OnStartupDataInitializer implements ApplicationRunner
{
    private final InterestCategoryService interestCategoryService;
    private final VolunteerExperienceService volunteerExperienceService;

    @Autowired
    public OnStartupDataInitializer( InterestCategoryService aInterestCategoryService,
                                     VolunteerExperienceService aVolunteerExperienceService)
    {
        interestCategoryService = aInterestCategoryService;
        volunteerExperienceService = aVolunteerExperienceService;
    }

    public void run( ApplicationArguments args )
    {
        initializeBaseInterestCategories();
        initializeBaseExperienceLevels();
    }

    private void initializeBaseInterestCategories() {
        List< Long > presentBaseCategoriesIds = interestCategoryService.findByIds( Arrays.stream( BaseInterestType.values() )
                .map( aType -> aType.getInterestCategory()
                        .getId() )
                .collect( Collectors.toList() ) ).stream().map( InterestCategory::getId ).toList();
        for( BaseInterestType interest : BaseInterestType.values() )
        {
            if( !presentBaseCategoriesIds.contains( interest.getInterestCategory()
                .getId() ) )
            {
                interestCategoryService.saveOrUpdate( interest.getInterestCategory() );
            }
        }
    }

    private void initializeBaseExperienceLevels() {
        List< Long > presentBaseCategoriesIds = volunteerExperienceService.loadAllEntities().stream()
                .map( VolunteerExperience::getId ).toList();
        for( BaseExperienceLevel experienceLevel : BaseExperienceLevel.values() )
        {
            if( !presentBaseCategoriesIds.contains( experienceLevel.getVolunteerExperience().getId()) )
            {
                volunteerExperienceService.saveOrUpdate( experienceLevel.getVolunteerExperience() );
            }
        }
    }
}
