package uam.volontario.validation.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.validation.*;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.impl.User;
import uam.volontario.validation.ValidationResult;

import java.util.List;
import java.util.Set;

@Component
public class UserValidationService
{
    @Autowired
    private UserService userService;

    /**
     * Validates volunteer.
     *
     * @param aUser volunteer user.
     *
     * @return validation result.
     */
    public ValidationResult validateVolunteerUser( final User aUser )
    {
        final Multimap< String, String > validationViolations =  ArrayListMultimap.create();
        validateVolunteerByAnnotations( aUser, validationViolations );
        checkUniquenessOfUniqueAttributes( aUser, validationViolations );

        if( validationViolations.isEmpty() )
        {
            return ValidationResult.ok( aUser );
        }

        return ValidationResult.error( validationViolations );
    }

    private void validateVolunteerByAnnotations( final User aUser, final Multimap< String, String > aValidationViolations )
    {
        try( ValidatorFactory factory = Validation.buildDefaultValidatorFactory() )
        {
            final Validator validator = factory.getValidator();
            final Set< ConstraintViolation< User > > annotationViolations = validator.validate( aUser );
            mapAnnotationViolationsToPropertiesWithCauses( annotationViolations, aValidationViolations );
        }
        catch( Exception aE )
        {
            throw new ValidationException( "Error occurred during annotation validation", aE );
        }
    }

    private void checkUniquenessOfUniqueAttributes( final User aUser, final Multimap< String, String > aValidationViolations )
    {
        final List< User > allUsers = userService.loadAllEntities();

        allUsers.stream()
                .map( User::getDomainEmailAddress )
                .filter( domainEmail -> domainEmail.equals( aUser.getDomainEmailAddress() ) )
                .findAny()
                .ifPresent( domainEmail -> aValidationViolations.put( "domainEmailAddress",
                        domainEmail + " belongs to already registered User." ) );
        allUsers.stream()
                .map( User::getContactEmailAddress )
                .filter( contactEmail -> contactEmail.equals( aUser.getContactEmailAddress() ) )
                .findAny()
                .ifPresent( contactEmail -> aValidationViolations.put( "contactEmailAddress",
                        contactEmail + " belongs to already registered User." ) );
        allUsers.stream()
                .map( User::getPhoneNumber )
                .filter( phoneNumber -> phoneNumber.equals( aUser.getPhoneNumber() ) )
                .findAny()
                .ifPresent( phoneNumber -> aValidationViolations.put( "phoneNumber",
                        phoneNumber + " belongs to already registered User." ) );

    }

    private void mapAnnotationViolationsToPropertiesWithCauses(
            final Set< ConstraintViolation< User > > aViolations, final Multimap< String, String > aValidationViolations )
    {
        aViolations.stream()
                .map( violation -> Pair.of( getPropertyName( violation ), violation.getMessage() ) )
                .forEach( pair -> aValidationViolations.put( pair.getLeft(), pair.getRight() ) );
    }

    private String getPropertyName( final ConstraintViolation< User > aViolation )
    {
        return ( (PathImpl)aViolation.getPropertyPath() ).getLeafNode().getName();
    }
}
