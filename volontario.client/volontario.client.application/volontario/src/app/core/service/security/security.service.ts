import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenService } from './token.service';
import { map, Observable, Subject } from 'rxjs';
import { VolunteerRegisterDTO } from '../../model/volunteer.model';
import {
  LoginInterface,
  RefreshTokenInterface,
  TokenPairInterface,
} from '../../interface/authorization.interface';
import { VolontarioRestService } from '../volontarioRest.service';
import { isNil } from 'lodash';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';
import { EndpointUrls } from 'src/app/utils/url.util';
import { UserService } from 'src/app/core/service/user.service';

@Injectable({ providedIn: 'root' })
export class SecurityService {
  private readonly endpointBaseUrl: string;

  private isLoggedIn = false;

  public loginEvent = new Subject<void>();

  private _logoutEvent = new Subject<void>();

  constructor(
    private tokenService: TokenService,
    private volRestService: VolontarioRestService,
    private userService: UserService
  ) {
    this.endpointBaseUrl = this.volRestService.getEnvironmentUrl();
  }

  public registerVolunteer(
    volunteerRegisterDto: VolunteerRegisterDTO
  ): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options: HttpOptionsInterface = {
      headers: headers,
    };
    return this.volRestService.post(
      EndpointUrls.registerVolunteer,
      volunteerRegisterDto,
      options
    );
  }

  public login(loginDto: LoginInterface): Observable<void> {
    return this.volRestService.post(EndpointUrls.login, loginDto).pipe(
      map(result => {
        const loginResult = <TokenPairInterface>result;
        this.tokenService.saveToken(loginResult.token);
        this.tokenService.saveRefreshToken(loginResult.refresh_token);
        this.loginEvent.next();
        this.isLoggedIn = true;
      })
    );
  }

  public logout() {
    this._logoutEvent.next();
    this.tokenService.logout();
    this.userService.logout();
    this.isLoggedIn = false;
  }

  public refreshToken(): Observable<any> {
    const refreshTokenObj: RefreshTokenInterface = {
      refresh_token: this.tokenService.getRefreshToken(),
    };
    return this.volRestService
      .post(EndpointUrls.refreshToken, refreshTokenObj)
      .pipe(
        map(response => {
          const refreshTokenResult = <TokenPairInterface>response;
          this.tokenService.saveToken(refreshTokenResult.token);
          this.tokenService.saveRefreshToken(refreshTokenResult.refresh_token);
        })
      );
  }

  public isUserLoggedIn(): boolean {
    return (
      !isNil(this.tokenService.getToken()) &&
      !isNil(this.tokenService.getRefreshToken())
    );
  }

  public get logoutEvent(): Observable<void> {
    return this._logoutEvent.asObservable();
  }
}
