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
     * Looks for User possessing given contact email address.
     *
     * @param aContactEmail contact email address.
     *
     * @return user with the same contact email, if found.
     */
    Optional< User > findByContactEmailAddress( String aContactEmail );

    /**
     * Looks for User possessing given phone number.
     *
     * @param aPhoneNumber phone number.
     *
     * @return user with the same phone number, if found.
     */
    Optional< User > findByPhoneNumber( String aPhoneNumber );
}
