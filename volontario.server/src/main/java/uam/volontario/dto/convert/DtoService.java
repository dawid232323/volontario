package uam.volontario.dto.convert;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.volontario.crud.service.*;
import uam.volontario.dto.*;
import uam.volontario.dto.Application.ApplicationBaseInfoDto;
import uam.volontario.dto.Application.ApplicationDetailsDto;
import uam.volontario.dto.Institution.InstitutionContactPersonDto;
import uam.volontario.dto.Institution.InstitutionDto;
import uam.volontario.dto.Institution.InterestCategoryDto;
import uam.volontario.dto.Offer.OfferBaseInfoDto;
import uam.volontario.dto.Offer.OfferDetailsDto;
import uam.volontario.dto.Offer.OfferDto;
import uam.volontario.dto.Offer.OfferTypeDto;
import uam.volontario.model.common.UserRole;
import uam.volontario.model.common.impl.Role;
import uam.volontario.model.common.impl.User;
import uam.volontario.model.institution.impl.Institution;
import uam.volontario.model.institution.impl.InstitutionContactPerson;
import uam.volontario.model.offer.impl.*;
import uam.volontario.model.volunteer.impl.ExperienceLevel;
import uam.volontario.model.volunteer.impl.InterestCategory;
import uam.volontario.model.volunteer.impl.VolunteerData;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for DTO operations.
 */
@Service
public class DtoService
{
    private final InterestCategoryService interestCategoryService;

    private final ExperienceLevelService experienceLevelService;

    private final RoleService roleService;

    private final BenefitService benefitService;

    private final UserService userService;

    private final OfferTypeService offerTypeService;

    private final OfferStateService offerStateService;

    /**
     * CDI constructor.
     *
     * @param aInterestCategoryService interest category service.
     * @param aExperienceLevelService  experience level service.
     */
    @Autowired
    public DtoService( final InterestCategoryService aInterestCategoryService, final ExperienceLevelService
            aExperienceLevelService, final RoleService aRoleService, final BenefitService aBenefitService,
                      final UserService aUserService, final OfferTypeService aOfferTypeService, final OfferStateService aOfferStateService )
    {
        interestCategoryService = aInterestCategoryService;
        experienceLevelService = aExperienceLevelService;
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
     * @return Volunteer.
     */
    public User createVolunteerFromDto( final VolunteerDto aDto )
    {
        final List<Role> roles = roleService.findByNameIn(UserRole.mapUserRolesToRoleNames(List.of(UserRole.VOLUNTEER)));

        final ExperienceLevel experienceLevel = experienceLevelService.tryLoadEntity(aDto.getExperienceId())
                .orElseThrow();

        final List<InterestCategory> volunteerInterestCategories = interestCategoryService.findByIds(
                aDto.getInterestCategoriesIds());

        final VolunteerData volunteerData = VolunteerData.builder()
                .experience(experienceLevel)
                .participationMotivation(aDto.getParticipationMotivation())
                .domainEmailAddress(aDto.getDomainEmail())
                .interestCategories(volunteerInterestCategories).build();

        final User user = User.builder().firstName(aDto.getFirstName())
                .lastName(aDto.getLastName())
                .password(aDto.getPassword())
                .contactEmailAddress(aDto.getContactEmail())
                .phoneNumber(aDto.getPhoneNumber())
                .roles(roles)
                .isVerified(true) // TODO: for now until email verification is implemented.
                .volunteerData(volunteerData)
                .build();

        user.getVolunteerData().setUser(user);

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
     * Maps {@linkplain ExperienceLevel} to its DTO.
     *
     * @param aExperienceLevel volunteer experience level to map.
     * @return dto.
     */
    public ExperienceLevelDto volunteerExperienceToDto( final ExperienceLevel aExperienceLevel )
    {
        return new ExperienceLevelDto(aExperienceLevel.getId(), aExperienceLevel.getName(),
                aExperienceLevel.getDefinition(), aExperienceLevel.getValue());
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

        final OfferState offerState = offerStateService.tryLoadByState(OfferStateEnum
                        .mapOfferStateEnumToOfferStateName(OfferStateEnum.NEW))
                .orElseThrow();

        ExperienceLevel offerMinExperience = null;
        List<Benefit> benefits = null;
        Instant offerEndDate = null;

        if (aOfferDto.getIsExperienceRequired())
        {
            offerMinExperience = this.experienceLevelService
                    .loadEntity(aOfferDto.getExperienceLevelId());
        }
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
        createdOffer.setMinimumExperience(offerMinExperience);
        createdOffer.setDescription(aOfferDto.getOfferDescription());
        createdOffer.setPlace(aOfferDto.getOfferPlace());
        createdOffer.setIsPoznanOnly(aOfferDto.getIsPoznanOnly());
        createdOffer.setBenefits(benefits);
        createdOffer.setOfferState(offerState);
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
        return new OfferBaseInfoDto(aOffer.getId(), aOffer.getTitle(), Date.from(aOffer.getExpirationDate()),
                aOffer.getOfferType().getName(), Date.from(aOffer.getStartDate()),
                endDate, aOffer.getPlace(), aOffer.getInstitution().getName(), aOffer.getIsPoznanOnly());
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
        ExperienceLevelDto experienceLevelDto = aOffer.getMinimumExperience() != null
                ? volunteerExperienceToDto(aOffer.getMinimumExperience()) : null;
        List<BenefitDto> benefitDtos = aOffer.getBenefits().stream().map(this::benefitToDto).toList();

        Date expirationDate = aOffer.getExpirationDate() != null ? Date.from(aOffer.getExpirationDate()) : null;
        Date startDate = aOffer.getStartDate() != null ? Date.from(aOffer.getStartDate()) : null;
        Date endDate = aOffer.getEndDate() != null ? Date.from(aOffer.getEndDate()) : null;

        return new OfferDetailsDto(aOffer.getId(), aOffer.getTitle(), expirationDate, institutionContactPersonDto,
                offerTypeDto, startDate, endDate, interestCategoryDtos, aOffer.getIsExperienceRequired(),
                experienceLevelDto, aOffer.getDescription(), aOffer.getPlace(), aOffer.getPeriodicDescription(),
                aOffer.getIsPoznanOnly(), benefitDtos);
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
        ExperienceLevelDto experience = volunteerExperienceToDto( volunteer.getVolunteerData().getExperience() );
        List< InterestCategoryDto > interestCategories = volunteer.getVolunteerData().getInterestCategories().stream()
                .map( this::interestCategoryToDto ).toList();
        return new ApplicationDetailsDto( aApplication.getId(), aApplication.getState().getName(), volunteer.getFirstName(),
                volunteer.getLastName(), volunteer.getContactEmailAddress(),
                volunteer.getVolunteerData().getDomainEmailAddress(), volunteer.getPhoneNumber(), experience,
                aApplication.getParticipationMotivation(), interestCategories, offer, aApplication.isStarred(),
                aApplication.getOffer().getContactPerson().getId() );
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
