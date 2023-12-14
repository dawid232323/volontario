import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { map, Observable, throwError } from 'rxjs';
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
  constructor(private userService: UserService, private router: Router) {}
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
        if (user.hasUserRoles([UserRoleEnum.Moderator, UserRoleEnum.Admin])) {
          return true;
        }
        if (!user.hasUserRoles(requiredRoles)) {
          this.router.navigate(['home']);
        }
        return user.hasUserRoles(requiredRoles);
      })
    );
  }
}
