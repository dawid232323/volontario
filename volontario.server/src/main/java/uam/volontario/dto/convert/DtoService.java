package uam.volontario.dto.convert;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.*;
import uam.volontario.dto.Application.ApplicationBaseInfoDto;
import uam.volontario.dto.Application.ApplicationDetailsDto;
import uam.volontario.dto.BenefitDto;
import uam.volontario.dto.Institution.InstitutionContactPersonDto;
import uam.volontario.dto.Institution.InstitutionDto;
import uam.volontario.dto.Institution.InterestCategoryDto;
import uam.volontario.dto.Offer.OfferBaseInfoDto;
import uam.volontario.dto.Offer.OfferDetailsDto;
import uam.volontario.dto.Offer.OfferDto;
import uam.volontario.dto.Offer.OfferTypeDto;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.dto.rating.InstitutionRatingDto;
import uam.volontario.dto.rating.VolunteerRatingDto;
import uam.volontario.dto.user.AdministrativeUserDetailsDto;
import uam.volontario.dto.user.InstitutionWorkerDto;
import uam.volontario.dto.user.UserProfileDto;
import uam.volontario.dto.user.UserWithJwtDto;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.model.offer.impl.*;
import uam.volontario.model.utils.ModelUtils;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.model.volunteer.impl.VolunteerData;
import uam.volontario.security.jwt.JWTService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service for DTO operations.
 */
@Service
public class DtoService
{
    private final InterestCategoryService interestCategoryService;

    private final RoleService roleService;

    private final BenefitService benefitService;

    private final UserService userService;

    private final OfferTypeService offerTypeService;

    private final OfferStateService offerStateService;

    /**
     * CDI constructor.
     *
     * @param aInterestCategoryService interest category service.
     */
    @Autowired
    public DtoService( final InterestCategoryService aInterestCategoryService,
                       final RoleService aRoleService, final BenefitService aBenefitService, final UserService aUserService,
                       final OfferTypeService aOfferTypeService, final OfferStateService aOfferStateService )
    {
        interestCategoryService = aInterestCategoryService;
        roleService = aRoleService;
        benefitService = aBenefitService;
        userService = aUserService;
        offerTypeService = aOfferTypeService;
        offerStateService = aOfferStateService;
    }

    /**
     * Creates Volunteer {@linkplain User} from DTO.
     *
     * @param aDto dto.
     *
     * @return Volunteer.
     */
    public User createVolunteerFromDto( final VolunteerDto aDto )
    {
        final List< Role > roles = ModelUtils.resolveRoles( roleService, UserRole.VOLUNTEER );


        final List< InterestCategory > volunteerInterestCategories = interestCategoryService.findByIds(
                aDto.getInterestCategoriesIds() );

        final VolunteerData volunteerData = VolunteerData.builder()
                .participationMotivation( aDto.getParticipationMotivation() )
                .domainEmailAddress( aDto.getDomainEmail() )
                .fieldOfStudy( aDto.getFieldOfStudy() )
                .interestCategories( volunteerInterestCategories )
                .build();

        final User user = User.builder().firstName( aDto.getFirstName() )
                .lastName( aDto.getLastName() )
                .password( aDto.getPassword() )
                .contactEmailAddress( aDto.getContactEmail() )
                .phoneNumber( aDto.getPhoneNumber() )
                .roles( roles )
                .isVerified( false )
                .volunteerData( volunteerData )
                .creationDate( Instant.now() )
                .build();

        user.getVolunteerData().setUser( user );

        return user;
    }

    /**
     * Creates Volunteer {@linkplain Institution} from DTO.
     *
     * @param aDto dto.
     * @return Institution.
     */
    public Institution createInstitutionFromDto( final InstitutionDto aDto )
    {
        final InstitutionContactPersonDto contactPersonDto = aDto.getContactPerson();
        final InstitutionContactPerson contactPerson = InstitutionContactPerson.builder()
                .firstName(contactPersonDto.getFirstName())
                .lastName(contactPersonDto.getLastName())
                .contactEmail(contactPersonDto.getContactEmail())
                .phoneNumber(contactPersonDto.getPhoneNumber())
                .build();

        return Institution.builder()
                .name(aDto.getName())
                .description(aDto.getDescription())
                .localization(aDto.getLocalization())
                .headquarters(aDto.getHeadquartersAddress())
                .krsNumber(aDto.getKrsNumber())
                .isActive(false)
                .registrationToken(RandomStringUtils.randomAlphanumeric(64))
                .institutionContactPerson(contactPerson)
                .institutionTags( String.join( ",", aDto.getTags() ) )
                .build();
    }

