package uam.volontario.crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uam.volontario.model.offer.impl.VoluntaryPresence;

/**
 * Repository for {@linkplain VoluntaryPresence} entity.
 */
@Repository
public interface VoluntaryPresenceRepository extends CrudRepository< VoluntaryPresence, Long >
{}
