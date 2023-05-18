package uam.volontario.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.OfferStateService;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.OfferState;
import uam.volontario.model.offer.impl.OfferStateEnum;

import java.util.Optional;

/**
 * Service for assigning {@linkplain uam.volontario.model.offer.impl.Offer}s.
 */
@Service
public class OfferAssignmentHandler
{
    private final OfferService offerService;

    private final UserService userService;

    private final OfferStateService offerStateService;

    /**
     * Constructor.
     *
     * @param aOfferService offer service.
     *
     * @param aUserService user service.
     */
    @Autowired
    public OfferAssignmentHandler( final OfferService aOfferService, final UserService aUserService,
                                   final OfferStateService aOfferStateService )
    {
        offerService = aOfferService;
        userService = aUserService;
        offerStateService = aOfferStateService;
    }

    /**
     * Assigns Offer to {@linkplain User} of role {@linkplain UserRole#MOD}.
     *
     * @param aOfferId id of Offer.
     *
     * @param aModeratorId id of Moderator.
     *
     * @return Response Entity with assigned Offer and status 200,
     *         Response Entity with status 400 if:
     *
     *              - there is no User with given id.
     *              - User with given id is not Moderator.
     *              - Offer with given id was not found.
     *
     *        or Response Entity with status 500 if an unexpected error occurred.
     */
    public ResponseEntity< ? > assignOffer( final Long aOfferId, final Long aModeratorId )
    {
        try
        {
            final Optional< User > optionalModerator = userService.tryLoadEntity( aModeratorId );

            if( optionalModerator.isPresent() )
            {
                final User user = optionalModerator.get();
                if( user.hasUserRole( UserRole.MOD ) )
                {
                    final Optional< Offer > optionalOffer = offerService.tryLoadEntity( aOfferId );
                    if( optionalOffer.isPresent() )
                    {
                        final Offer offer = optionalOffer.get();
                        offer.setAssignedModerator( user );
                        offer.setOfferState( getUnderVerificationOfferState() );
                        offerService.saveOrUpdate( offer );

                        return ResponseEntity.ok( offer );
                    }
                    else
                    {
                        return ResponseEntity.badRequest()
                                .body( "No offer of id: " + aOfferId + "." );
                    }
                }
                else
                {
                    return ResponseEntity.badRequest()
                            .body( "User has no authority to have offer assigned." );
                }
            }
            else
            {
                return ResponseEntity.badRequest()
                        .body( "No moderator of id: " + aModeratorId + "." );
            }
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all Offers assigned to Moderator with given id.
     *
     * @param aModeratorId id of Moderator.
     *
     * @return Response Entity with Offers assigned to Moderator and status 200,
     *         Response Entity with status 400 if:
     *
     *              - there is no User with given id.
     *              - User with given id is not Moderator.
     *
     *        or Response Entity with status 500 if an unexpected error occurred.
     */
    public ResponseEntity< ? > loadOffersAssignedToModerator( final Long aModeratorId )
    {
        try
        {
            final Optional< User > optionalUser = userService.tryLoadEntity( aModeratorId );
            if( optionalUser.isPresent() )
            {
                final User user = optionalUser.get();
                if( user.hasUserRole( UserRole.MOD ) )
                {
                    return ResponseEntity.ok( offerService.findAllOffersAssignedToModerator( user ) );
                }
                else
                {
                    return ResponseEntity.badRequest()
                            .body( "User has no authority to have offer assigned." );
                }
            }
            else
            {
                return ResponseEntity.badRequest()
                        .body( "No moderator of id: " + aModeratorId + ".");
            }
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    /**
     * Loads all unassigned Offers.
     *
     * @return Response Entity with all unassigned Offers and status 200,
     *        or Response Entity with status 500 if an unexpected error occurred.
     */
    public ResponseEntity< ? > loadAllUnassignedOffers()
    {
        try
        {
            return ResponseEntity.ok( offerService.findAllUnassignedOffers() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( aE.getMessage() );
        }
    }

    private OfferState getUnderVerificationOfferState()
    {
        return offerStateService.tryLoadByState( OfferStateEnum
                        .mapOfferStateEnumToOfferStateName( OfferStateEnum.UNDER_VERIFICATION ) )
                .orElseThrow();
    }
}
