package uam.volontario.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.ExperienceLevelService;
import uam.volontario.crud.service.InterestCategoryService;
import uam.volontario.crud.service.UserService;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.dto.VolunteerPatchInfoDto;
import uam.volontario.dto.convert.DtoService;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.rest.VolunteerController;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.entity.UserValidationService;

import java.util.Optional;

/**
 * Service for handling operations related to {@linkplain uam.volontario.model.common.impl.User}s of type
 * {@linkplain uam.volontario.model.common.UserRole#VOLUNTEER}.
 */
@Service
public class VolunteerHandler
{
    private final DtoService dtoService;

    private final UserService userService;

    private final UserValidationService userValidationService;

    private final InterestCategoryService interestCategoryService;

    private final ExperienceLevelService experienceLevelService;

    private final PasswordEncoder passwordEncoder;

    /**
     * CDI constructor.
     *
     * @param aUserValidationService user validation service.
     *
     * @param aDtoService dto service.
     *
     * @param aUserService user service.
     *
     * @param aPasswordEncoder password encoder.
     *
     * @param aInterestCategoryService interest category service.
     *
     * @param aExperienceLevelService experience level service.
     */
    @Autowired
    public VolunteerHandler( final UserValidationService aUserValidationService, final DtoService aDtoService,
                             final UserService aUserService, final PasswordEncoder aPasswordEncoder,
                             final InterestCategoryService aInterestCategoryService,
                             final ExperienceLevelService aExperienceLevelService )
    {
        dtoService = aDtoService;
        userService = aUserService;
        passwordEncoder = aPasswordEncoder;
        userValidationService = aUserValidationService;
        interestCategoryService = aInterestCategoryService;
        experienceLevelService = aExperienceLevelService;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger( VolunteerController.class );

    /**
     * Registers volunteer.
     *
     * @param aDto dto containing registration data.
     *
     * @return if volunteer passes validation, then ResponseEntity with 201 status and volunteer. If volunteer did not
     *         pass validation then ResponseEntity with 400 status and constraints violated. If there was an error,
     *         then ResponseEntity with 500 status and error message.
     */
    public ResponseEntity< ? > registerVolunteer( final VolunteerDto aDto )
    {
        try
        {
            final User user = dtoService.createVolunteerFromDto( aDto );
            final ValidationResult validationResult = userValidationService.validateEntity( user );

            if( validationResult.isValidated() )
            {
                user.setHashedPassword( passwordEncoder.encode( user.getPassword() ) );
                userService.saveOrUpdate( user );

                LOGGER.debug( "A new user has been registered for contact email {}, userId: {}",
                        user.getContactEmailAddress(), user.getId() );

                return ResponseEntity.status( HttpStatus.CREATED )
                        .body( validationResult.getValidatedEntity() );
            }

            LOGGER.debug( "Validation failed for new user with contact email {}, violations: {}", aDto.getContactEmail(),
                    validationResult.getValidationViolations().values() );

            return ResponseEntity.badRequest()
                    .body( validationResult.getValidationViolations() );
        }
        catch ( Exception aE )
        {
            LOGGER.error( "Exception occurred during registration of volunteer: {}", aE.getMessage(), aE );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Updates Volunteer's contact data, experience level, interest categories and participation motivation. If
     * some of mentioned data is not provided in dto then update on those properties is not performed.
     *
     * @param aVolunteerId volunteer id.
     *
     * @param aPatchDto volunteer patch info dto.
     *
     * @return
     *        - Response Entity with code 200 and patched Volunteer if everything went as expected.
     *        - Response Entity with code 400 if:
     *             1. There is no Volunteer with provided id found.
     *             2. Provided id belongs to User who is not Volunteer.
     *             3. Volunteer does not pass validation after patch (in this case also validation violations
     *                                                                are provided within the Response)
     *
     *        - Response Entity with code 500 when unexpected server-side error occurs.
     */
    public ResponseEntity< ? > updateVolunteerInformation( final Long aVolunteerId,
                                                           final VolunteerPatchInfoDto aPatchDto )
    {
        try
        {
            final Optional< User > optionalVolunteer = userService.tryLoadEntity( aVolunteerId );
            if( optionalVolunteer.isPresent() )
            {
                final User volunteer = optionalVolunteer.get();
                if( volunteer.hasUserRole( UserRole.VOLUNTEER ) )
                {
                    final VolunteerData volunteerData = volunteer.getVolunteerData();

                    Optional.ofNullable( aPatchDto.getContactEmailAddress() )
                            .ifPresent( volunteer::setContactEmailAddress );
                    Optional.ofNullable( aPatchDto.getPhoneNumber() )
                            .ifPresent( volunteer::setPhoneNumber );
                    Optional.ofNullable( aPatchDto.getParticipationMotivation() )
                            .ifPresent( volunteerData::setParticipationMotivation );

                    Optional.ofNullable( aPatchDto.getInterestCategoriesIds() )
                            .ifPresent( idList -> volunteerData.setInterestCategories( idList.stream()
                                    .map( interestCategoryService::loadEntity )
                                    .toList() ) );
                    Optional.ofNullable( aPatchDto.getExperienceId() )
                            .ifPresent( id -> volunteerData.setExperience( experienceLevelService.loadEntity( id ) ) );

                    final ValidationResult validationResult = userValidationService.validateEntity( volunteer );
                    if( validationResult.isValidated() )
                    {
                        userService.saveOrUpdate( volunteer );
                        return ResponseEntity.ok( validationResult.getValidatedEntity() );
                    }
                    else
                    {
                        return ResponseEntity.badRequest()
                                .body( validationResult.getValidationViolations() );
                    }
                }
                else
                {
                    return ResponseEntity.badRequest()
                            .body( "User with id: " + aVolunteerId +  " is not a Volunteer." );
                }
            }
            else
            {
                return ResponseEntity.badRequest()
                        .body( "No Volunteer found with id: " + aVolunteerId +  "." );
            }
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
