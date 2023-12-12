import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import {
  LandingPageDto,
  Regulations,
} from 'src/app/core/model/configuration.model';
import { Observable } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ConfigurationService {
  private _landingPagePreview?: LandingPageDto;

  constructor(private restService: VolontarioRestService) {}

  public getLandingPageData(): Observable<LandingPageDto> {
    return this.restService.get('/configuration/landingPage', {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  public getRegulationsData(): Observable<Regulations> {
    return this.restService.get('/configuration/regulations', {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  public saveNewLandingPage(
    landingPageData: LandingPageDto
  ): Observable<LandingPageDto> {
    return this.restService.post('/configuration/landingPage', landingPageData);
  }

  public saveRegulations(regulations: Regulations): Observable<void> {
    return this.restService.post('/configuration/regulations', regulations);
  }

  get landingPagePreview(): LandingPageDto | undefined {
    return this._landingPagePreview;
  }

  set landingPagePreview(value: LandingPageDto | undefined) {
    this._landingPagePreview = value;
  }
}
