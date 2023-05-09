import { ObjectBuilderIf } from 'src/app/core/interface/object-builder.interface';

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
  implements AdvertisementDtoIf, ObjectBuilderIf<AdvertisementDto>
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

  public build(): AdvertisementDto {
    return new AdvertisementDto(
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
export class AdvertisementDto implements AdvertisementDtoIf {
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
    public offerTypeName: string
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
      payload?.offerTypeName
    );
  }
}
