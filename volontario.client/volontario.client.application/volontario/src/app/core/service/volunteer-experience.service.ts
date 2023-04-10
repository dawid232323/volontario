import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import { map, Observable } from 'rxjs';
import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
import { HttpHeaders } from '@angular/common/http';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';

@Injectable({ providedIn: 'root' })
export class VolunteerExperienceService {
  constructor(private restService: VolontarioRestService) {}

  public getAllExperienceLevels(): Observable<VolunteerExperience[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options: HttpOptionsInterface = {
      headers: headers,
    };
    return this.restService
      .get('/experienceLevels', options)
      .pipe(map(result => result.map(VolunteerExperience.fromPayload)));
  }
}
