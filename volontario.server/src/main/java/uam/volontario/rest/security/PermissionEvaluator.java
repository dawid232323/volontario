package uam.volontario.rest.security;

import org.springframework.stereotype.Service;
import uam.volontario.crud.service.ApplicationService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.Offer;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Evaluates Permissions for {@linkplain User}s and other API accessors.
 */
@Service
public class PermissionEvaluator
{
    private final UserService userService;

    private final OfferService offerService;

    private final ApplicationService applicationService;

    /**
     * CDI constructor.
     *
     * @param aUserService user service.
     *
     * @param aOfferService offer service.
     *
     * @param aApplicationService application service.
     */
    public PermissionEvaluator( final UserService aUserService, final ApplicationService aApplicationService,
                                final OfferService aOfferService )
    {
        userService = aUserService;
        offerService = aOfferService;
        applicationService = aApplicationService;
    }

    /**
     * Allows access to method only for Volunteers.
     *
     * @param aContactEmailAddress user's contact email address.
     *
     * @return true if User exists and is Volunteer, false otherwise.
     */
    public boolean allowForVolunteers( final String aContactEmailAddress )
    {
        final Optional< User > optionalUser = userService.tryToLoadByContactEmail( aContactEmailAddress );

        return optionalUser.isPresent() && Stream.of( UserRole.VOLUNTEER, UserRole.MOD, UserRole.ADMIN )
                .anyMatch( role -> optionalUser.get().hasUserRole( role ) );
    }

    /**
     * Allows access to method only for Institution Employees.
     *
     * @param aContactEmailAddress user's contact email address.
     *
     * @return true if User exists and is Institution Employee, false otherwise.
     */
    public boolean allowForInstitutionEmployees( final String aContactEmailAddress )
    {
        final Optional< User > optionalUser = userService.tryToLoadByContactEmail( aContactEmailAddress );

        return optionalUser.isPresent() && Stream.of( UserRole.INSTITUTION_EMPLOYEE, UserRole.INSTITUTION_ADMIN,
                        UserRole.MOD, UserRole.ADMIN ).anyMatch( role -> optionalUser.get().hasUserRole( role ) );
    }

    /**
     * Allows access to method only for Institution Administrator.
     *
     * @param aContactEmailAddress user's contact email address.
     *
     * @return true if User exists and is Institution Admin, false otherwise.
     */
    public boolean allowForInstitutionAdministrators( final String aContactEmailAddress )
    {
        final Optional< User > optionalUser = userService.tryToLoadByContactEmail( aContactEmailAddress );

        return optionalUser.isPresent() && Stream.of( UserRole.INSTITUTION_ADMIN,
                UserRole.MOD, UserRole.ADMIN ).anyMatch( role -> optionalUser.get().hasUserRole( role ) );
    }

    /**
     * Allows access to method only for Moderators.
     *
     * @param aContactEmailAddress user's contact email address.
     *
     * @return true if User exists and is Moderator, false otherwise.
     */
    public boolean allowForModerators( final String aContactEmailAddress )
    {
        final Optional< User > optionalUser = userService.tryToLoadByContactEmail( aContactEmailAddress );

        return optionalUser.isPresent() && Stream.of( UserRole.MOD, UserRole.ADMIN )
                .anyMatch( role -> optionalUser.get().hasUserRole( role ) );
    }

    /**
     * Allows access to method only for Administrators.
     *
     * @param aContactEmailAddress user's contact email address.
     *
     * @return true if User exists and is Admin, false otherwise.
     */
    public boolean allowForAdministrators( final String aContactEmailAddress )
    {
        final Optional< User > optionalUser = userService.tryToLoadByContactEmail( aContactEmailAddress );

        return optionalUser.isPresent() && optionalUser.get()
                .hasUserRole( UserRole.ADMIN );
    }

    /**
     * Allows access to method only for Moderators and Administrators.
     *
     * @param aContactEmailAddress user's contact email address.
     *
     * @return true if User exists and is Admin or Moderator, false otherwise.
     */
    public boolean allowForAdministration( final String aContactEmailAddress )
    {
        final Optional< User > optionalUser = userService.tryToLoadByContactEmail( aContactEmailAddress );

        return optionalUser.isPresent() && Stream.of( UserRole.MOD, UserRole.ADMIN )
                .anyMatch( role -> optionalUser.get().hasUserRole( role ) );
    }

