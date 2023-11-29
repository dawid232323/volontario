package uam.volontario.crud.service.impl;

import com.google.common.collect.Lists;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.repository.VoluntaryRatingRepository;
import uam.volontario.crud.service.VoluntaryRatingService;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.VoluntaryRating;

import java.util.List;
import java.util.Optional;

/**
 * Basic implementation for {@linkplain VoluntaryRatingService}.
 */
@Service
public class VoluntaryRatingServiceImpl implements VoluntaryRatingService
{
    private final VoluntaryRatingRepository voluntaryRatingRepository;

    /**
     * CDI constructor.
     *
     * @param aVoluntaryRatingRepository voluntary rating repository.
     */
    @Autowired
    public VoluntaryRatingServiceImpl( final VoluntaryRatingRepository aVoluntaryRatingRepository )
    {
        voluntaryRatingRepository = aVoluntaryRatingRepository;
    }

    @Override
    public VoluntaryRating loadEntity( final Long aVoluntaryRatingId ) throws NoResultException
    {
        return voluntaryRatingRepository.findById( aVoluntaryRatingId )
                .orElseThrow( NoResultException::new );
    }

    @Override
    public Optional< VoluntaryRating > tryLoadEntity( final Long aVoluntaryRatingId )
    {
        return voluntaryRatingRepository.findById( aVoluntaryRatingId );
    }

    @Override
    public List< VoluntaryRating > loadAllEntities()
    {
        return Lists.newArrayList( voluntaryRatingRepository.findAll() );
    }

    @Override
    public VoluntaryRating saveOrUpdate( final VoluntaryRating aVoluntaryRating )
    {
        return voluntaryRatingRepository.save( aVoluntaryRating );
    }

    @Override
    public void deleteEntity( final Long aVoluntaryRatingId )
    {
        voluntaryRatingRepository.deleteById( aVoluntaryRatingId );
    }

    @Override
    public Optional< VoluntaryRating > findByOfferAndVolunteer( final Offer aOffer, final User aVolunteer )
    {
        return voluntaryRatingRepository.findByOfferAndVolunteer( aOffer, aVolunteer );
    }

    @Override
    public List< VoluntaryRating > findAllByInstitution( final Institution aInstitution )
    {
        return voluntaryRatingRepository.findAllByInstitution( aInstitution );
    }

    @Override
    public List< VoluntaryRating > findAllByVolunteer( final User aVolunteer )
    {
        return voluntaryRatingRepository.findAllByVolunteer( aVolunteer );
    }
}
