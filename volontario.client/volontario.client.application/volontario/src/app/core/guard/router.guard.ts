import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlSerializer,
  UrlTree,
} from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { SecurityService } from '../service/security/security.service';
import { isNil } from 'lodash';

@Injectable({ providedIn: 'root' })
export class RouterGuard implements CanActivate {
  constructor(private authService: SecurityService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    if (this.authService.isUserLoggedIn()) {
      return of(true);
    }
    const nextUrlTree = this.router.createUrlTree(
      route.pathFromRoot
        .filter(route => !isNil(route.url[0]?.path))
        .map(route => route.url[0]?.path),
      { queryParams: route.queryParams }
    );
    const nextUrl = this.router.serializeUrl(nextUrlTree);
    return this.router.navigate(['/login'], { queryParams: { next: nextUrl } });
  }
}
