package uam.volontario.crud.service;

import org.springframework.stereotype.Service;
import uam.volontario.model.volunteer.impl.InterestCategory;

import java.util.List;

/**
 * Injection interface for {@linkplain InterestCategory}'s service.
 */
@Service
public interface InterestCategoryService extends EntityService< InterestCategory >
{
    List< InterestCategory > findByIds( List< Long > aIds );
}
