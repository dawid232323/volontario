import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenService } from './token.service';
import { map, Observable } from 'rxjs';
import { VolunteerRegisterDTO } from '../../model/volunteer.model';
import {
  LoginInterface,
  RefreshTokenInterface,
  TokenPairInterface,
} from '../../interface/authorization.interface';
import { VolontarioRestService } from '../volontarioRest.service';
import { isNil } from 'lodash';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';

@Injectable({ providedIn: 'root' })
export class SecurityService {
  private readonly endpointBaseUrl: string;

  private isLoggedIn = false;

  constructor(
    private httpClient: HttpClient,
    private tokenService: TokenService,
    private volRestService: VolontarioRestService
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
      '/volunteer/register',
      volunteerRegisterDto,
      options
    );
  }

  public login(loginDto: LoginInterface): Observable<void> {
    return this.volRestService.post('/login', loginDto).pipe(
      map(result => {
        const loginResult = <TokenPairInterface>result;
        this.tokenService.saveToken(loginResult.token);
        this.tokenService.saveRefreshToken(loginResult.refresh_token);
        this.isLoggedIn = true;
      })
    );
  }

  public logout() {
    this.tokenService.logout();
    this.isLoggedIn = false;
  }

  public refreshToken(): Observable<any> {
    const refreshTokenObj: RefreshTokenInterface = {
      refresh_token: this.tokenService.getRefreshToken(),
    };
    return this.volRestService.post('/refresh/token', refreshTokenObj).pipe(
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
}
