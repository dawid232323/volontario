package uam.volontario.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.lang.Nullable;
import uam.volontario.model.common.VolontarioDomainElementIf;

/**
 * Exception thrown when domain entity was not found.
 */
@Getter
public class VolontarioEntityNotFoundException extends EntityNotFoundException
{
    private final String entityType;

    @Nullable
    private final String entityName;

    @Nullable
    private final Long entityId;

    /**
     * Constructor.
     *
     * @param aEntityType entity type class.
     *
     * @param aEntityId entity id.
     *
     */
    public VolontarioEntityNotFoundException( final Class< ? extends VolontarioDomainElementIf > aEntityType, final Long aEntityId )
    {
        super();
        entityType = aEntityType.getSimpleName();
        entityId = aEntityId;
        entityName = null;
    }

    /**
     * Constructor.
     *
     * @param aEntityType entity type class.
     *
     * @param aEntityName entity name.
     *
     */
    public VolontarioEntityNotFoundException( final Class< ? extends VolontarioDomainElementIf > aEntityType, final String aEntityName )
    {
        super();
        entityType = aEntityType.getSimpleName();
        entityId = null;
        entityName = aEntityName;
    }
}
