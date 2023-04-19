import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { isNil } from 'lodash';

@Injectable({ providedIn: 'root' })
export class InstitutionVerifyGuard implements CanActivate {
  constructor(private router: Router) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const canActivate =
      !isNil(route.queryParams['t']) && !isNil(route.queryParams['a']);
    if (canActivate) {
      return of(true);
    }
    return this.router.navigate(['home']);
  }
}
