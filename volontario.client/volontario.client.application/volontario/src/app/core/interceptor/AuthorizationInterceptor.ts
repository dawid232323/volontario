import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { TokenService } from '../service/security/token.service';
import { SecurityService } from '../service/security/security.service';
import { VolontarioRestService } from '../service/volontarioRest.service';

@Injectable({ providedIn: 'root' })
export class AuthorizationInterceptor implements HttpInterceptor {
  constructor(
    private tokenService: TokenService,
    private authService: SecurityService,
    private backendService: VolontarioRestService
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const isLoggedIn = this.authService.isUserLoggedIn();
    const authHeader = `Bearer ${this.tokenService.getToken()}`;
    if (
      isLoggedIn &&
      this.isEndpointAuthenticated(req.url) &&
      this.isVolontarioApiUrl(req.url)
    ) {
      req = req.clone({
        setHeaders: {
          Authorization: authHeader,
          'Content-Type': 'application/json',
        },
      });
    }
    return next.handle(req);
  }

  private isEndpointAuthenticated(url: string): boolean {
    return !url.includes('/login') || !url.includes('/register');
  }

  private isVolontarioApiUrl(url: string): boolean {
    const apiUrl = this.backendService.getEnvironmentUrl();
    return url.includes(apiUrl);
  }
}
