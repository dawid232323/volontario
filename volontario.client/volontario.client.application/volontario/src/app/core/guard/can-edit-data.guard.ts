import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { map, Observable } from 'rxjs';
import { UserService } from '../service/user.service';
import { UserRoleEnum } from '../model/user-role.model';
import { isNil } from 'lodash';

@Injectable({
  providedIn: 'root',
})
export class CanEditDataGuard implements CanActivate {
  constructor(private userService: UserService) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return this.userService.getCurrentUserData().pipe(
      map(user => {
        if (
          user.hasUserRole(
            UserRoleEnum.Admin || user.hasUserRole(UserRoleEnum.Moderator)
          )
        ) {
          return true;
        }

        let userIdFromRoute: number;
        const optionalUserIdFromRoute = route.paramMap.get('user_id');
        if (
          !isNil(optionalUserIdFromRoute) &&
          !isNaN(+optionalUserIdFromRoute)
        ) {
          userIdFromRoute = +optionalUserIdFromRoute;
        } else {
          throw false;
        }

        return user.id === userIdFromRoute;
      })
    );
  }
}
