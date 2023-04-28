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

export enum InstitutionRegistrationStage {
  Verification,
  ContactPersonRegistration,
}

@Injectable({ providedIn: 'root' })
export class InstitutionRegistrationGuard implements CanActivate {
  constructor(private router: Router) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const registrationStage = <InstitutionRegistrationStage>route.data['stage'];
    const canActivate = this.determineCanActivate(route, registrationStage);
    if (canActivate) {
      return of(true);
    }
    return this.router.navigate(['home']);
  }

  private determineCanActivate(
    route: ActivatedRouteSnapshot,
    stage: InstitutionRegistrationStage
  ): boolean {
    switch (stage) {
      case InstitutionRegistrationStage.Verification:
        return !isNil(route.queryParams['t']) && !isNil(route.queryParams['a']);
      case InstitutionRegistrationStage.ContactPersonRegistration:
        return !isNil(route.queryParams['t']);
      default:
        return false;
    }
  }
}
