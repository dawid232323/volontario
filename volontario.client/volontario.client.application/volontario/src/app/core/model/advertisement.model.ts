import { ObjectBuilderIf } from 'src/app/core/interface/object-builder.interface';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
import { isNil } from 'lodash';
import { User } from 'src/app/core/model/user.model';
import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';

export enum AdvertisementTypeEnum {
  SingleUse = 1,
  Regular = 2,
  Continuous = 3,
}

export class AdvertisementType {
  constructor(public id: number, public name: string) {}

  public static fromPayload(payload?: any): AdvertisementType {
    return new AdvertisementType(payload?.id, payload?.name);
  }
}

export class AdvertisementBenefit implements DictionaryValueInterface {
  constructor(public id: number, public name: string, public isUsed: boolean) {}

  public static fromPayload(payload?: any): AdvertisementBenefit {
    return new AdvertisementBenefit(payload?.id, payload?.name, payload?.used);
  }
}

interface AdvertisementDtoIf {
  offerTitle?: string;
  offerExpirationDate?: Date;
  contactPersonId?: number;
  offerTypeId?: number;
  startDate?: Date;
  endDate?: Date;
  interestCategoryIds?: number[];
  isExperienceRequired?: boolean;
  experienceLevelId?: number;
  offerDescription?: string;
  offerPlace?: string;
  isPoznanOnly?: boolean;
  offerBenefitIds?: number[];
  periodicDescription?: string;
  otherCategories?: string;
  otherBenefits?: string;
}

export class AdvertisementDtoBuilder
  implements AdvertisementDtoIf, ObjectBuilderIf<AdvertisementUpdateCreateDto>
{
  contactPersonId?: number;
  endDate?: Date;
  experienceLevelId?: number;
  interestCategoryIds?: number[];
  isExperienceRequired?: boolean;
  isPoznanOnly?: boolean;
  offerBenefitIds?: number[];
  offerDescription?: string;
  offerExpirationDate?: Date;
  offerPlace?: string;
  offerTitle?: string;
  offerTypeId?: number;
  startDate?: Date;
  periodicDescription?: string;
  otherCategories?: string;
  otherBenefits?: string;

  public static builder(): AdvertisementDtoBuilder {
    return new AdvertisementDtoBuilder();
  }

  public build(): AdvertisementUpdateCreateDto {
    return new AdvertisementUpdateCreateDto(
      this.offerTitle,
      this.offerExpirationDate,
      this.contactPersonId,
      this.offerTypeId,
      this.startDate,
      this.endDate,
      this.interestCategoryIds,
      this.isExperienceRequired,
      this.experienceLevelId,
      this.offerDescription,
      this.offerPlace,
      this.isPoznanOnly,
      this.offerBenefitIds,
      this.periodicDescription,
      this.otherCategories,
      this.otherBenefits
    );
  }

  public setContactPersonId(id: number): AdvertisementDtoBuilder {
    this.contactPersonId = id;
    return this;
  }

  public setOfferTitle(title: string): AdvertisementDtoBuilder {
    this.offerTitle = title;
    return this;
  }

  public setExpirationDate(expirationDate: Date): AdvertisementDtoBuilder {
    this.offerExpirationDate = expirationDate;
    return this;
  }

  public setOfferTypeId(id: number): AdvertisementDtoBuilder {
    this.offerTypeId = id;
    return this;
  }

  public setStartDate(date: Date): AdvertisementDtoBuilder {
    this.startDate = date;
    return this;
  }

  public setEndDate(date: Date): AdvertisementDtoBuilder {
    this.endDate = date;
    return this;
  }

  public setInterestCategoriesIds(ids: number[]): AdvertisementDtoBuilder {
    this.interestCategoryIds = ids;
    return this;
  }

  public setIsExperienceRequired(isRequired: boolean): AdvertisementDtoBuilder {
    this.isExperienceRequired = isRequired;
    return this;
  }

  public setExperienceLevelId(experienceId: number): AdvertisementDtoBuilder {
    this.experienceLevelId = experienceId;
    return this;
  }

  public setOfferDescription(description: string): AdvertisementDtoBuilder {
    this.offerDescription = description;
    return this;
  }

  public setOfferPlace(place: string): AdvertisementDtoBuilder {
    this.offerPlace = place;
    return this;
  }

  public setIsPoznanOnly(isPoznanOnly: boolean): AdvertisementDtoBuilder {
    this.isPoznanOnly = isPoznanOnly;
    return this;
  }

  public setBenefitIds(benefitIds: number[]): AdvertisementDtoBuilder {
    this.offerBenefitIds = benefitIds;
    return this;
  }

  public setPeriodicDescription(description: string): AdvertisementDtoBuilder {
    this.periodicDescription = description;
    return this;
  }

  public setOtherCategories(otherCategories: string): AdvertisementDtoBuilder {
    this.otherCategories = otherCategories;
    return this;
  }

  public setOtherBenefits(otherBenefits: string): AdvertisementDtoBuilder {
    this.otherBenefits = otherBenefits;
    return this;
  }
}

/**
 * Object that represents dto to create and update advertisement
 */
