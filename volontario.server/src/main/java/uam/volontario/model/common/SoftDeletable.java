package uam.volontario.model.common;

/**
 * Interface for marking entities/objects which are soft-deletable (which are kept in the system, but not used anymore).
 */
public interface SoftDeletable
{
    /**
     * Marks whether entity/object should be currently used in the system.
     *
     * @return true if entity/object is meant for using, false if soft-deleted.
     */
    boolean isUsed();

    /**
     * Set value of used parameter.
     *
     * @param aValue true or false.
     */
    void setUsed( boolean aValue );
}
