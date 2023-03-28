package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.common.impl.User;

import java.util.Optional;

/**
 * Repository for {@linkplain User} entity.
 */
@Repository
public interface UserRepository extends CrudRepository< User, Long >
{
    /**
     * Looks for User possessing given domain email address.
     *
     * @param aDomainEmail domain email address.
     *
     * @return user with the same domain email, if found.
     */
    Optional< User > findByDomainEmailAddress( String aDomainEmail );
}