    /**
     * Builds and returns dto object based on institution.
     *
     * @param aInstitution institution that the dto should be based on
     *
     * @return {@linkplain InstitutionDto with given institution data}
     */
    public InstitutionDto getDtoFromInstitution( final Institution aInstitution )
    {
        final List< String > institutionTags = new ArrayList<>();
        if( aInstitution.getInstitutionTags() != null )
        {
            institutionTags.addAll( List.of( aInstitution
                    .getInstitutionTags().split( "," ) ) );
        }
        return InstitutionDto.builder()
                .id( aInstitution.getId() )
                .tags( institutionTags )
                .name( aInstitution.getName() )
                .headquartersAddress( aInstitution.getHeadquarters() )
                .localization( aInstitution.getLocalization() )
                .krsNumber( aInstitution.getKrsNumber() )
                .description( aInstitution.getDescription() )
                .build();
    }

    /**
     * Maps {@linkplain InterestCategory} to its DTO.
     *
     * @param aInterestCategory interest category to map.
     * @return dto.
     */
    public InterestCategoryDto interestCategoryToDto(final InterestCategory aInterestCategory)
    {
        return new InterestCategoryDto(aInterestCategory.getId(),
                aInterestCategory.getName(), aInterestCategory.getDescription());
    }

    /**
     * Maps {@linkplain OfferDto} to its entity {@linkplain Offer}
     *
     * @param aOfferDto dto to be mapped
     * @return entity based on dto
     */
    public Offer createOfferFromDto( final OfferDto aOfferDto )
    {
        final Offer createdOffer = new Offer();

        final User contactPerson = this.userService.loadEntity(aOfferDto.getContactPersonId());
        final List<InterestCategory> offerCategories = this.interestCategoryService
                .findByIds(aOfferDto.getInterestCategoryIds());
        final OfferType offerType = this.offerTypeService
                .loadEntity(aOfferDto.getOfferTypeId());

        final OfferState offerState = offerStateService.tryLoadByState( OfferStateEnum.NEW.getTranslatedState() )
                .orElseThrow();

        List<Benefit> benefits = null;
        Instant offerEndDate = null;

        if (aOfferDto.getOfferBenefitIds() != null && !aOfferDto.getOfferBenefitIds().isEmpty())
        {
            benefits = this.benefitService.findByIds(aOfferDto.getOfferBenefitIds());
        }
        if (aOfferDto.getEndDate() != null)
        {
            offerEndDate = aOfferDto.getEndDate().toInstant();
        }
        createdOffer.setTitle(aOfferDto.getOfferTitle());
        createdOffer.setExpirationDate(aOfferDto.getOfferExpirationDate().toInstant());
        createdOffer.setContactPerson(contactPerson);
        createdOffer.setInstitution(contactPerson.getInstitution());
        createdOffer.setOfferType(offerType);
        createdOffer.setStartDate(aOfferDto.getStartDate().toInstant());
        createdOffer.setEndDate(offerEndDate);
        createdOffer.setPeriodicDescription(aOfferDto.getPeriodicDescription());
        createdOffer.setInterestCategories(offerCategories);
        createdOffer.setIsExperienceRequired(aOfferDto.getIsExperienceRequired());
        createdOffer.setDescription(aOfferDto.getOfferDescription());
        createdOffer.setPlace(aOfferDto.getOfferPlace());
        createdOffer.setIsPoznanOnly(aOfferDto.getIsPoznanOnly());
        createdOffer.setBenefits(benefits);
        createdOffer.setOfferState(offerState);
        createdOffer.setOtherBenefits( aOfferDto.getOtherBenefits() );
        createdOffer.setOtherCategories( aOfferDto.getOtherCategories() );
        createdOffer.setIsHidden(false);
        return createdOffer;
    }

