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
import { isNil } from 'lodash';

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
    } else if (this.shouldBeSentWithNoContentWithAuthorization(req.url)) {
      req = req.clone({
        setHeaders: { Authorization: authHeader },
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
            'Content-Type': isNil(req.headers.get('Content-Type'))
              ? 'application/json'
              : <string>req.headers.get('Content-Type'),
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
    const isAuthorizedStaticUrl: boolean =
      !EndpointUrls.unauthorizedUrls.has(url);
    let isAuthorizedParametrizedUrl: boolean = true;

    for (let urlRegex of EndpointUrls.unauthorizedParametrizedUrlPatterns) {
      if (urlRegex.test(url)) {
        isAuthorizedParametrizedUrl = false;
        break;
      }
    }

    return isAuthorizedStaticUrl && isAuthorizedParametrizedUrl;
  }

  private isVolontarioApiUrl(url: string): boolean {
    const apiUrl = this.backendService.getEnvironmentUrl();
    return url.includes(apiUrl);
  }

  private shouldBeSentWithNoHeaders(url: string): boolean {
    return Array.from(EndpointUrls.resourcesRequiringNoContentType).some(
      resource => url.endsWith(resource)
    );
  }

  private shouldBeSentWithNoContentWithAuthorization(url: string): boolean {
    return Array.from(
      EndpointUrls.resourcesRequiringNoContentWithAuthorization
    ).some(resource => url.endsWith(resource));
  }
}
