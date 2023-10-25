import { EventEmitter, Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import {
  AdvertisementBenefit,
  AdvertisementDto,
  AdvertisementDtoBuilder,
  AdvertisementPreview,
  AdvertisementType,
  AdvertisementUpdateCreateDto,
} from 'src/app/core/model/advertisement.model';
import { EndpointUrls } from 'src/app/utils/url.util';
import { map, Observable, Subject } from 'rxjs';
import { HttpParams } from '@angular/common/http';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';
import { PageableModelInterface } from 'src/app/core/model/pageable.model';
import { isNil } from 'lodash';
import { User } from 'src/app/core/model/user.model';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import { OfferVisibilityInterface } from 'src/app/core/interface/offer-visibility.interface';
import { Params } from '@angular/router';
import { SecurityService } from 'src/app/core/service/security/security.service';

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
  visibility?: AdvertisementVisibilityEnum;
}

export enum AdvertisementVisibilityEnum {
  Active = 'active',
  Hidden = 'hidden',
  All = 'all',
}

@Injectable({ providedIn: 'root' })
export class AdvertisementService {
  public addAdvertisementReloadEvent: Subject<void> = new Subject<void>();
  private _offerListQueryParams?: Params;

  constructor(
    private restService: VolontarioRestService,
    private securityService: SecurityService
  ) {
    this.securityService.logoutEvent.subscribe(() => {
      this._offerListQueryParams = undefined;
    });
  }

  public getAllAdvertisementTypes(): Observable<AdvertisementType[]> {
    return this.restService.get(EndpointUrls.advertisementType).pipe(
      map(result => {
        return result.map(AdvertisementType.fromPayload);
      })
    );
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
      fromObject: { page: pageNumber, size: pageSize, ...(<any>filters) },
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

  public changeOfferVisibility(
    offerId: number,
    visibilityIf: OfferVisibilityInterface
  ): Observable<AdvertisementPreview> {
    return this.restService
      .patch(
        EndpointUrls.advertisementChangeVisibilityResource.concat(
          `/${offerId}`
        ),
        visibilityIf
      )
      .pipe(map(result => AdvertisementPreview.fromPayload(result)));
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

  get offerListQueryParams(): Params | undefined {
    return this._offerListQueryParams;
  }

  set offerListQueryParams(value: Params | undefined) {
    this._offerListQueryParams = value;
  }

  public canManageOffer(
    advertisement: AdvertisementDto | null,
    loggedUser?: User
  ): boolean {
    if (isNil(loggedUser) || isNil(advertisement)) {
      return false;
    }
    if (loggedUser.hasUserRoles([UserRoleEnum.Admin, UserRoleEnum.Moderator])) {
      return true;
    }
    if (
      loggedUser.hasUserRole(UserRoleEnum.InstitutionAdmin) &&
      advertisement.institutionId === loggedUser.institution?.id
    ) {
      return true;
    }
    if (
      loggedUser.hasUserRole(UserRoleEnum.InstitutionWorker) &&
      advertisement.contactPerson.id === loggedUser.id
    ) {
      return true;
    }
    return false;
  }
}
