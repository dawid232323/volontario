package uam.volontario.validation.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uam.volontario.crud.service.InstitutionContactPersonService;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.validation.service.AbstractValidationService;

import java.util.List;
import java.util.stream.Stream;

/**
 * Validation service for {@linkplain uam.volontario.model.institution.impl.InstitutionContactPerson}.
 */
@Service
@RequestScope
public class InstitutionContactPersonValidationService extends AbstractValidationService< InstitutionContactPerson >
{
    private final UserService userService;

    private final InstitutionContactPersonService institutionContactPersonService;

    /**
     * CDI constructor.
     *
     * @param aUserService user service.
     *
     * @param aInstitutionContactPersonService institution contact person service.
     */
    @Autowired
    public InstitutionContactPersonValidationService( final UserService aUserService,
                                  final InstitutionContactPersonService aInstitutionContactPersonService )
    {
        userService = aUserService;
        institutionContactPersonService = aInstitutionContactPersonService;
    }

    @Override
    protected void validateEntityByCustomConstraints( final InstitutionContactPerson aInstitutionContactPerson )
    {
        final List< User > allUsers = userService.loadAllEntities();
        final List< InstitutionContactPerson > allContactPeople = institutionContactPersonService.loadAllEntities();

        Stream.concat( Stream.concat( allUsers.stream()
                        .map( User::getContactEmailAddress ),
                        allUsers.stream()
                                .filter( user -> user.hasUserRole( UserRole.VOLUNTEER ) )
                                .map( volunteer -> volunteer.getVolunteerData().getDomainEmailAddress() ) ),
                        allContactPeople.stream()
                        .map( InstitutionContactPerson::getContactEmail ) )
                .filter( email -> aInstitutionContactPerson.getContactEmail().equals( email ) )
                .findAny()
                .ifPresent( contactEmail -> validationViolations.put( "contactEmailAddress",
                        contactEmail + " belongs to already registered User or Contact Person." ) );

        Stream.concat( allUsers.stream()
                        .map( User::getPhoneNumber ), allContactPeople.stream()
                        .map( InstitutionContactPerson::getPhoneNumber ) )
                .filter( phoneNumber ->  aInstitutionContactPerson.getPhoneNumber().equals( phoneNumber ) )
                .findAny()
                .ifPresent( phoneNumber -> validationViolations.put( "phoneNumber",
                        phoneNumber + " belongs to already registered User or Contact Person." ) );
    }

    @Override
    protected void postProcessValidation( final InstitutionContactPerson aInstitutionContactPerson )
    {
        // no post process validation for Institution Contact Person entity.
    }
}
