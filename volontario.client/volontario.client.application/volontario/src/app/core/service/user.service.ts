import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import { Injectable } from '@angular/core';
import { map, Observable, of } from 'rxjs';
import { PatchUserDto, User } from 'src/app/core/model/user.model';
import { isNil } from 'lodash';
import { EndpointUrls } from 'src/app/utils/url.util';

@Injectable({ providedIn: 'root' })
export class UserService {
  private userData?: User;
  constructor(private restService: VolontarioRestService) {}

  public getCurrentUserData(): Observable<User> {
    if (!isNil(this.userData)) {
      return of(this.userData);
    }
    return this.restService.get(EndpointUrls.userData).pipe(
      map(userResult => {
        return User.fromPayload(userResult);
      })
    );
  }

  public patchVolunteerData(
    userId: number,
    body: PatchUserDto
  ): Observable<any> {
    return this.restService.patch(
      EndpointUrls.volunteerResource.concat('/', userId.toString()),
      body
    );
  }
}