export class AdvertisementUpdateCreateDto implements AdvertisementDtoIf {
  constructor(
    public offerTitle?: string,
    public offerExpirationDate?: Date,
    public contactPersonId?: number,
    public offerTypeId?: number,
    public startDate?: Date,
    public endDate?: Date,
    public interestCategoryIds?: number[],
    public isExperienceRequired?: boolean,
    public experienceLevelId?: number,
    public offerDescription?: string,
    public offerPlace?: string,
    public isPoznanOnly?: boolean,
    public offerBenefitIds?: number[],
    public periodicDescription?: string,
    public otherCategories?: string,
    public otherBenefits?: string
  ) {}
}

export class AdvertisementPreview {
  constructor(
    public id: number,
    public offerTitle: string,
    public startDate: Date,
    public endDate: Date,
    public offerPlace: string,
    public isPoznanOnly: boolean,
    public offerExpirationDate: Date,
    public offerTypeName: string,
    public institutionName: string,
    public isHidden: boolean,
    public applicationsCount: number,
    public institutionId: number
  ) {}

  public static fromPayload(payload?: any): AdvertisementPreview {
    return new AdvertisementPreview(
      payload?.id,
      payload?.offerTitle,
      payload?.startDate,
      payload?.endDate,
      payload?.offerPlace,
      payload?.isPoznanOnly,
      payload?.offerExpirationDate,
      payload?.offerTypeName,
      payload?.institutionName,
      payload?.isHidden,
      payload?.applicationsCount,
      payload?.institutionId
    );
  }
}

export class AdvertisementDto {
  constructor(
    public id: number,
    public offerTitle: string,
    public offerExpirationDate: Date,
    public contactPerson: User,
    public offerType: AdvertisementType,
    public startDate: Date,
    public endDate: Date,
    public interestCategories: InterestCategoryDTO[],
    public isExperienceRequired: boolean,
    public experienceLevel: VolunteerExperience,
    public offerDescription: string,
    public offerPlace: string,
    public isPoznanOnly: boolean,
    public offerBenefitIds: AdvertisementBenefit[],
    public periodicDescription: string,
    public institutionId: number,
    public institutionName: string,
    public otherCategories: string,
    public otherBenefits: string,
    public hidden: boolean
  ) {}

  public static fromPayload(payload?: any): AdvertisementDto {
    return new AdvertisementDto(
      payload?.id,
      payload?.offerTitle,
      payload?.offerExpirationDate,
      payload?.contactPerson,
      payload?.offerType,
      payload?.startDate,
      payload?.endDate,
      payload?.interestCategories,
      payload?.isExperienceRequired,
      payload?.experienceLevel,
      payload?.offerDescription,
      payload?.offerPlace,
      payload?.isPoznanOnly,
      payload?.offerBenefits,
      payload?.periodicDescription,
      payload?.institutionId,
      payload?.institutionName,
      payload?.otherCategories,
      payload?.otherBenefits,
      payload?.hidden
    );
  }
}

export class AdvertisementBasicInfo {
  constructor(
    public title: string,
    public contactPerson: number,
    public expirationDate: Date,
    public advertisementType: number,
    public startDate: Date,
    public endDate: Date,
    public periodicDescription: string
  ) {}

  public static fromAdvertisementDto(
    advertisement: AdvertisementDto
  ): AdvertisementBasicInfo {
    return new AdvertisementBasicInfo(
      advertisement?.offerTitle,
      advertisement?.contactPerson?.id,
      advertisement?.offerExpirationDate,
      advertisement?.offerType?.id,
      advertisement?.startDate,
      advertisement?.endDate,
      advertisement?.periodicDescription
    );
  }
}

export class AdvertisementAdditionalInfo {
  constructor(
    public advertisementCategories: number[],
    public isExperienceRequired: boolean,
    public experienceLevel: number | null,
    public description: string,
    public otherCategories: string | null
  ) {}

  public static fromAdvertisementDto(
    advertisement: AdvertisementDto
  ): AdvertisementAdditionalInfo {
    const categoryIds = advertisement.interestCategories.map(
      category => category.id
    );
    if (!isNil(advertisement?.otherCategories)) {
      categoryIds.push(-1);
    }
    return new AdvertisementAdditionalInfo(
      categoryIds,
      advertisement?.isExperienceRequired,
      isNil(advertisement?.experienceLevel?.id)
        ? null
        : advertisement?.experienceLevel?.id,
      advertisement?.offerDescription,
      advertisement?.otherCategories
    );
  }
}

export class AdvertisementOptionalInfo {
  constructor(
    public isPoznanOnly: boolean,
    public eventPlace: string,
    public benefits: number[],
    public otherBenefits: string | null
  ) {}

  public static fromAdvertisementDto(
    advertisement: AdvertisementDto
  ): AdvertisementOptionalInfo {
    const benefitIds = this.getBenefitIds(advertisement?.offerBenefitIds);
    if (!isNil(advertisement?.otherBenefits)) {
      benefitIds.push(-1);
    }
    return new AdvertisementOptionalInfo(
      advertisement?.isPoznanOnly,
      advertisement?.offerPlace,
      benefitIds,
      advertisement?.otherBenefits
    );
  }

  private static getBenefitIds(benefits?: AdvertisementBenefit[]): number[] {
    if (isNil(benefits)) {
      return [];
    }
    return benefits.map(benefit => benefit.id);
  }
}
