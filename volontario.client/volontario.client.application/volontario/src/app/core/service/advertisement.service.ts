import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import {
  AdvertisementBenefit,
  AdvertisementDto,
  AdvertisementDtoBuilder,
  AdvertisementType,
} from 'src/app/core/model/advertisement.model';
import { EndpointUrls } from 'src/app/utils/url.util';
import { map, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AdvertisementService {
  constructor(private restService: VolontarioRestService) {}

  public getAllAdvertisementTypes(): Observable<AdvertisementType[]> {
    return this.restService.get(EndpointUrls.advertisementType).pipe(
      map(result => {
        return result.map(AdvertisementType.fromPayload);
      })
    );
  }

  public getAllAdvertisementBenefits(): Observable<AdvertisementBenefit[]> {
    return this.restService
      .get(EndpointUrls.advertisementBenefits)
      .pipe(map(result => result.map(AdvertisementBenefit.fromPayload)));
  }

  public createNewAdvertisement(body: AdvertisementDto): Observable<any> {
    return this.restService.post(EndpointUrls.advertisementResource, body);
  }

  /**
   * Creates advertisement dto from combined form values
   *
   * @param value combined values from basic, additional and optional form groups
   *
   * @return returns valid dto that can be processed later on
   */
  public getAdvertisementDtoFromValue(value: any): AdvertisementDto {
    return AdvertisementDtoBuilder.builder()
      .setContactPersonId(value.contactPerson)
      .setDurationUnit(value.durationUnit)
      .setDurationValue(value.durationValue)
      .setOfferTitle(value.title)
      .setExpirationDate(value.expirationDate)
      .setOfferTypeId(value.advertisementType)
      .setStartDate(value.startDate)
      .setEndDate(value.endDate)
      .setOfferWeekDays(value.daysOfWeek)
      .setOfferInterval(value.interval)
      .setInterestCategoriesIds(value.advertisementCategories)
      .setIsExperienceRequired(value.isExperienceRequired)
      .setExperienceLevelId(value.experienceLevel)
      .setOfferDescription(value.description)
      .setIsPoznanOnly(value.isPoznanOnly)
      .setOfferPlace(value.eventPlace)
      .setBenefitIds(value.benefits)
      .setIsInsuranceNeeded(value.isInsuranceNeeded)
      .build();
  }
}
