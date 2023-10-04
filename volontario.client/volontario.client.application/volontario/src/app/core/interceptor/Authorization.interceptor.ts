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
import { EndpointUrls } from 'src/app/utils/url.util';

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

    if (this.shouldBeSentWithNoHeaders(req.url)) {
      req = req.clone({
        setHeaders: {},
      });
    } else {
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
      } else if (!this.isEndpointAuthenticated(req.url)) {
        req = req.clone({
          setHeaders: {
            'Content-Type': 'application/json',
          },
        });
      }
    }

    return next.handle(req);
  }

  private isEndpointAuthenticated(url: string): boolean {
    return !EndpointUrls.unauthorizedUrls.has(url);
  }

  private isVolontarioApiUrl(url: string): boolean {
    const apiUrl = this.backendService.getEnvironmentUrl();
    return url.includes(apiUrl);
  }

  private shouldBeSentWithNoHeaders(url: string): boolean {
    for (let resource of EndpointUrls.resourcesRequiringNoContentType) {
      if (url.endsWith(resource)) {
        return true;
      }
    }
    return false;
  }
}
