package uam.volontario.model.utils;

import lombok.experimental.UtilityClass;
import uam.volontario.crud.service.*;
import uam.volontario.exception.VolontarioEntityNotFoundException;
import uam.volontario.exception.user.RoleMismatchException;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.configuration.ConfigurationEntry;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.offer.impl.*;
import uam.volontario.model.volunteer.impl.ExperienceLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Class to hold utility logic for easier work with model.
 */
@UtilityClass
public class ModelUtils
{
    /**
     * Resolves Volunteer from User id.
     *
     * @param aVolunteerId id of Volunteer.
     *
     * @param aUserService user service.
     *
     * @return Volunteer if User of given id exists and has Volunteer role.
     */
    public User resolveVolunteer( final Long aVolunteerId, final UserService aUserService )
    {
        return resolveVolunteer( aVolunteerId, aUserService::tryToFindById );
    }

    /**
     * Resolves Volunteer from User id.
     *
     * @param aVolunteerId id of Volunteer.
     *
     * @param aUserLookupFunction function to retrieve User instance from database.
     *
     * @return Volunteer if User of given id exists and has Volunteer role.
     */
    public User resolveVolunteer( final Long aVolunteerId, final Function< Long, Optional< User > > aUserLookupFunction )
    {
        final Optional< User > optionalUser = aUserLookupFunction.apply( aVolunteerId );

        if ( optionalUser.isEmpty() )
        {
            throw new VolontarioEntityNotFoundException( User.class, aVolunteerId );
        }

        final User user = optionalUser.get();
        if( !user.hasUserRole( UserRole.VOLUNTEER ) )
        {
            throw new RoleMismatchException( String
                    .format( "User with username %s is not a volunteer", user.getUsername() ) );
        }

        return user;
    }

    /**
     * Resolves User from id.
     *
     * @param aUserId id of User.
     *
     * @param aUserService User service.
     *
     * @return User of given id if it exists.
     *
     */
    public User resolveUser( final Long aUserId, final UserService aUserService )
    {
        return resolveUser( aUserId, aUserService::tryLoadEntity );
    }

    /**
     * Resolves User from id.
     *
     * @param aUserId id of User.
     *
     * @param aUserLookupFunction function to retrieve User instance from database.
     *
     * @return User of given id if it exists.
     *
     */
    public User resolveUser( final Long aUserId, final Function< Long, Optional< User > > aUserLookupFunction )
    {
        return aUserLookupFunction.apply( aUserId )
                .orElseThrow( () -> new VolontarioEntityNotFoundException( User.class, aUserId ) );
    }

    /**
     * Resolves Application from id.
     *
     * @param aApplicationId id of Application.
     *
     * @param aApplicationService application service.
     *
     * @return Application of given id if it exists.
     *
     */
    public Application resolveApplication( final Long aApplicationId, final ApplicationService aApplicationService )
    {
        return resolveApplication( aApplicationId, aApplicationService::tryLoadEntity );
    }

    /**
     * Resolves Application from id.
     *
     * @param aApplicationId id of Application.
     *
     * @param aApplicationLookupFunction function to retrieve Application instance from database.
     *
     * @return Application of given id if it exists.
     *
     */
    public Application resolveApplication( final Long aApplicationId,
                                           final Function< Long, Optional< Application > > aApplicationLookupFunction )
    {
        return aApplicationLookupFunction.apply( aApplicationId )
                .orElseThrow( () -> new VolontarioEntityNotFoundException( Application.class, aApplicationId ) );
    }

    /**
     * Resolves Offer from id.
     *
     * @param aOfferId id of Offer.
     *
     * @param aOfferService offer service.
     *
     * @return Offer of given id if it exists.
     *
     */
    public Offer resolveOffer( final Long aOfferId, final OfferService aOfferService )
    {
        return resolveOffer( aOfferId, aOfferService::tryLoadEntity );
    }

    /**
     * Resolves Offer from id.
     *
     * @param aOfferId id of Offer.
     *
     * @param aOfferLookupFunction function to retrieve Offer instance from database.
     *
     * @return Offer of given id if it exists.
     *
     */
    public Offer resolveOffer( final Long aOfferId, final Function< Long, Optional< Offer > > aOfferLookupFunction )
    {
        return aOfferLookupFunction.apply( aOfferId )
                .orElseThrow( () -> new VolontarioEntityNotFoundException( Application.class, aOfferId ) );
    }

