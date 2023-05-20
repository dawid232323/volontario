package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.offer.impl.Application;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.Application} entity.
 */
@Repository
public interface ApplicationRepository extends CrudRepository< Application, Long >
{}
