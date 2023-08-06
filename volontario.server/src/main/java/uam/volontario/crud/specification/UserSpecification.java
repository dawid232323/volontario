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
        final List< Predicate > predicates = new ArrayList<>();

        final Join< Role, User > rolesJoin = aRoot.join( "roles" );
        final Path< String > namePath = aRoot.get( "firstName" );
        final Path< String > lastNamePath = aRoot.get( "lastName" );
        final Path< String > emailPath = aRoot.get( "contactEmailAddress" );

        if( this.userSearchQuery.getRoleIds() != null && !this.userSearchQuery.getRoleIds().isEmpty() )
        {
            predicates.add( rolesJoin.get( "id" ).in( this.userSearchQuery.getRoleIds() ) );
        }
        if( this.userSearchQuery.getFirstName() != null )
        {
            predicates.add( aBuilder.like( aBuilder.lower( namePath ), "%" + this.userSearchQuery
                    .getFirstName().toLowerCase() + "%" ) );
        }
        if( this.userSearchQuery.getLastName() != null )
        {
            predicates.add( aBuilder.like( aBuilder.lower( lastNamePath ), "%" + this.userSearchQuery
                    .getLastName().toLowerCase() + "%" ) );
        }
        if( this.userSearchQuery.getEmail() != null )
        {
            predicates.add( aBuilder.like( aBuilder.lower( emailPath ), "%" + this.userSearchQuery
                    .getEmail().toLowerCase() + "%" ) );
        }

        return aBuilder.and( predicates.toArray( new Predicate[0] ) );
    }
}
