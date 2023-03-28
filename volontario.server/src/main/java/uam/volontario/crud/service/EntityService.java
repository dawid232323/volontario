package uam.volontario.crud.service;

import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;

/**
 * CRUD service for all Volontario domain entities.
 *
 * @param < T > entity type.
 */
public interface EntityService< T >
{
    /**
     * Loads entity by its ID.
     *
     * @param aEntityId entity's id.
     *
     * @return entity.
     *
     * @throws jakarta.persistence.NoResultException when none entity matches given ID.
     */
    T loadEntity( Long aEntityId ) throws NoResultException;

    /**
     * Tries to load entity by its ID.
     *
     * @param aEntityId entity's id.
     *
     * @return Optional with found entity or empty Optional if none entity matches given ID.
     */
    Optional< T > tryLoadEntity( Long aEntityId );

    /**
     * Loads all entities.
     *
     * @return List of all entities.
     */
    List< T > loadAllEntities();

    /**
     * Saves entity to database (if it does not already exist) or updates it (if it exists).
     *
     * @param aEntity entity.
     *
     * @return saved/updated entity.
     */
    T saveOrUpdate( T aEntity );

    /**
     * Deletes entity with given id.
     *
     * @param aEntityId entity's id.
     */
    void deleteEntity( Long aEntityId );
}
