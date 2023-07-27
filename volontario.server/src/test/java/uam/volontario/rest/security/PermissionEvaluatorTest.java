package uam.volontario.rest.security;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uam.volontario.crud.service.ApplicationService;
import uam.volontario.crud.service.OfferService;
import uam.volontario.crud.service.UserService;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.offer.impl.Application;
import uam.volontario.model.offer.impl.Offer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith( MockitoExtension.class )
class PermissionEvaluatorTest
{
    @Mock
    private UserService userService;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private OfferService offerService;

    @InjectMocks
    private PermissionEvaluator permissionEvaluator;

    @Test
    public void allowForEveryoneShouldBeAllowedNoMatterWhat()
    {
        // given & when & then
        assertTrue( permissionEvaluator.allowForEveryone() );
    }

    @Test
    public void allowForVolunteersShouldBeAllowedForVolunteers()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        setUserRole( user, UserRole.VOLUNTEER );
        setUserContactEmail( user, contactEmail );

        // when
        doReturn( Optional.of( user ) ).when( userService )
                .tryToLoadByContactEmail( eq( contactEmail ) );

        // then
        assertTrue( permissionEvaluator.allowForVolunteers( contactEmail ) );
    }

    @Test
    public void allowForVolunteersShouldBeAllowedForAdministration()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.MOD, UserRole.ADMIN ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertTrue( permissionEvaluator.allowForVolunteers( contactEmail ) );
        } );
    }

    @Test
    public void allowForVolunteersShouldBeNOTAllowedForOtherRolesThanVolunteerAndAdministrationRoles()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        getAllUserRolesBut( List.of( UserRole.VOLUNTEER, UserRole.MOD, UserRole.ADMIN ) ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertFalse( permissionEvaluator.allowForVolunteers( contactEmail ) );
        } );
    }

    @Test
    public void allowForInstitutionEmployeeShouldBeAllowedForInstitutionEmployeeAndAdministrators()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.INSTITUTION_EMPLOYEE, UserRole.INSTITUTION_ADMIN ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertTrue( permissionEvaluator.allowForInstitutionEmployees( contactEmail ) );
        } );
    }

    @Test
    public void allowForInstitutionEmployeeShouldBeAllowedForAdministration()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.MOD, UserRole.ADMIN ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertTrue( permissionEvaluator.allowForVolunteers( contactEmail ) );
        } );
    }

    @Test
    public void allowForInstitutionEmployeeShouldBeNOTAllowedForOtherRolesThanInstitutionRolesAndAdministrationRoles()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        getAllUserRolesBut( List.of( UserRole.INSTITUTION_EMPLOYEE, UserRole.INSTITUTION_ADMIN,
                UserRole.MOD, UserRole.ADMIN ) ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertFalse( permissionEvaluator.allowForInstitutionEmployees( contactEmail ) );
        } );
    }

    @Test
    public void allowForInstitutionAdminShouldBeAllowedForInstitutionEmployeeAndAdministrators()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.INSTITUTION_ADMIN ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertTrue( permissionEvaluator.allowForInstitutionAdministrators( contactEmail ) );
        } );
    }

    @Test
    public void allowForInstitutionAdministratorsShouldBeAllowedForAdministration()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.MOD, UserRole.ADMIN ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertTrue( permissionEvaluator.allowForInstitutionAdministrators( contactEmail ) );
        } );
    }

    @Test
    public void allowForInstitutionAdministratorsShouldBeNOTAllowedForOtherRolesThanInstitutionAdminAndAdministrationRoles()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        getAllUserRolesBut( List.of( UserRole.INSTITUTION_ADMIN,
                UserRole.MOD, UserRole.ADMIN ) ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertFalse( permissionEvaluator.allowForInstitutionAdministrators( contactEmail ) );
        } );
    }

    @Test
    public void allowForModeratorsShouldBeAllowedForModeratorsAndAdmins()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.MOD, UserRole.ADMIN).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertTrue( permissionEvaluator.allowForModerators( contactEmail ) );
        } );
    }

    @Test
    public void allowForModeratorsShouldBeNOTAllowedForOtherRolesThanAdministrationRoles()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        getAllUserRolesBut( List.of( UserRole.INSTITUTION_ADMIN,
                UserRole.MOD, UserRole.ADMIN ) ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertFalse( permissionEvaluator.allowForModerators( contactEmail ) );
        } );
    }

    @Test
    public void allowForAdministratorsShouldBeAllowedForAdmins()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.ADMIN ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertTrue( permissionEvaluator.allowForAdministrators( contactEmail ) );
        } );
    }

    @Test
    public void allowForAdministratorsShouldBeNOTAllowedForOtherRolesThanAdministrator()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        getAllUserRolesBut( List.of( UserRole.ADMIN ) ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertFalse( permissionEvaluator.allowForAdministrators( contactEmail ) );
        } );
    }

    @Test
    public void allowForAdministrationShouldBeAllowedForModeratorsAndAdmins()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.MOD, UserRole.ADMIN).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertTrue( permissionEvaluator.allowForAdministration( contactEmail ) );
        } );
    }

    @Test
    public void allowForAdministrationShouldBeNOTAllowedForOtherRolesThanAdministrationRoles()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        getAllUserRolesBut( List.of( UserRole.MOD, UserRole.ADMIN ) ).forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertFalse( permissionEvaluator.allowForAdministration( contactEmail ) );
        } );
    }

    @Test
    public void allowForInstitutionShouldBeAllowedForInstitutionAndAdministration()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.MOD, UserRole.ADMIN, UserRole.INSTITUTION_ADMIN, UserRole.INSTITUTION_EMPLOYEE )
                .forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertTrue( permissionEvaluator.allowForInstitution( contactEmail ) );
        } );
    }

    @Test
    public void allowForAdministrationShouldBeNOTAllowedForOtherRolesThanAdministrationRolesAndInstitutionRoles()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        getAllUserRolesBut( List.of( UserRole.MOD, UserRole.ADMIN, UserRole.INSTITUTION_ADMIN, UserRole.INSTITUTION_EMPLOYEE ) )
                .forEach( role ->
        {
            setUserRole( user, role );
            setUserContactEmail( user, contactEmail );

            doReturn( Optional.of( user ) ).when( userService )
                    .tryToLoadByContactEmail( eq( contactEmail ) );

            // then
            assertFalse( permissionEvaluator.allowForInstitution( contactEmail ) );
        } );
    }

    @Test
    public void allowForInstitutionRelatedToOfferShouldBeAllowedForAdministration()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.MOD, UserRole.ADMIN )
                .forEach( role ->
                {
                    setUserRole( user, role );
                    setUserContactEmail( user, contactEmail );

                    doReturn( Optional.of( user ) ).when( userService )
                            .tryToLoadByContactEmail( eq( contactEmail ) );

                    // then
                    assertTrue( permissionEvaluator.allowForInstitutionRelatedToTheOffer( contactEmail, null ) );
                } );
    }

    @Test
    public void allowForInstitutionRelatedToApplicationShouldBeAllowedForAdministration()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.MOD, UserRole.ADMIN )
                .forEach( role ->
                {
                    setUserRole( user, role );
                    setUserContactEmail( user, contactEmail );

                    doReturn( Optional.of( user ) ).when( userService )
                            .tryToLoadByContactEmail( eq( contactEmail ) );

                    // then
                    assertTrue( permissionEvaluator.allowForInstitutionRelatedToTheApplication( contactEmail,
                            null ) );
                } );
    }

    @Test
    public void allowForVolunteerRelatedToApplicationShouldBeAllowedForAdministration()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        Stream.of( UserRole.MOD, UserRole.ADMIN )
                .forEach( role ->
                {
                    setUserRole( user, role );
                    setUserContactEmail( user, contactEmail );

                    doReturn( Optional.of( user ) ).when( userService )
                            .tryToLoadByContactEmail( eq( contactEmail ) );

                    // then
                    assertTrue( permissionEvaluator.allowForVolunteerRelatedToTheApplication( contactEmail,
                            null ) );
                } );
    }

    @Test
    public void allowForEveryUserShouldBeAllowedForAnyRole()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        // when
        getAllUserRoles()
                .forEach( role ->
                {
                    setUserRole( user, role );
                    setUserContactEmail( user, contactEmail );

                    doReturn( Optional.of( user ) ).when( userService )
                            .tryToLoadByContactEmail( eq( contactEmail ) );

                    // then
                    assertTrue( permissionEvaluator.allowForEveryUser( contactEmail) );
                } );
    }

    @Test
    public void allowForInstitutionRelatedToOfferShouldNOTBeAllowedForInstitutionIfOfferDoesNotExists()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        final Long offerId = 2137L;

        // when
        Stream.of( UserRole.INSTITUTION_EMPLOYEE, UserRole.INSTITUTION_ADMIN )
                .forEach( role ->
                {
                    setUserRole( user, role );
                    setUserContactEmail( user, contactEmail );

                    doReturn( Optional.of( user ) ).when( userService )
                            .tryToLoadByContactEmail( eq( contactEmail ) );
                    doReturn( Optional.empty() ).when( offerService )
                            .tryLoadEntity( eq( offerId ) );

                    // then
                    assertFalse( permissionEvaluator.allowForInstitutionRelatedToTheOffer( contactEmail, offerId ) );
                } );
    }

    @Test
    public void allowForInstitutionRelatedToApplicationShouldNOTBeAllowedForInstitutionIfApplicationDoesNotExists()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        final Long applicationId = 2137L;

        // when
        Stream.of( UserRole.INSTITUTION_EMPLOYEE, UserRole.INSTITUTION_ADMIN )
                .forEach( role ->
                {
                    setUserRole( user, role );
                    setUserContactEmail( user, contactEmail );

                    doReturn( Optional.of( user ) ).when( userService )
                            .tryToLoadByContactEmail( eq( contactEmail ) );
                    doReturn( Optional.empty() ).when( applicationService )
                            .tryLoadEntity( eq( applicationId ) );

                    // then
                    assertFalse( permissionEvaluator.allowForInstitutionRelatedToTheApplication( contactEmail,
                            applicationId ) );
                } );
    }

    @Test
    public void allowForVolunteerRelatedToApplicationShouldNOTBeAllowedToVolunteerIfApplicationDoesNotExists()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        final Long applicationId = 2137L;

        // when
        Stream.of( UserRole.VOLUNTEER )
                .forEach( role ->
                {
                    setUserRole( user, role );
                    setUserContactEmail( user, contactEmail );

                    doReturn( Optional.of( user ) ).when( userService )
                            .tryToLoadByContactEmail( eq( contactEmail ) );
                    doReturn( Optional.empty() ).when( applicationService )
                            .tryLoadEntity( eq( applicationId ) );

                    // then
                    assertFalse( permissionEvaluator.allowForVolunteerRelatedToTheApplication( contactEmail,
                            applicationId ) );
                } );
    }

    @Test
    public void allowForInstitutionRelatedToOfferShouldBeAllowedForInstitutionRelatedToTheOffer()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        final Offer offer = new Offer();
        final Long offerId = 2137L;
        final Institution institution = new Institution();

        offer.setContactPerson( user );
        offer.setInstitution( institution );
        user.setInstitution( institution );

        // when
        Stream.of( UserRole.INSTITUTION_EMPLOYEE, UserRole.INSTITUTION_ADMIN )
                .forEach( role ->
                {
                    setUserRole( user, role );
                    setUserContactEmail( user, contactEmail );

                    doReturn( Optional.of( user ) ).when( userService )
                            .tryToLoadByContactEmail( eq( contactEmail ) );
                    doReturn( Optional.of( offer ) ).when( offerService )
                            .tryLoadEntity( eq( offerId ) );

                    // then
                    assertTrue( permissionEvaluator.allowForInstitutionRelatedToTheOffer( contactEmail, offerId ) );
                } );
    }

    @Test
    public void allowForInstitutionRelatedToApplicationShouldBeAllowedForInstitutionWhenItsRelatedToTheApplication()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        final Application application = new Application();
        final Long applicationId = 2137L;
        final Institution institution = new Institution();

        final Offer offer = new Offer();

        application.setOffer( offer );
        offer.setContactPerson( user );
        offer.setInstitution( institution );
        user.setInstitution( institution );

        // when
        Stream.of( UserRole.INSTITUTION_EMPLOYEE, UserRole.INSTITUTION_ADMIN )
                .forEach( role ->
                {
                    setUserRole( user, role );
                    setUserContactEmail( user, contactEmail );

                    doReturn( Optional.of( user ) ).when( userService )
                            .tryToLoadByContactEmail( eq( contactEmail ) );
                    doReturn( Optional.of( application ) ).when( applicationService )
                            .tryLoadEntity( eq( applicationId ) );

                    // then
                    assertTrue( permissionEvaluator.allowForInstitutionRelatedToTheApplication( contactEmail,
                            applicationId ) );
                } );
    }

    @Test
    public void allowForVolunteerRelatedToApplicationShouldBeAllowedToVolunteerIfHeIsRelatedToApplication()
    {
        // given
        final User user = new User();
        final String contactEmail = "test@wp.pl";

        final Application application = new Application();
        final Long applicationId = 2137L;
        final Institution institution = new Institution();

        final Offer offer = new Offer();

        application.setOffer( offer );
        offer.setContactPerson( user );
        offer.setInstitution( institution );
        user.setInstitution( institution );
        application.setVolunteer( user );

        // when
        Stream.of( UserRole.VOLUNTEER )
                .forEach( role ->
                {
                    setUserRole( user, role );
                    setUserContactEmail( user, contactEmail );

                    doReturn( Optional.of( user ) ).when( userService )
                            .tryToLoadByContactEmail( eq( contactEmail ) );
                    doReturn( Optional.of( application ) ).when( applicationService )
                            .tryLoadEntity( eq( applicationId ) );

                    // then
                    assertTrue( permissionEvaluator.allowForVolunteerRelatedToTheApplication( contactEmail,
                            applicationId ) );
                } );
    }

    private List< UserRole > getAllUserRoles()
    {
        return Arrays.stream( UserRole.values() ).collect( Collectors.toList() );
    }

    private List< UserRole > getAllUserRolesBut( final List< UserRole > aUserRolesToExclude )
    {
        return CollectionUtils.subtract( getAllUserRoles(), aUserRolesToExclude ).stream().toList();
    }

    private void setUserRole( final User aUser, final UserRole aUserRole )
    {
        final List< Role > userRoles = UserRole.mapUserRolesToRoleNames( Collections.singletonList( aUserRole ) )
                .stream()
                .map( mapRoleNameToRole() )
                .toList();

        aUser.setRoles( userRoles );
    }

    private void setUserContactEmail( final User aUser, final String aContactEmail )
    {
        aUser.setContactEmailAddress( aContactEmail );
    }

    private Function< String, Role > mapRoleNameToRole()
    {
        return roleName -> Role.builder()
                .name( roleName )
                .build();
    }
}