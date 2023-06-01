import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import {
  OfferApplicationModel,
  OfferApplicationModelDto,
} from 'src/app/core/model/offerApplication.model';
import { EndpointUrls } from 'src/app/utils/url.util';
import { map, Observable } from 'rxjs';
import { isNil } from 'lodash';
import { HttpParams } from '@angular/common/http';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';
import { PageableModel } from 'src/app/core/model/pageable.model';
import {
  ApplicationBaseInfo,
  ApplicationDetails,
} from 'src/app/core/model/application.model';
import { throwUnknownPortalTypeError } from '@angular/cdk/portal/portal-errors';

export interface BaseApplicationFiltersIf {
  state?: number;
  starred?: boolean;
  offerId?: number;
  volunteerId?: number;
  institutionId?: number;
}
@Injectable({ providedIn: 'root' })
export class OfferApplicationService {
  constructor(private restService: VolontarioRestService) {}

  public createApplication(
    applicationBody: OfferApplicationModelDto
  ): Observable<OfferApplicationModel> {
    return this.restService
      .post(EndpointUrls.offerApplicationResource, applicationBody)
      .pipe(map(result => OfferApplicationModel.fromPayload(result)));
  }

  public getBaseApplicationList(
    filters: BaseApplicationFiltersIf,
    pageNumber?: number,
    pageSize?: number
  ): Observable<PageableModel<ApplicationBaseInfo>> {
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
      .get(EndpointUrls.offerApplicationSearchResource, options)
      .pipe(map(result => <PageableModel<ApplicationBaseInfo>>result));
  }

  public getApplicationDetailsList(
    filters: BaseApplicationFiltersIf,
    pageNumber?: number,
    pageSize?: number
  ): Observable<PageableModel<ApplicationDetails>> {
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
      .get(EndpointUrls.offerApplicationSearchDetailsResource, options)
      .pipe(map(result => <PageableModel<ApplicationDetails>>result));
  }

  public markApplicationStarred(applicationId: number): Observable<any> {
    return this.restService.patch(
      EndpointUrls.offerApplicationMarkStarred.concat(`/${applicationId}`),
      {}
    );
  }

  public markApplicationUnStarred(applicationId: number): Observable<any> {
    return this.restService.patch(
      EndpointUrls.offerApplicationMarkUnStarred.concat(`/${applicationId}`),
      {}
    );
  }
}
