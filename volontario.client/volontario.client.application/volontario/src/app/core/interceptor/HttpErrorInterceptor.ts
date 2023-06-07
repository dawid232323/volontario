import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import {
  catchError,
  Observable,
  Subject,
  switchMap,
  take,
  throwError,
} from 'rxjs';
import { Injectable } from '@angular/core';
import { TokenService } from '../service/security/token.service';
import { SecurityService } from '../service/security/security.service';
import { ActivatedRoute, Router } from '@angular/router';
import { EndpointUrls } from 'src/app/utils/url.util';

@Injectable({ providedIn: 'root' })
export class HttpErrorInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private tokenHasBeenRefreshed = new Subject<void>();

  constructor(
    private tokenService: TokenService,
    private authService: SecurityService,
    private router: Router
  ) {}
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError(requestError => {
        if (
          requestError instanceof HttpErrorResponse &&
          !req.url.includes('/login') &&
          requestError.status === 401
        ) {
          return this.handleUnauthorized(req, next);
        }
        return throwError(() => requestError);
      })
    );
  }

  private handleUnauthorized(req: HttpRequest<any>, next: HttpHandler) {
    // when current request is the one to refresh access token
    if (this.isRefreshing && req.url.includes(EndpointUrls.refreshToken)) {
      return next.handle(req);
    }
    // when it is the first request that fails
    if (this.authService.isUserLoggedIn() && !this.isRefreshing) {
      this.isRefreshing = true;
      return this.authService.refreshToken().pipe(
        switchMap(() => {
          this.isRefreshing = false;
          this.tokenHasBeenRefreshed.next();
          // old request doesn't fall into AuthorizationInterceptor once again so new token needs to be added manually
          return this.getRefreshedRequest(req, next);
        }),
        catchError(error => {
          this.isRefreshing = false;
          this.authService.logout();
          this.router.navigate(['/login'], {
            queryParams: { next: this.router.url },
          });
          return throwError(error);
        })
      );
    }
    // when there are multiple request at one time (e.g. via forkJoin) and token is already refreshing
    return this.tokenHasBeenRefreshed.pipe(
      take(1),
      switchMap(() => {
        return this.getRefreshedRequest(req, next);
      })
    );
  }

  private getRefreshedRequest(req: HttpRequest<any>, next: HttpHandler) {
    const refreshedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${this.tokenService.getToken()}`,
      },
    });
    return next.handle(refreshedRequest);
  }
}
