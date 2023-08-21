import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import { forkJoin, map, Observable, of } from 'rxjs';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';
import { HttpHeaders } from '@angular/common/http';
import { EndpointUrls } from 'src/app/utils/url.util';
import { DictionaryValuesServiceInterface } from 'src/app/core/interface/dictionary-values-service.interface';
import { AbstractDictionaryValueService } from 'src/app/core/service/abstract/abstract-dictionary-value.service';
import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';

@Injectable({ providedIn: 'root' })
export class InterestCategoryService extends AbstractDictionaryValueService<DictionaryValueInterface> {
  public constructor(restService: VolontarioRestService) {
    super(restService);
  }

  public getUsedValues(): Observable<InterestCategoryDTO[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options: HttpOptionsInterface = {
      headers: headers,
    };
    return this.restService
      .get(EndpointUrls.interestCategories, options)
      .pipe(map(result => result.map(InterestCategoryDTO.fromPayload)));
  }

  public getNotUsedValues(): Observable<InterestCategoryDTO[]> {
    return this.restService
      .get(EndpointUrls.notUsedInterestCategories)
      .pipe(map(result => result.map(InterestCategoryDTO.fromPayload)));
  }

  public override createValue(
    body: InterestCategoryDTO
  ): Observable<InterestCategoryDTO> {
    return super
      .createValue(body)
      .pipe(map(result => InterestCategoryDTO.fromPayload(result)));
  }

  public override updateValue(
    body: InterestCategoryDTO,
    valueId: number
  ): Observable<any> {
    return super
      .updateValue(body, valueId)
      .pipe(map(result => InterestCategoryDTO.fromPayload(result)));
  }

  protected deactivateValue(valueId: number): Observable<void> {
    return this.restService.delete(
      EndpointUrls.interestCategorySoftDelete.concat(`/${valueId}`)
    );
  }

  protected activateValue(valueId: number): Observable<void> {
    return this.restService.post(
      EndpointUrls.interestCategoryRevertDelete.concat(`/${valueId}`),
      {}
    );
  }

  protected override getCreateUpdateUrl(valueId: number): string {
    return EndpointUrls.interestCategoryResource;
  }
}
