import { ObjectBuilderIf } from 'src/app/core/interface/object-builder.interface';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
import { isNil } from 'lodash';

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

export class AdvertisementBenefit {
  constructor(public id: number, public name: string) {}

  public static fromPayload(payload?: any): AdvertisementBenefit {
    return new AdvertisementBenefit(payload?.id, payload?.name);
  }
}

interface AdvertisementDtoIf {
  offerTitle?: string;
  offerExpirationDate?: Date;
  contactPersonId?: number;
  offerTypeId?: number;
  startDate?: Date;
  endDate?: Date;
  offerWeekDays?: number[];
  durationUnit?: string;
  durationValue?: number;
  offerInterval?: string;
  interestCategoryIds?: number[];
  isExperienceRequired?: boolean;
  experienceLevelId?: number;
  offerDescription?: string;
  offerPlace?: string;
  isPoznanOnly?: boolean;
  offerBenefitIds?: number[];
  isInsuranceNeeded?: boolean;
}

export class AdvertisementDtoBuilder
  implements AdvertisementDtoIf, ObjectBuilderIf<AdvertisementUpdateCreateDto>
{
  contactPersonId?: number;
  durationUnit?: string;
  durationValue?: number;
  endDate?: Date;
  experienceLevelId?: number;
  interestCategoryIds?: number[];
  isExperienceRequired?: boolean;
  isInsuranceNeeded?: boolean;
  isPoznanOnly?: boolean;
  offerBenefitIds?: number[];
  offerDescription?: string;
  offerExpirationDate?: Date;
  offerInterval?: string;
  offerPlace?: string;
  offerTitle?: string;
  offerTypeId?: number;
  offerWeekDays?: number[];
  startDate?: Date;

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
      this.offerWeekDays,
      this.durationUnit,
      this.durationValue,
      this.offerInterval,
      this.interestCategoryIds,
      this.isExperienceRequired,
      this.experienceLevelId,
      this.offerDescription,
      this.offerPlace,
      this.isPoznanOnly,
      this.offerBenefitIds,
      this.isInsuranceNeeded
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

  public setOfferWeekDays(days: number[]): AdvertisementDtoBuilder {
    this.offerWeekDays = days;
    return this;
  }

  public setDurationUnit(unit: string): AdvertisementDtoBuilder {
    this.durationUnit = unit;
    return this;
  }

  public setDurationValue(value: number): AdvertisementDtoBuilder {
    this.durationValue = value;
    return this;
  }

  public setOfferInterval(interval: string): AdvertisementDtoBuilder {
    this.offerInterval = interval;
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

  public setIsInsuranceNeeded(isInsNeeded: boolean): AdvertisementDtoBuilder {
    this.isInsuranceNeeded = isInsNeeded;
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
    public offerWeekDays?: number[],
    public durationUnit?: string,
    public durationValue?: number,
    public offerInterval?: string,
    public interestCategoryIds?: number[],
    public isExperienceRequired?: boolean,
    public experienceLevelId?: number,
    public offerDescription?: string,
    public offerPlace?: string,
    public isPoznanOnly?: boolean,
    public offerBenefitIds?: number[],
    public isInsuranceNeeded?: boolean
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
    public institutionName: string
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
      payload?.institutionName
    );
  }
}

export class AdvertisementDto {
  constructor(
    public id: number,
    public offerTitle: string,
    public offerExpirationDate: Date,
    public contactPerson: any,
    public offerType: AdvertisementType,
    public startDate: Date,
    public endDate: Date,
    public offerWeekDays: number[],
    public offerInterval: string,
    public interestCategories: InterestCategoryDTO[],
    public isExperienceRequired: boolean,
    public experienceLevel: VolunteerExperience,
    public offerDescription: string,
    public offerPlace: string,
    public isPoznanOnly: boolean,
    public offerBenefitIds: AdvertisementBenefit[],
    public isInsuranceNeeded: boolean
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
      payload?.offerWeekDays,
      payload?.offerInterval,
      payload?.interestCategories,
      payload?.isExperienceRequired,
      payload?.experienceLevel,
      payload?.offerDescription,
      payload?.offerPlace,
      payload?.isPoznanOnly,
      payload?.offerBenefitIds,
      payload?.isInsuranceNeeded
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
    public daysOfWeek: number[],
    public interval: string | null,
    public durationUnit: string,
    public durationValue: number
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
      advertisement?.offerWeekDays,
      advertisement?.offerInterval,
      '',
      1
    );
  }
}

export class AdvertisementAdditionalInfo {
  constructor(
    public advertisementCategories: number[],
    public isExperienceRequired: boolean,
    public experienceLevel: number,
    public description: string
  ) {}

  public static fromAdvertisementDto(
    advertisement: AdvertisementDto
  ): AdvertisementAdditionalInfo {
    return new AdvertisementAdditionalInfo(
      advertisement.interestCategories.map(category => category.id),
      advertisement?.isExperienceRequired,
      advertisement?.experienceLevel?.id,
      advertisement?.offerDescription
    );
  }
}

export class AdvertisementOptionalInfo {
  constructor(
    public isPoznanOnly: boolean,
    public eventPlace: string,
    public benefits: number[],
    public isInsuranceNeeded: boolean
  ) {}

  public static fromAdvertisementDto(
    advertisement: AdvertisementDto
  ): AdvertisementOptionalInfo {
    return new AdvertisementOptionalInfo(
      advertisement?.isPoznanOnly,
      advertisement?.offerPlace,
      this.getBenefitIds(advertisement?.offerBenefitIds),
      advertisement?.isInsuranceNeeded
    );
  }

  private static getBenefitIds(benefits?: AdvertisementBenefit[]): number[] {
    if (isNil(benefits)) {
      return [];
    }
    return benefits.map(benefit => benefit.id);
  }
}
