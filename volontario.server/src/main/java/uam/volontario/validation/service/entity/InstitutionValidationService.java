package uam.volontario.validation.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uam.volontario.crud.service.InstitutionService;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.validation.service.AbstractValidationService;

import java.util.List;

/**
 * Validation service for {@linkplain uam.volontario.model.institution.impl.Institution}.
 */
@Service
@RequestScope
public class InstitutionValidationService extends AbstractValidationService< Institution >
{
    private final InstitutionService institutionService;

    private final InstitutionContactPersonValidationService institutionContactPersonValidationService;

    /**
     * CDI constructor.
     *
     * @param aInstitutionService institution service.
     *
     * @param aInstitutionContactPersonValidationService institution contact person validation service.
     */
    @Autowired
    public InstitutionValidationService( final InstitutionService aInstitutionService,
                                         final InstitutionContactPersonValidationService aInstitutionContactPersonValidationService )
    {
        institutionService = aInstitutionService;
        institutionContactPersonValidationService = aInstitutionContactPersonValidationService;
    }

    @Override
    protected void validateEntityByCustomConstraints( final Institution aInstitution )
    {
        final List< Institution > allInstitutions = institutionService.loadAllEntities();

        allInstitutions.stream()
                .map( Institution::getKrsNumber )
                .filter( krs -> aInstitution.getKrsNumber().equals( krs ) )
                .findAny()
                .ifPresent( krs -> validationViolations.put( "krsNumber",
                        krs + " belongs to already registered Institution." ) );

        institutionContactPersonValidationService.validateEntity( aInstitution.getInstitutionContactPerson() );
    }

    @Override
    protected void postProcessValidation( final Institution aInstitution )
    {
        // no post process validation for Institution entity.
    }
}
