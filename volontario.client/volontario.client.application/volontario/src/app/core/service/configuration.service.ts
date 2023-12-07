import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import { LandingPageDto } from 'src/app/core/model/configuration.model';
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

  public saveNewLandingPage(
    landingPageData: LandingPageDto
  ): Observable<LandingPageDto> {
    return this.restService.post('/configuration/landingPage', landingPageData);
  }

  get landingPagePreview(): LandingPageDto | undefined {
    return this._landingPagePreview;
  }

  set landingPagePreview(value: LandingPageDto | undefined) {
    this._landingPagePreview = value;
  }
}
