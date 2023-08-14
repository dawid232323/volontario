package uam.volontario.crud.service;

import uam.volontario.model.common.SoftDeletable;

import java.util.List;

/**
 * Extension of entity service for soft deletable entities.
 *
 * @param <T> soft deletable entity.
 */
public interface SoftDeletableEntityService< T extends SoftDeletable > extends EntityService< T >
{
    /**
     * Fetches all entities which are marked as used.
     *
     * @return all used entities.
     */
    List< T > findAllUsed();

    /**
     * Fetches all entities which are marked as not used.
     *
     * @return all not used entities.
     */
    List< T > findAllNotUsed();

    /**
     * Softly removes entity.
     *
     * @param aEntity entity.
     *
     * @return softly removed entity.
     */
    default T softDelete( T aEntity )
    {
        aEntity.setUsed( false );
        return saveOrUpdate( aEntity );
    }

    /**
     * Reverts soft deletion of entity.
     *
     * @param aEntity entity.
     *
     * @return entity.
     */
    default T revertRemoval( T aEntity )
    {
        aEntity.setUsed( true );
        return saveOrUpdate( aEntity );
    }
}