    /**
     * Maps {@linkplain Offer} to {@linkplain OfferBaseInfoDto}
     *
     * @param aOffer dto to be mapped.
     * @return dto containing base offer information.
     */
    public OfferBaseInfoDto createBaseInfoDtoOfOffer( final Offer aOffer ) {
        // there are cases where end date is null so Date.form method throws an exception
        Date endDate = null;
        if (aOffer.getEndDate() != null) {
            endDate = Date.from(aOffer.getEndDate());
        }
        return OfferBaseInfoDto.builder()
                .id( aOffer.getId() )
                .offerTitle( aOffer.getTitle() )
                .offerExpirationDate( Date.from( aOffer.getExpirationDate() ) )
                .offerTypeName( aOffer.getOfferType().getName() )
                .startDate( Date.from(aOffer.getStartDate()) )
                .endDate( endDate )
                .offerPlace(aOffer.getPlace() )
                .institutionName( aOffer.getInstitution().getName() )
                .isPoznanOnly( aOffer.getIsPoznanOnly() )
                .isHidden( aOffer.getIsHidden() )
                .institutionId( aOffer
                        .getInstitution()
                        .getId() )
                .build();
    }

    /**
     * Maps {@linkplain Offer} to {@linkplain OfferDetailsDto}
     *
     * @param aOffer object to be mapped.
     * @return dto containing base offer information.
     */
    public OfferDetailsDto createOfferDetailsDto(Offer aOffer )
    {
        InstitutionContactPersonDto institutionContactPersonDto = contactPersonToDto(aOffer.getContactPerson());
        OfferTypeDto offerTypeDto = offerTypeToDto(aOffer.getOfferType());
        List<InterestCategoryDto> interestCategoryDtos =
                aOffer.getInterestCategories().stream().map(this::interestCategoryToDto).toList();
        List<BenefitDto> benefitDtos = aOffer.getBenefits().stream().map(this::benefitToDto).toList();

        Date expirationDate = aOffer.getExpirationDate() != null ? Date.from(aOffer.getExpirationDate()) : null;
        Date startDate = aOffer.getStartDate() != null ? Date.from(aOffer.getStartDate()) : null;
        Date endDate = aOffer.getEndDate() != null ? Date.from(aOffer.getEndDate()) : null;

        return new OfferDetailsDto(aOffer.getId(), aOffer.getTitle(), expirationDate, institutionContactPersonDto,
                offerTypeDto, startDate, endDate, interestCategoryDtos, aOffer.getIsExperienceRequired(),
                aOffer.getDescription(), aOffer.getPlace(), aOffer.getPeriodicDescription(),
                aOffer.getIsPoznanOnly(), benefitDtos, aOffer.getInstitution().getId(),
                aOffer.getInstitution().getName(), aOffer.getOtherCategories(), aOffer.getOtherBenefits(),
                aOffer.getIsHidden() );
    }

    /**
     * Maps {@linkplain uam.volontario.dto.Application.ApplicationDto} to {@linkplain ApplicationDetailsDto}
     *
     * @param aApplication object to be mapped.
     * @return dto containing base offer information.
     */
    public ApplicationDetailsDto createApplicationDetailsDto( Application aApplication )
    {
        User volunteer = aApplication.getVolunteer();
        OfferBaseInfoDto offer = createBaseInfoDtoOfOffer( aApplication.getOffer() );
        List< InterestCategoryDto > interestCategories = volunteer.getVolunteerData().getInterestCategories().stream()
                .map( this::interestCategoryToDto ).toList();
        return new ApplicationDetailsDto( aApplication.getId(), aApplication.getState().getName(), volunteer.getFirstName(),
                volunteer.getLastName(), volunteer.getContactEmailAddress(),
                volunteer.getVolunteerData().getDomainEmailAddress(), volunteer.getPhoneNumber(),
                aApplication.getParticipationMotivation(), interestCategories, offer, aApplication.isStarred(),
                aApplication.getOffer().getContactPerson().getId(), aApplication.getDecisionReason(), volunteer.getId() );
    }

