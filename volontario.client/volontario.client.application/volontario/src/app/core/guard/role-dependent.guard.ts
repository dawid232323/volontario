import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { map, Observable } from 'rxjs';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import { UserService } from 'src/app/core/service/user.service';

/**
 * Router guard that checks current route against user roles
 *
 * In app.routing.module routes required roles should be declared as list of roles that can access given route
 *
 * Admin and moderator can access all routes guarded by this guard
 */
@Injectable({ providedIn: 'root' })
export class RoleDependentGuard implements CanActivate {
  constructor(private userService: UserService) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const requiredRoles = <UserRoleEnum[]>route.data['roles'];
    return this.userService.getCurrentUserData().pipe(
      map(user => {
        if (
          user.hasUserRole(
            UserRoleEnum.Admin || user.hasUserRole(UserRoleEnum.Moderator)
          )
        ) {
          return true;
        }
        return user.hasUserRoles(requiredRoles);
      })
    );
  }
}
