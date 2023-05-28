import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import {
  OfferApplicationModel,
  OfferApplicationModelDto,
} from 'src/app/core/model/offerApplication.model';
import { EndpointUrls } from 'src/app/utils/url.util';
import { map, Observable } from 'rxjs';

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
}