    /**
     * Maps {@linkplain uam.volontario.dto.Application.ApplicationDto} to {@linkplain ApplicationBaseInfoDto}
     *
     * @param aApplication object to be mapped.
     * @return dto containing base offer information.
     */
    public ApplicationBaseInfoDto createApplicationBaseInfosDto( Application aApplication )
    {
        User volunteer = aApplication.getVolunteer();
        OfferBaseInfoDto offer = createBaseInfoDtoOfOffer( aApplication.getOffer() );
        return new ApplicationBaseInfoDto(aApplication.getId(), volunteer.getFirstName(), volunteer.getLastName(),
                aApplication.getParticipationMotivation(), aApplication.isStarred(), aApplication.getState().getName(), offer );
    }

    /**
     * Transforms {@linkplain User} entity to {@linkplain AdministrativeUserDetailsDto}
     *
     * @param aUser {@linkplain User} entity to be transformed
     *
     * @return {@linkplain AdministrativeUserDetailsDto} dto with user information
     */
    public AdministrativeUserDetailsDto getAdmUserDetailsDtoFromUser( final User aUser )
    {
        return AdministrativeUserDetailsDto.builder()
                .userId( aUser.getId() )
                .firstName( aUser.getFirstName() )
                .lastName( aUser.getLastName() )
                .email( aUser.getContactEmailAddress() )
                .userRoles( aUser.getRoles() )
                .verified( aUser.isVerified() )
                .build();
    }

    public InstitutionWorkerDto getInstitutionWorkerDtoFromUser( final User aUser )
    {
        assert aUser.getInstitution() != null;
        final List< UserRole > roles = UserRole.mapRolesToUserRoles( aUser.getRoles() );
        final UserRole institutionRelatedRole = roles.stream()
                .filter( aUserRole -> aUserRole.equals( UserRole.INSTITUTION_ADMIN ) ||
                        aUserRole.equals( UserRole.INSTITUTION_EMPLOYEE ) )
                .findFirst().orElse( null );
        final String roleName = institutionRelatedRole == null ? null :
                institutionRelatedRole.getTranslatedRoleName();
        return InstitutionWorkerDto.builder()
                .id( aUser.getId() )
                .firstName( aUser.getFirstName() )
                .lastName( aUser.getLastName() )
                .institutionId( aUser.getInstitution().getId() )
                .institutionName( aUser.getInstitution().getName() )
                .role( roleName )
                .build();
    }

    /**
     * Converts user entity to the dto used for presenting basic user data.
     *
     * @param aUser entity that needs to be converted
     *
     * @return dto with all user information
     */
    public UserProfileDto getUserProfileDtoFromUser( final User aUser )
    {
        final List<String> userRoles = aUser.getUserRoles().stream()
                .map( UserRole::getTranslatedRoleName )
                .toList();
        final UserProfileDto.UserProfileDtoBuilder userProfileDtoBuilder = UserProfileDto.builder()
                .id( aUser.getId() )
                .firstName( aUser.getFirstName() )
                .lastName( aUser.getLastName() )
                .contactEmailAddress( aUser.getContactEmailAddress() )
                .phoneNumber( aUser.getPhoneNumber() )
                .userRoles( userRoles );
        if( aUser.getVolunteerData() != null )
        {
            final VolunteerData volunteerData = aUser.getVolunteerData();
            userProfileDtoBuilder
                    .domainEmailAddress( volunteerData.getDomainEmailAddress() )
                    .interestCategories( volunteerData.getInterestCategories() )
                    .participationMotivation( volunteerData.getParticipationMotivation() )
                    .experienceDescription( volunteerData.getExperienceDescription() )
                    .interests( volunteerData.getInterests() )
                    .fieldOfStudy( volunteerData.getFieldOfStudy() );
        }
        if( aUser.getInstitution() != null )
        {
            final Institution institution = aUser.getInstitution();
            userProfileDtoBuilder
                    .institutionId( institution.getId() )
                    .institutionName( institution.getName() );
        }
        return userProfileDtoBuilder.build();
    }

