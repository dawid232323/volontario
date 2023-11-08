package uam.volontario.crud.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import uam.volontario.model.offer.impl.Offer;
import uam.volontario.model.offer.impl.OfferSearchQuery;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification for {@link Offer}. Used to filter search queries.
 * */
public class OfferSpecification implements Specification< Offer >
{
    private final OfferSearchQuery query;

    public OfferSpecification( OfferSearchQuery aQuery )
    {
        this.query = aQuery;
    }

    @Override
    public Predicate toPredicate( Root< Offer > aRoot, CriteriaQuery< ? > aQuery, CriteriaBuilder aCriteriaBuilder ) {

        Path< String > offerTitle = aRoot.get( "title" );
        Path< Long > offerTypeId = aRoot.get( "offerType" ).get( "id" );
        Path< Instant > startDate = aRoot.get( "startDate" );
        Path< Instant > endDate = aRoot.get( "endDate" );
        Path< List< Long > > interestCategoryIds = aRoot.get( "interestCategories" ).get( "id" );
        Path< String > offerPlace = aRoot.get( "place" );
        Path< Long > experienceLevelId = aRoot.get( "minimumExperience" ).get( "id" );
        Path< Boolean > isPoznanOnly = aRoot.get( "isPoznanOnly" );
        Path< Boolean > isHidden = aRoot.get( "isHidden" );
        Path< Long > institutionId = aRoot.get( "institution" ).get( "id" );
        Path< Long > contactPersonId = aRoot.get( "contactPerson" ).get("id");

        final List< Predicate > predicateList = new ArrayList<>();

        if ( query.getTitle() != null )
        {
            predicateList.add( aCriteriaBuilder.like( aCriteriaBuilder.lower( offerTitle ),
                    "%" + query.getTitle().toLowerCase() + "%" ) );
        }
        if ( query.getOfferTypeId() != null )
        {
            predicateList.add( aCriteriaBuilder.equal( offerTypeId, query.getOfferTypeId() ) );
        }
        if ( query.getStartDate() != null )
        {
            predicateList.add( aCriteriaBuilder.greaterThanOrEqualTo( startDate , query.getStartDate().toInstant() ) );
        }
        if ( query.getEndDate() != null )
        {
            predicateList.add( aCriteriaBuilder.lessThanOrEqualTo( endDate, query.getEndDate().toInstant() ) );
        }
        if ( query.getInterestCategoryIds() != null && !query.getInterestCategoryIds().isEmpty() )
        {
            predicateList.add(interestCategoryIds.in( query.getInterestCategoryIds() ) );
        }
        if ( query.getOfferPlace() != null )
        {
            predicateList.add( aCriteriaBuilder.like( aCriteriaBuilder.lower( offerPlace ),
                    "%" + query.getOfferPlace().toLowerCase() + "%" ) );
        }
        if ( query.getExperienceLevelId() != null )
        {
            predicateList.add( aCriteriaBuilder.lessThanOrEqualTo( experienceLevelId, query.getExperienceLevelId() ) );
        }
        if ( query.getIsPoznanOnly() != null )
        {
            predicateList.add( aCriteriaBuilder.equal( isPoznanOnly, query.getIsPoznanOnly() ) );
        }
        if ( query.getIsHidden() != null )
        {
            predicateList.add( aCriteriaBuilder.equal( isHidden, query.getIsHidden() ) );
        }
        if ( query.getInstitutionId() != null )
        {
            predicateList.add( aCriteriaBuilder.equal( institutionId, query.getInstitutionId() ) );
        }
        if ( query.getContactPersonId() != null )
        {
            predicateList.add( aCriteriaBuilder.equal( contactPersonId, query.getContactPersonId() ) );
        }

        aQuery.distinct( true );
        return aCriteriaBuilder.and( predicateList.toArray( predicateList.toArray( new Predicate[0] ) ) );
    }
}
