package uam.volontario.validation.service.entity;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uam.volontario.crud.service.InstitutionContactPersonService;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.validation.service.AbstractValidationService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Validation service for {@linkplain User}.
 */
@Service
@RequestScope
public class UserValidationService extends AbstractValidationService< User >
{
    private final UserService userService;

    private final InstitutionContactPersonService institutionContactPersonService;

    private final VolunteerDataValidationService volunteerDataValidationService;

    /**
     * CDI constructor.
     *
     * @param aUserService user service.
     *
     * @param aInstitutionContactPersonService institution contact person service.
     *
     * @param aVolunteerDataValidationService volunteer data validation service.
     */
    @Autowired
    public UserValidationService( final UserService aUserService,
                                  final InstitutionContactPersonService aInstitutionContactPersonService,
                                  final VolunteerDataValidationService aVolunteerDataValidationService )
    {
        userService = aUserService;
        institutionContactPersonService = aInstitutionContactPersonService;
        volunteerDataValidationService = aVolunteerDataValidationService;
    }

    @Override
    protected void validateEntityByCustomConstraints( final User aUser )
    {
        checkUniquenessOfUniqueUserAttributes( aUser );
        if( aUser.hasUserRole( UserRole.VOLUNTEER ) )
        {
            validateVolunteerData( aUser );
        }
    }

    @Override
    protected void postProcessValidation( final User aUser )
    {
        if( Objects.isNull( aUser.getPassword() ) && Objects.nonNull( aUser.getId() ) )
        {
            // we ignore validation of 'password' property while patching other User properties.
            final Collection< String > violatedPasswordConstraints = Lists.newArrayList( validationViolations.get( "password" ) );
            violatedPasswordConstraints.forEach( violation -> validationViolations.remove( "password", violation ) );
        }
    }

    private void validateVolunteerData(final User aUser )
    {
        volunteerDataValidationService.validateEntity( aUser.getVolunteerData() )
                .getValidationViolations()
                .forEach( validationViolations::put );
    }

    private void checkUniquenessOfUniqueUserAttributes( final User aUser )
    {
        final List< User > allUsers = userService.loadAllEntities();
        final List< InstitutionContactPerson > allContactPeople = institutionContactPersonService.loadAllEntities();

        // if user is already persisted, then we exclude him from uniqueness validation.
        Optional.ofNullable( aUser.getId() )
                .ifPresent( id -> allUsers.remove( aUser ) );

        if( aUser.hasUserRole( UserRole.VOLUNTEER ) )
        {
            allUsers.stream()
                    .map( User::getVolunteerData )
                    .filter( Objects::nonNull )
                    .map( VolunteerData::getDomainEmailAddress )
                    .filter( emailUniquenessPredicate( aUser ) )
                    .findAny()
                    .ifPresent( domainEmail -> validationViolations.put( "domainEmailAddress",
                            domainEmail + " belongs to already registered Volunteer." ) );
        }

        Stream.concat( allUsers.stream()
                        .map( User::getContactEmailAddress ), allContactPeople.stream()
                        .filter( icp -> !icp.getInstitution().equals( aUser.getInstitution() ) )
                        .map( InstitutionContactPerson::getContactEmail ) )
                .filter( emailUniquenessPredicate( aUser ) )
                .findAny()
                .ifPresent( contactEmail -> validationViolations.put( "contactEmailAddress",
                        contactEmail + " belongs to already registered User or Contact Person." ) );

        Stream.concat( allUsers.stream()
                .map( User::getPhoneNumber ), allContactPeople.stream().filter( icp -> !icp.getInstitution().equals( aUser.getInstitution() ) )
                        .map( InstitutionContactPerson::getPhoneNumber ) )
                .filter( phoneNumber ->  aUser.getPhoneNumber().equals( phoneNumber ) )
                .findAny()
                .ifPresent( phoneNumber -> validationViolations.put( "phoneNumber",
                        phoneNumber + " belongs to already registered User or Contact Person." ) );
    }

    private Predicate< String > emailUniquenessPredicate( final User aUser )
    {
        if( aUser.hasUserRole( UserRole.VOLUNTEER ) )
        {
            return email -> email.equals( aUser.getVolunteerData().getDomainEmailAddress() )
                    || email.equals( aUser.getContactEmailAddress() );
        }

        return email -> email.equals( aUser.getContactEmailAddress() );
    }
}