    /**
     * Converts user entity to the dto used for presenting basic user data and containing new JWT.
     *
     * @param aUser entity that needs to be converted
     *
     * @return dto with all user information
     */
    public UserWithJwtDto getUserProfileWithJwtDtoFromUser( final User aUser, final Map<String, String> aTokens )
    {
        final String refreshToken = aTokens.get( JWTService.REFRESH_TOKEN_MAP_ENTRY );
        final String token = aTokens.get( JWTService.TOKEN_MAP_ENTRY );
        final List< UserRole > userRoles = aUser.getUserRoles();
        final UserWithJwtDto.UserWithJwtDtoBuilder userProfileDtoBuilder = UserWithJwtDto.builder()
                .id( aUser.getId() )
                .firstName( aUser.getFirstName() )
                .lastName( aUser.getLastName() )
                .contactEmailAddress( aUser.getContactEmailAddress() )
                .phoneNumber( aUser.getPhoneNumber() )
                .roles( userRoles )
                .token( token )
                .refreshToken( refreshToken );
        if( aUser.getVolunteerData() != null )
        {
            final VolunteerData volunteerData = aUser.getVolunteerData();
            userProfileDtoBuilder
                    .domainEmailAddress( volunteerData.getDomainEmailAddress() )
                    .volunteerData( volunteerData );
        }
        if( aUser.getInstitution() != null )
        {
            final Institution institution = aUser.getInstitution();
            userProfileDtoBuilder
                    .institution( institution );
        }
        return userProfileDtoBuilder.build();
    }

    /**
     * Maps Voluntary Rating of Volunteer to dto.
     *
     * @param aVoluntaryRating voluntary rating.
     *
     * @return volunteer rating dto.
     */
    public VolunteerRatingDto volunteerRatingToDto( final VoluntaryRating aVoluntaryRating )
    {
        return VolunteerRatingDto.builder()
                .volunteerId( aVoluntaryRating.getVolunteer().getId() )
                .rating( aVoluntaryRating.getVolunteerRating() )
                .contactPersonId( aVoluntaryRating.getOffer().getContactPerson().getId() )
                .contactPersonName( aVoluntaryRating.getOffer().getContactPerson().getFullName() )
                .institutionName( aVoluntaryRating.getInstitution().getName() )
                .ratingComment( aVoluntaryRating.getVolunteerRatingReason() )
                .offerName( aVoluntaryRating.getOffer().getTitle() )
                .offerId( aVoluntaryRating.getOffer().getId() )
                .build();
    }

    /**
     * Maps Voluntary Rating of Institution to dto.
     *
     * @param aVoluntaryRating voluntary rating.
     *
     * @return institution rating dto.
     */
    public InstitutionRatingDto institutionRatingToDto( final VoluntaryRating aVoluntaryRating )
    {
        return InstitutionRatingDto.builder()
                .volunteerId( aVoluntaryRating.getVolunteer().getId() )
                .volunteerName( aVoluntaryRating.getVolunteer().getFullName() )
                .rating( aVoluntaryRating.getInstitutionRating() )
                .ratingComment( aVoluntaryRating.getInstitutionRatingReason() )
                .offerName( aVoluntaryRating.getOffer().getTitle() )
                .offerId( aVoluntaryRating.getOffer().getId() )
                .build();
    }

    private BenefitDto benefitToDto( Benefit aBenefit )
    {
        return new BenefitDto( aBenefit.getId(), aBenefit.getName() );
    }

    private OfferTypeDto offerTypeToDto( OfferType offerType )
    {
        return new OfferTypeDto( offerType.getId(), offerType.getName() );
    }

    private InstitutionContactPersonDto contactPersonToDto( User contactPerson )
    {
        return new InstitutionContactPersonDto( contactPerson.getId(), contactPerson.getFirstName(), contactPerson.getLastName(),
                contactPerson.getPhoneNumber(), contactPerson.getContactEmailAddress() );
    }
}
