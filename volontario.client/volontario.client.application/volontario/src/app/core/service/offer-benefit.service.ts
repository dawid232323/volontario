import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import { DictionaryValuesServiceInterface } from 'src/app/core/interface/dictionary-values-service.interface';
import { map, Observable, of } from 'rxjs';
import { AdvertisementBenefit } from 'src/app/core/model/advertisement.model';
import { EndpointUrls } from 'src/app/utils/url.util';
import { AbstractDictionaryValueService } from 'src/app/core/service/abstract/abstract-dictionary-value.service';

@Injectable({ providedIn: 'root' })
export class OfferBenefitService extends AbstractDictionaryValueService<AdvertisementBenefit> {
  public constructor(restService: VolontarioRestService) {
    super(restService);
  }

  public getUsedValues(): Observable<AdvertisementBenefit[]> {
    return this.restService
      .get(EndpointUrls.advertisementBenefits)
      .pipe(map(result => result.map(AdvertisementBenefit.fromPayload)));
  }

  public override getNotUsedValues(): Observable<AdvertisementBenefit[]> {
    return this.restService
      .get(EndpointUrls.notUsedBenefits)
      .pipe(map(result => result.map(AdvertisementBenefit.fromPayload)));
  }

  public override createValue(body: any): Observable<AdvertisementBenefit> {
    return super
      .createValue(body)
      .pipe(map(result => AdvertisementBenefit.fromPayload(result)));
  }

  public override updateValue(body: any, valueId: number): Observable<any> {
    return super
      .updateValue(body, valueId)
      .pipe(map(result => AdvertisementBenefit.fromPayload(result)));
  }

  protected deactivateValue(valueId: number): Observable<void> {
    return this.restService.delete(
      EndpointUrls.benefitSoftDelete.concat(`/${valueId}`)
    );
  }

  protected activateValue(valueId: number): Observable<void> {
    return this.restService.post(
      EndpointUrls.benefitRevertDelete.concat(`/${valueId}`),
      {}
    );
  }

  protected override getCreateUpdateUrl(valueId: number): string {
    return EndpointUrls.benefitResource;
  }
}
