import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import {
  AdvertisementBenefit,
  AdvertisementUpdateCreateDto,
  AdvertisementDtoBuilder,
  AdvertisementPreview,
  AdvertisementType,
  AdvertisementDto,
} from 'src/app/core/model/advertisement.model';
import { EndpointUrls } from 'src/app/utils/url.util';
import { map, Observable, of } from 'rxjs';
import { HttpParams } from '@angular/common/http';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';
import { PageableModelInterface } from 'src/app/core/model/pageable.model';
import { isNil, result } from 'lodash';

/**
 * Object that stores information about user filter preferences on the advertisement panel list.
 */
export interface AdvertisementFilterIf {
  institutionId?: number;
  contactPersonId?: number;
  title?: string;
  startDate?: Date | string;
  endDate?: Date | string;
  interestCategoryIds?: number[];
  typeIds?: number[];
}

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

  public createNewAdvertisement(
    body: AdvertisementUpdateCreateDto
  ): Observable<any> {
    return this.restService.post(EndpointUrls.advertisementResource, body);
  }

  public updateAdvertisement(
    advertisementId: number,
    body: AdvertisementUpdateCreateDto
  ): Observable<any> {
    return this.restService.put(
      `${EndpointUrls.advertisementResource}/${advertisementId}`,
      body
    );
  }

  public getAdvertisementPreviews(
    filters: AdvertisementFilterIf,
    pageNumber?: number,
    pageSize?: number
  ): Observable<PageableModelInterface<AdvertisementPreview>> {
    if (isNil(pageNumber)) {
      pageNumber = 0;
    }
    if (isNil(pageSize)) {
      pageSize = 5;
    }
    const params = new HttpParams({
      fromObject: { page: pageNumber, limit: pageSize, ...(<any>filters) },
    });
    const options: HttpOptionsInterface = { params: params };
    return this.restService
      .get(EndpointUrls.advertisementSearch, options)
      .pipe(
        map(result => <PageableModelInterface<AdvertisementPreview>>result)
      );
  }

  public getAdvertisement(
    advertisementId: number
  ): Observable<AdvertisementDto> {
    return this.restService
      .get(`${EndpointUrls.advertisementDetails}/${advertisementId}`)
      .pipe(map(result => AdvertisementDto.fromPayload(result)));
  }

  /**
   * Creates advertisement dto from combined form values
   *
   * @param value combined values from basic, additional and optional form groups
   *
   * @return returns valid dto that can be processed later on
   */
  public getAdvertisementDtoFromValue(
    value: any
  ): AdvertisementUpdateCreateDto {
    return AdvertisementDtoBuilder.builder()
      .setContactPersonId(value.contactPerson)
      .setOfferTitle(value.title)
      .setExpirationDate(value.expirationDate)
      .setOfferTypeId(value.advertisementType)
      .setStartDate(value.startDate)
      .setEndDate(value.endDate)
      .setInterestCategoriesIds(value.advertisementCategories)
      .setIsExperienceRequired(value.isExperienceRequired)
      .setExperienceLevelId(value.experienceLevel)
      .setOfferDescription(value.description)
      .setIsPoznanOnly(value.isPoznanOnly)
      .setOfferPlace(value.eventPlace)
      .setBenefitIds(value.benefits)
      .setPeriodicDescription(value.periodicDescription)
      .build();
  }
}
