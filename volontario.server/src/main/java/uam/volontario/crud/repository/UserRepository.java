package uam.volontario.crud.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.common.impl.User;

import javax.swing.text.html.Option;
import java.util.List;
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
     * @return user with the same contact email, if found.
     */
    Optional< User > findByContactEmailAddress( String aContactEmail );

    /**
     * Looks for User possessing given phone number.
     *
     * @param aPhoneNumber phone number.
     * @return user with the same phone number, if found.
     */
    Optional< User > findByPhoneNumber( String aPhoneNumber );

    /**
     * Looks for User possessing given primary key.
     *
     * @param aUserId primary key.
     * @return user with the same id, if found.
     */
    Optional< User > findById( final Long aUserId );

    /**
     * Retrieves list of institution workers.
     *
     * @param aInstitutionId institution id to retrieve workers
     *
     * @return list of workers assigned to the institution with given id
     */
    List< User > getUsersByInstitution_Id( final Long aInstitutionId );

    /**
     * Retrieves list of all users assigned to any institution.
     *
     * @return list of users assigned to any institution
     */
    List< User > getUsersByInstitutionIsNotNull();
}