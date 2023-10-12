import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import { Injectable } from '@angular/core';
import { map, Observable, of } from 'rxjs';
import {
  AdministrativeUserDetails,
  PatchUserDto,
  User,
  UserProfile,
} from 'src/app/core/model/user.model';
import { isNil } from 'lodash';
import { EndpointUrls } from 'src/app/utils/url.util';
import { AdminUsersManagementQueryParamsIf } from 'src/app/features/admin-users-management/_features/users-filter-pane/users-filter-pane.component';
import { PageableModel } from 'src/app/core/model/pageable.model';
import { HttpParams } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class UserService {
  private userData?: User;
  constructor(private restService: VolontarioRestService) {}

  public logout() {
    this.userData = undefined;
  }

  public getCurrentUserData(): Observable<User> {
    if (!isNil(this.userData)) {
      return of(this.userData);
    }
    return this.restService.get(EndpointUrls.userData).pipe(
      map(userResult => {
        this.userData = User.fromPayload(userResult);
        return this.userData;
      })
    );
  }

  public patchVolunteerData(
    userId: number,
    body: PatchUserDto
  ): Observable<User> {
    return this.restService
      .patch(
        EndpointUrls.volunteerResource.concat('/', userId.toString()),
        body
      )
      .pipe(
        map(updateResult => {
          this.userData = User.fromPayload(updateResult);
          return this.userData;
        })
      );
  }

  public getUserProfileDetails(userId: number): Observable<UserProfile> {
    return this.restService
      .get(EndpointUrls.userProfile.concat(`/${userId}`))
      .pipe(map(result => UserProfile.fromPayload(result)));
  }

  public getAdministrativeUserDetails(
    params?: AdminUsersManagementQueryParamsIf,
    pageIndex?: number,
    pageSize?: number
  ): Observable<PageableModel<AdministrativeUserDetails>> {
    if (isNil(pageIndex)) {
      pageIndex = 0;
    }
    if (isNil(pageSize)) {
      pageSize = 5;
    }
    const queryParams = new HttpParams({
      fromObject: { page: pageIndex, size: pageSize, ...(<any>params) },
    });
    return this.restService.get(EndpointUrls.userResource, {
      params: queryParams,
    });
  }

  public changeUserActivityStatus(
    userId: number,
    isUserDisabled: boolean
  ): Observable<void> {
    return this.restService.patch(
      EndpointUrls.changeUserActiveStatusUrl.concat(`/${userId}`),
      {
        isDeactivated: isUserDisabled,
      }
    );
  }

  public changeUserRoles(userId: number, roleIds: number[]): Observable<void> {
    const rolesDto = roleIds.map(role => {
      return { roleId: role };
    });
    return this.restService.patch(
      EndpointUrls.changeUserRolesUrl.concat(`/${userId}`),
      rolesDto
    );
  }

  public changeUserPassword(
    userId: number,
    newPassword: string
  ): Observable<void> {
    return this.restService.patch(
      EndpointUrls.changeUserPassword.concat(`/${userId}`),
      { password: newPassword }
    );
  }

  public updateVolunteerInterests(
    volunteerId: number,
    volunteerInterests: string
  ): Observable<any> {
    return this.restService.patch(
      EndpointUrls.volunteerResource.concat(`/${volunteerId}/interests`),
      { interests: volunteerInterests }
    );
  }

  public updateVolunteerExperienceDescription(
    volunteerId: number,
    volunteerExperienceDescription: string
  ): Observable<any> {
    return this.restService.patch(
      EndpointUrls.volunteerResource.concat(
        `/${volunteerId}/experience-description`
      ),
      { experienceDescription: volunteerExperienceDescription }
    );
  }
}
