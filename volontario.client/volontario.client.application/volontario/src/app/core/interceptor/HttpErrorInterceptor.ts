import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { Injectable } from '@angular/core';
import { TokenService } from '../service/security/token.service';
import { SecurityService } from '../service/security/security.service';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class HttpErrorInterceptor implements HttpInterceptor {
  private isRefreshing = false;

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
    if (this.isRefreshing) {
      return next.handle(req);
    }
    this.isRefreshing = true;
    if (this.authService.isUserLoggedIn()) {
      return this.authService.refreshToken().pipe(
        switchMap(() => {
          this.isRefreshing = false;
          // old request doesn't fall into AuthorizationInterceptor once again so new token needs to be added manually
          const refreshedRequest = req.clone({
            setHeaders: {
              Authorization: `Bearer ${this.tokenService.getToken()}`,
            },
          });
          return next.handle(refreshedRequest);
        }),
        catchError(error => {
          this.isRefreshing = false;
          this.authService.logout();
          this.router.navigate(['/login']);
          return throwError(error);
        })
      );
    }
    return next.handle(req);
  }
}
