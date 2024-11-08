import { Injectable } from '@angular/core';
import { TokenService } from './security/token.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { isNil } from 'lodash';
import { HttpOptionsInterface } from '../interface/httpOptions.interface';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class VolontarioRestService {
  constructor(
    private tokenService: TokenService,
    private httpClient: HttpClient
  ) {}

  private getHttpOptionsWithAuthorization(
    options?: HttpOptionsInterface
  ): HttpOptionsInterface {
    const token = this.tokenService.getToken();
    if (isNil(options)) {
      return {
        headers: new HttpHeaders({ Authorization: `Bearer ${token}` }),
      };
    }
    const { headers } = options;
    if (isNil(headers)) {
      return {
        ...options,
        headers: new HttpHeaders({ Authorization: `Bearer ${token}` }),
      };
    }
    headers.set('Authorization', `Bearer ${token}`);
    return { ...options, headers: headers };
  }

  public get(
    endpoint: string,
    options?: HttpOptionsInterface
  ): Observable<any> {
    return this.httpClient.get(this.getEnvironmentUrl() + endpoint, options);
  }

  public post(
    endpoint: string,
    body: any,
    options?: HttpOptionsInterface
  ): Observable<any> {
    return this.httpClient.post(
      this.getEnvironmentUrl() + endpoint,
      body,
      options
    );
  }

  public put(
    endpoint: string,
    body: any,
    options?: HttpOptionsInterface
  ): Observable<any> {
    return this.httpClient.put(
      this.getEnvironmentUrl() + endpoint,
      body,
      options
    );
  }

  public patch(
    endpoint: string,
    body: any,
    options?: HttpOptionsInterface
  ): Observable<any> {
    return this.httpClient.patch(
      this.getEnvironmentUrl() + endpoint,
      body,
      options
    );
  }

  public delete(
    endpoint: string,
    options?: HttpOptionsInterface
  ): Observable<any> {
    return this.httpClient.delete(this.getEnvironmentUrl() + endpoint, options);
  }

  public getEnvironmentUrl(): string {
    return environment.apiUrl;
  }
}
