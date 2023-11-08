package uam.volontario.crud.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.ApplicationSearchQuery;
import uam.volontario.model.offer.impl.ApplicationState;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification for {@link Application}. Used to filter search queries.
 * */
public class ApplicationSpecification implements Specification< Application >
{
    private final ApplicationSearchQuery query;

    public ApplicationSpecification( ApplicationSearchQuery aQuery )
    {
        this.query = aQuery;
    }

    @Override
    public Predicate toPredicate ( Root< Application > aRoot, CriteriaQuery< ? > aQuery, CriteriaBuilder aCriteriaBuilder )
    {
        Path< ApplicationState > state = aRoot.get( "state" ).get( "name" );
        Path< Boolean > isStarred = aRoot.get( "isStarred" );
        Path< Long > offerId = aRoot.get( "offer" ).get( "id" );
        Path< Long > volunteerId = aRoot.get( "volunteer" ).get( "id" );
        Path< Long > institutionId = aRoot.get( "offer" ).get( "institution" ).get( "id" );

        final List< Predicate > predicateList = new ArrayList<>();

        if( query.getState() != null )
        {
            predicateList.add( aCriteriaBuilder.equal( state,
                    query.getState().getTranslatedState() ) );
        }
        if ( query.getIsStarred() != null )
        {
            predicateList.add( aCriteriaBuilder.equal( isStarred, query.getIsStarred() ) );
        }
        if ( query.getOfferId() != null )
        {
            predicateList.add( aCriteriaBuilder.equal( offerId, query.getOfferId() ) );
        }
        if ( query.getVolunteerId() != null )
        {
            predicateList.add( aCriteriaBuilder.equal( volunteerId, query.getVolunteerId() ) );
        }
        if ( query.getInstitutionId() != null )
        {
            predicateList.add( aCriteriaBuilder.equal( institutionId, query.getInstitutionId() ) );
        }

        aQuery.distinct( true );
        return aCriteriaBuilder.and( predicateList.toArray( new Predicate[ 0 ] ) );
    }
}
