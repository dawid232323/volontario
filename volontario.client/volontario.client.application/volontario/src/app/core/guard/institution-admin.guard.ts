import { ActivatedRoute, ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { UserService } from 'src/app/core/service/user.service';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import { isNil } from 'lodash';

@Injectable({ providedIn: 'root' })
export class InstitutionAdminGuard implements CanActivate {
  constructor(private router: Router, private userService: UserService, private activatedRoute: ActivatedRoute) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const institutionId = +route.params['institution_id'];
    if (isNil(institutionId) || isNaN(institutionId)) {
      return this.router.navigate(['home']);
    }
    return this.userService.getCurrentUserData().pipe(
      map(loggedUser => {
        if (loggedUser.hasUserRoles([UserRoleEnum.Admin, UserRoleEnum.Moderator])) {
          return true;
        }
        if (loggedUser.hasUserRole(UserRoleEnum.InstitutionAdmin) && loggedUser.institution?.id === institutionId) {
          return true;
        }
        return false;
      })
    );
  }
}
