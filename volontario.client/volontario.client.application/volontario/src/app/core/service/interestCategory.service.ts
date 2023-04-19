import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import { map, Observable } from 'rxjs';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';
import { HttpHeaders } from '@angular/common/http';
import { EndpointUrls } from 'src/app/utils/url.util';

@Injectable({ providedIn: 'root' })
export class InterestCategoryService {
  constructor(private restService: VolontarioRestService) {}

  public getAllInterestCategories(): Observable<InterestCategoryDTO[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options: HttpOptionsInterface = {
      headers: headers,
    };
    return this.restService
      .get(EndpointUrls.interestCategories, options)
      .pipe(map(result => result.map(InterestCategoryDTO.fromPayload)));
  }
}
