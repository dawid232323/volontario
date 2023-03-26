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

@Injectable({ providedIn: 'root' })
export class HttpErrorInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private isUserLoggedIn = false;

  constructor(
    private tokenService: TokenService,
    private authService: SecurityService
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
          return next.handle(req);
        }),
        catchError(error => {
          this.isRefreshing = false;
          return throwError(error);
        })
      );
    }
    return next.handle(req);
  }
}
