package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.common.impl.User;

/**
 * Repository for {@linkplain User}s.
 */
@Repository
public interface UserRepository extends CrudRepository< User, Long >
{}
