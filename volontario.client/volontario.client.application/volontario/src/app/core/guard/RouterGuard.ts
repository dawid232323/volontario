import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { SecurityService } from '../service/security/security.service';

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
    return this.router.navigate(['/login']);
  }
}
