package uam.volontario.crud.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.common.impl.User;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Repository for {@linkplain User} entity.
 */
@Repository
public interface UserRepository extends CrudRepository< User, Long >, JpaSpecificationExecutor< User >
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

    /**
     * Looks for User possessing given primary key.
     *
     * @param aUserId primary key.
     *
     * @return user with the same id, if found.
     */
    Optional< User > findById( final Long aUserId );
}
