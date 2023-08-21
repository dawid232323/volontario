import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import { map, Observable, of } from 'rxjs';
import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
import { HttpHeaders } from '@angular/common/http';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';
import { EndpointUrls } from 'src/app/utils/url.util';
import { DictionaryValuesServiceInterface } from 'src/app/core/interface/dictionary-values-service.interface';
import { AbstractDictionaryValueService } from 'src/app/core/service/abstract/abstract-dictionary-value.service';

@Injectable({ providedIn: 'root' })
export class VolunteerExperienceService extends AbstractDictionaryValueService<VolunteerExperience> {
  public constructor(restService: VolontarioRestService) {
    super(restService);
  }

  public getUsedValues(): Observable<VolunteerExperience[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options: HttpOptionsInterface = {
      headers: headers,
    };
    return this.restService
      .get(EndpointUrls.experienceLevels, options)
      .pipe(map(result => result.map(VolunteerExperience.fromPayload)));
  }

  public override getNotUsedValues(): Observable<VolunteerExperience[]> {
    return this.restService
      .get(EndpointUrls.notUsedExpLevels)
      .pipe(map(result => result.map(VolunteerExperience.fromPayload)));
  }

  protected deactivateValue(valueId: number): Observable<void> {
    return this.restService.delete(
      EndpointUrls.expLevelsSoftDelete.concat(`/${valueId}`)
    );
  }

  protected activateValue(valueId: number): Observable<void> {
    return this.restService.post(
      EndpointUrls.epxLevelRevertDelete.concat(`/${valueId}`),
      {}
    );
  }

  public override createValue(
    body: VolunteerExperience
  ): Observable<VolunteerExperience> {
    return super
      .createValue(body)
      .pipe(map(result => VolunteerExperience.fromPayload(result)));
  }

  public override updateValue(
    body: VolunteerExperience,
    valueId: number
  ): Observable<any> {
    return super
      .updateValue(body, valueId)
      .pipe(map(result => VolunteerExperience.fromPayload(result)));
  }

  protected override getCreateUpdateUrl(valueId: number): string {
    return EndpointUrls.experienceLevelResource;
  }
}
