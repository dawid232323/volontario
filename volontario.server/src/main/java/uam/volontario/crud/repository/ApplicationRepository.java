package uam.volontario.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.offer.impl.Application;

/**
 * Repository for {@linkplain uam.volontario.model.offer.impl.Application} entity.
 */
@Repository
public interface ApplicationRepository extends JpaRepository< Application, Long >, JpaSpecificationExecutor< Application >
{}
