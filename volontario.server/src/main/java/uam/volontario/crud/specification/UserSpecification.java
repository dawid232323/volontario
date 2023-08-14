package uam.volontario.crud.specification;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.common.impl.UserSearchQuery;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserSpecification implements Specification< User > {

    private final UserSearchQuery userSearchQuery;


    @Override
    public Predicate toPredicate( final Root< User > aRoot, final CriteriaQuery< ? > aQuery,
                                  final CriteriaBuilder aBuilder ) {

        final List< Predicate > conjunctionPredicates = new ArrayList<>();
        final List< Predicate > alternativePredicates = new ArrayList<>();

        final Join< Role, User > rolesJoin = aRoot.join( "roles" );
        final Path< String > namePath = aRoot.get( "firstName" );
        final Path< String > lastNamePath = aRoot.get( "lastName" );
        final Path< String > emailPath = aRoot.get( "contactEmailAddress" );

        if( this.userSearchQuery.getRoleIds() != null && !this.userSearchQuery.getRoleIds().isEmpty() )
        {
            conjunctionPredicates.add( rolesJoin.get( "id" ).in( this.userSearchQuery.getRoleIds() ) );
        }
        if( this.userSearchQuery.getFirstName() != null )
        {
            alternativePredicates.add( aBuilder.like( aBuilder.lower( namePath ), "%" + this.userSearchQuery
                    .getFirstName().toLowerCase() + "%" ) );
        }
        if( this.userSearchQuery.getLastName() != null )
        {
            alternativePredicates.add( aBuilder.like( aBuilder.lower( lastNamePath ), "%" + this.userSearchQuery
                    .getLastName().toLowerCase() + "%" ) );
        }
        if( this.userSearchQuery.getEmail() != null )
        {
            alternativePredicates.add( aBuilder.like( aBuilder.lower( emailPath ), "%" + this.userSearchQuery
                    .getEmail().toLowerCase() + "%" ) );
        }

        if( alternativePredicates.isEmpty() && conjunctionPredicates.isEmpty() )
        {
            return aBuilder.and( new ArrayList<Predicate>().toArray( new Predicate[0] ) );
        }
        if( !alternativePredicates.isEmpty() && !conjunctionPredicates.isEmpty() )
        {
            return aBuilder.and( aBuilder.and( conjunctionPredicates.toArray( new Predicate[0] ) ),
                    aBuilder.or( alternativePredicates.toArray( new Predicate[0] ) ) );
        }
        if( conjunctionPredicates.isEmpty() )
        {
            return aBuilder.or( alternativePredicates.toArray( new Predicate[0] ) );
        }
        return aBuilder.and( conjunctionPredicates.toArray( new Predicate[0] ) );
    }
}