    /**
     * Resolves Institution from id.
     *
     * @param aInstitutionId id of Institution.
     *
     * @param aInstitutionService Institution service.
     *
     * @return Institution of given id if it exists.
     *
     */
    public Institution resolveInstitution( final Long aInstitutionId, final InstitutionService aInstitutionService )
    {
        return resolveInstitution( aInstitutionId, aInstitutionService::tryLoadEntity );
    }

    /**
     * Resolves Institution from id.
     *
     * @param aInstitutionId id of Institution.
     *
     * @param aInstitutionLookupFunction function to retrieve Institution instance from database.
     *
     * @return Institution of given id if it exists.
     *
     */
    public Institution resolveInstitution( final Long aInstitutionId,
                                           final Function< Long, Optional< Institution > > aInstitutionLookupFunction )
    {
        return aInstitutionLookupFunction.apply( aInstitutionId )
                .orElseThrow( () -> new VolontarioEntityNotFoundException( Application.class, aInstitutionId ) );
    }

    /**
     * Resolves Voluntary Presence State from its enum.
     *
     * @param aVoluntaryPresenceStateEnum voluntary presence state enum.
     *
     * @param aVoluntaryPresenceStateService voluntary presence state service.
     *
     * @return voluntary presence state corresponding to given enum if it exists.
     *
     */
    public VoluntaryPresenceState resolveVoluntaryPresenceState( final VoluntaryPresenceStateEnum aVoluntaryPresenceStateEnum,
                                                                 final VoluntaryPresenceStateService aVoluntaryPresenceStateService )
    {
        return resolveVoluntaryPresenceState( aVoluntaryPresenceStateEnum, aVoluntaryPresenceStateService::tryLoadByState );
    }

    /**
     * Resolves Voluntary Presence State from its enum.
     *
     * @param aVoluntaryPresenceStateEnum voluntary presence state enum.
     *
     * @param aVoluntaryPresenceStateLookupFunction function to retrieve voluntary presence state instance from database.
     *
     * @return voluntary presence state corresponding to given enum if it exists.
     *
     */
    public VoluntaryPresenceState resolveVoluntaryPresenceState( final VoluntaryPresenceStateEnum aVoluntaryPresenceStateEnum,
                                                                 final Function< String, Optional< VoluntaryPresenceState > >
                                                                         aVoluntaryPresenceStateLookupFunction )
    {
        return aVoluntaryPresenceStateLookupFunction.apply( aVoluntaryPresenceStateEnum.getTranslatedState() )
                .orElseThrow( () -> new VolontarioEntityNotFoundException( VoluntaryPresenceState.class, aVoluntaryPresenceStateEnum.name() ) );
    }

    /**
     * Resolves Configuration Entry from its key name.
     *
     * @param aConfigurationEntryName Configuration Entry key name.
     *
     * @param aConfigurationEntryService Configuration Entry service.
     *
     * @return Configuration Entry instance of given key name if it exists.
     *
     */
    public ConfigurationEntry resolveConfigurationEntry( final String aConfigurationEntryName,
                                                         final ConfigurationEntryService aConfigurationEntryService )
    {
        return resolveConfigurationEntry( aConfigurationEntryName, aConfigurationEntryService::findByKey );
    }

    /**
     * Resolves Configuration Entry from its key name.
     *
     * @param aConfigurationEntryName Configuration Entry key name.
     *
     * @param aConfigurationEntryLookupFunction function to retrieve Configuration Entry instance from database.
     *
     * @return Configuration Entry instance of given key name if it exists.
     *
     */
    public ConfigurationEntry resolveConfigurationEntry( final String aConfigurationEntryName,
                                                         final Function< String, Optional< ConfigurationEntry > > aConfigurationEntryLookupFunction )
    {
        return aConfigurationEntryLookupFunction.apply( aConfigurationEntryName )
                .orElseThrow( () -> new VolontarioEntityNotFoundException( ConfigurationEntry.class, aConfigurationEntryName ) );
    }

