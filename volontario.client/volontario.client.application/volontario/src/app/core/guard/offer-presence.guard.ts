import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { catchError, map, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { AdvertisementService } from 'src/app/core/service/advertisement.service';
import { isNil } from 'lodash';

@Injectable({ providedIn: 'root' })
export class OfferPresenceGuard implements CanActivate {
  constructor(
    private offerService: AdvertisementService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const offerId = +route.queryParams['o'];
    if (isNaN(offerId) || isNil(offerId)) {
      return this.router.navigate(['/']);
    }
    return this.offerService.isOfferReadyToConfirmPresence(offerId).pipe(
      map(result => result.isOfferReadyToConfirmPresences),
      catchError(error => {
        return this.router.navigate(['advertisement', offerId]);
      })
    );
  }
}