    /**
     * Allows access to method only for Institution Employees or Institution Administrators.
     *
     * @param aContactEmailAddress user's contact email address.
     *
     * @return true if User exists and is Institution Employee or Institution Administrator, false otherwise.
     */
    public boolean allowForInstitution( final String aContactEmailAddress )
    {
        final Optional< User > optionalUser = userService.tryToLoadByContactEmail( aContactEmailAddress );

        return optionalUser.isPresent() && Stream.of( UserRole.INSTITUTION_EMPLOYEE, UserRole.INSTITUTION_ADMIN,
                        UserRole.MOD, UserRole.ADMIN ).anyMatch( role -> optionalUser.get().hasUserRole( role ) );
    }

    /**
     * Allows access to method only for Institution Employees or Institution Administrators which belong
     * to Institution related to Offer..
     *
     * @param aContactEmailAddress user's contact email address.
     *
     * @param aOfferId offer id.
     *
     * @return true if User exists and is Institution Employee or Institution Administrator, false otherwise.
     */
    public boolean allowForInstitutionRelatedToTheOffer( final String aContactEmailAddress, final Long aOfferId )
    {
        final Optional< User > optionalUser = userService.tryToLoadByContactEmail( aContactEmailAddress );

        if( optionalUser.isPresent() )
        {
            final User user = optionalUser.get();

            if( Stream.of( UserRole.ADMIN, UserRole.MOD )
                    .anyMatch( user::hasUserRole ) )
            {
                return true;
            }

            final Optional< Offer > optionalOffer = offerService.tryLoadEntity( aOfferId );

            if( optionalOffer.isPresent() )
            {
                final Offer offer = optionalOffer.get();

                if( user.hasUserRole( UserRole.INSTITUTION_EMPLOYEE ) )
                {
                    return offer.getContactPerson().equals( user );
                }

                if( user.hasUserRole( UserRole.INSTITUTION_ADMIN ) )
                {
                    return offer.getInstitution().equals( user.getInstitution() );
                }
            }
        }

        return false;
    }

    public boolean allowForInstitutionRelatedToTheApplication( final String aContactEmailAddress, final Long aApplicationId )
    {
        final Optional< User > optionalUser = userService.tryToLoadByContactEmail( aContactEmailAddress );

        if( optionalUser.isPresent() )
        {
            final User user = optionalUser.get();

            if( Stream.of( UserRole.ADMIN, UserRole.MOD )
                    .anyMatch( user::hasUserRole ) )
            {
                return true;
            }

            final Optional< Application > optionalApplication = applicationService.tryLoadEntity( aApplicationId );

            if( optionalApplication.isPresent() )
            {
                final Application application = optionalApplication.get();

                if( user.hasUserRole( UserRole.INSTITUTION_EMPLOYEE ) )
                {
                    return application.getOffer()
                            .getContactPerson().equals( user );
                }

                if( user.hasUserRole( UserRole.INSTITUTION_ADMIN ) )
                {
                    return application.getOffer()
                            .getInstitution().equals( user.getInstitution() );
                }
            }
        }

        return false;
    }

    public boolean allowForVolunteerRelatedToTheApplication( final String aContactEmailAddress, final Long aApplicationId )
    {
        final Optional< User > optionalUser = userService.tryToLoadByContactEmail( aContactEmailAddress );

        if( optionalUser.isPresent() )
        {
            final User user = optionalUser.get();

            if( Stream.of( UserRole.ADMIN, UserRole.MOD )
                    .anyMatch( user::hasUserRole ) )
            {
                return true;
            }

            final Optional< Application > optionalApplication = applicationService.tryLoadEntity( aApplicationId );

            if( optionalApplication.isPresent() )
            {
                final Application application = optionalApplication.get();

                if( user.hasUserRole( UserRole.VOLUNTEER ) )
                {
                    return application.getVolunteer().equals( user );
                }
            }
        }

        return false;
    }

    /**
     * Allows access to method for all Users.
     *
     * @param aContactEmailAddress user's contact email address.
     *
     * @return true if User exists, false otherwise.
     */
    public boolean allowForEveryUser( final String aContactEmailAddress )
    {
        return userService.tryToLoadByContactEmail( aContactEmailAddress )
                .isPresent();
    }

    /**
     * Allows access to everyone.
     *
     * @return true.
     */
    public boolean allowForEveryone()
    {
        return true;
    }
}