    /**
     * Resolves Application State from id.
     *
     * @param aApplicationStateEnum  Application State enum.
     *
     * @param aApplicationStateService Application State service.
     *
     * @return Application State of given id if it exists.
     *
     */
    public ApplicationState resolveApplicationState( final ApplicationStateEnum aApplicationStateEnum, final ApplicationStateService aApplicationStateService )
    {
        return resolveApplicationState( aApplicationStateEnum, aApplicationStateService::tryLoadByName );
    }

    /**
     * Resolves Application State from id.
     *
     * @param aApplicationStateEnum  Application State enum.
     *
     * @param aApplicationStateLookupFunction function to retrieve Application State instance from database.
     *
     * @return Application State of given id if it exists.
     *
     */
    public ApplicationState resolveApplicationState( final ApplicationStateEnum aApplicationStateEnum,
                                                     final Function< String, Optional< ApplicationState > > aApplicationStateLookupFunction )
    {
        return aApplicationStateLookupFunction.apply( aApplicationStateEnum.getTranslatedState() )
                .orElseThrow( () -> new VolontarioEntityNotFoundException( ApplicationState.class, aApplicationStateEnum.getTranslatedState() ) );
    }

    /**
     * Resolves Offer State from id.
     *
     * @param aOfferStateEnum  Offer State enum.
     *
     * @param aOfferStateService Offer State service.
     *
     * @return Offer State of given id if it exists.
     *
     */
    public OfferState resolveOfferState( final OfferStateEnum aOfferStateEnum, final OfferStateService aOfferStateService )
    {
        return resolveOfferState( aOfferStateEnum, aOfferStateService::tryLoadByState );
    }

    /**
     * Resolves Offer State from id.
     *
     * @param aOfferStateEnum  Offer State enum.
     *
     * @param aOfferStateLookupFunction function to retrieve Offer State instance from database.
     *
     * @return Offer State of given id if it exists.
     *
     */
    public OfferState resolveOfferState( final OfferStateEnum aOfferStateEnum,
                                         final Function< String, Optional< OfferState > > aOfferStateLookupFunction )
    {
        return aOfferStateLookupFunction.apply( aOfferStateEnum.getTranslatedState() )
                .orElseThrow( () -> new VolontarioEntityNotFoundException( OfferState.class, aOfferStateEnum.getTranslatedState() ) );
    }

    /**
     * Resolves Roles from given UserRoles.
     * 
     * @param aRoleService role service.
     *                     
     * @param aUserRoles user roles.
     *                   
     * @return list of Roles.
     */
    public List< Role > resolveRoles( final RoleService aRoleService, final UserRole ... aUserRoles )
    {
        return resolveRoles( aRoleService::findByNameIn, aUserRoles );
    }

    /**
     * Resolves Roles from given UserRoles.
     *
     * @param aRolesLookupFunction function to retrieve Roles from database.
     *
     * @param aUserRoles user roles.
     *
     * @return list of Roles.
     */
    public List< Role > resolveRoles( final Function< List< String >, List< Role > > aRolesLookupFunction, final UserRole ... aUserRoles )
    {
        return aRolesLookupFunction.apply( 
                UserRole.mapUserRolesToRoleNames( Arrays.stream( aUserRoles ).toList() ) );
    }

    /**
     * Resolves ExperienceLevel from id.
     *
     * @param aExperienceLevelId id of ExperienceLevel.
     *
     * @param aExperienceLevelService ExperienceLevel service.
     *
     * @return ExperienceLevel of given id if it exists.
     *
     */
    public ExperienceLevel resolveExperienceLevel( final Long aExperienceLevelId, final ExperienceLevelService aExperienceLevelService )
    {
        return resolveExperienceLevel( aExperienceLevelId, aExperienceLevelService::tryLoadEntity );
    }

    /**
     * Resolves ExperienceLevel from id.
     *
     * @param aExperienceLevelId id of ExperienceLevel.
     *
     * @param aExperienceLevelLookupFunction function to retrieve ExperienceLevel instance from database.
     *
     * @return ExperienceLevel of given id if it exists.
     *
     */
    public ExperienceLevel resolveExperienceLevel( final Long aExperienceLevelId, final Function< Long, Optional< ExperienceLevel > > aExperienceLevelLookupFunction )
    {
        return aExperienceLevelLookupFunction.apply( aExperienceLevelId )
                .orElseThrow( () -> new VolontarioEntityNotFoundException( ExperienceLevel.class, aExperienceLevelId ) );
    }
}
