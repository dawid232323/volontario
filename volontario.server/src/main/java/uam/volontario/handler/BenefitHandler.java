package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.BenefitService;
import uam.volontario.dto.BenefitDto;
import uam.volontario.model.offer.impl.Benefit;

@Service
public class BenefitHandler
{
    private final BenefitService benefitService;

    /**
     * CDI controller.
     *
     * @param aBenefitService benefit service.
     */
    @Autowired
    public BenefitHandler( final BenefitService aBenefitService )
    {
        benefitService = aBenefitService;
    }

    /**
     * Loads all used Benefits.
     *
     * @return
     *         - Response Entity with code 200 and list of all used Benefits if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    public ResponseEntity< ? > loadAllUsedBenefits()
    {
        try
        {
            return ResponseEntity.ok( benefitService.findAllUsed() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all not used Benefits.
     *
     * @return
     *         - Response Entity with code 200 and list of all not used Benefits if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    public ResponseEntity< ? > loadAllNotUsedBenefits()
    {
        try
        {
            return ResponseEntity.ok( benefitService.findAllNotUsed() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all Benefits.
     *
     * @return
     *         - Response Entity with code 200 and list of all Benefits if everything went as expected.
     *         - Response Entity with code 500 and error message if server side error occurred.
     */
    public ResponseEntity< ? > loadAllBenefits()
    {
        try
        {
            return ResponseEntity.ok( benefitService.loadAllEntities() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Creates Benefit instance.
     *
     * @param aBenefitDto dto containing Benefit data.
     *
     * @return
     *        - Response Entity with code 201 and created Benefit if everything went as expected.
     *        - Response Entity with code 500 and error message in case of server side error.
     */
    public ResponseEntity< ? > createBenefit( final BenefitDto aBenefitDto )
    {
        try
        {
            final Benefit benefit = Benefit.builder()
                    .name( aBenefitDto.getName() )
                    .isUsed( true )
                    .build();

            // TODO: validation for benefit saving.
            return ResponseEntity.status( HttpStatus.CREATED )
                    .body( benefitService.saveOrUpdate( benefit ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Updates Benefit instance.
     *
     * @param aBenefitDto dto containing Benefit data.
     *
     * @return
     *        - Response Entity with code 200 and updated Benefit if everything went as expected.
     *        - Response Entity with code 500 and error message in case of server side error.
     */
    public ResponseEntity< ? > updateBenefit( final BenefitDto aBenefitDto )
    {
        try
        {
            final Benefit benefit = Benefit.builder()
                    .name( aBenefitDto.getName() )
                    .isUsed( true )
                    .id( aBenefitDto.getId() )
                    .build();

            // TODO: validation for benefit update.
            return ResponseEntity.ok( benefitService.saveOrUpdate( benefit ) );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Removes Benefit instance.
     *
     * @param aId id of Benefit to remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    public ResponseEntity< ? > removeBenefit( final Long aId )
    {
        try
        {
            benefitService.deleteEntity( aId );
            return ResponseEntity.ok().
                    build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Softly removes Benefit instance.
     *
     * @param aId id of Benefit to softly remove.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    public ResponseEntity< ? > softRemoveBenefit( final Long aId )
    {
        try
        {
            benefitService.tryLoadEntity( aId )
                    .ifPresent( benefitService::softDelete );
            return ResponseEntity.ok()
                    .build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Reverts removal of Benefit instance.
     *
     * @param aId id of Benefit to revert soft removal.
     *
     * @return
     *         - Response Entity with 200 status if everything went as expected.
     *         - Response Entity with 500 status if server side error occurred.
     */
    public ResponseEntity< ? > revertSoftRemovalOfBenefit( final Long aId )
    {
        try
        {
            benefitService.tryLoadEntity( aId )
                    .ifPresent( benefitService::revertRemoval );
            return ResponseEntity.ok()
                    .build();
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }
}
