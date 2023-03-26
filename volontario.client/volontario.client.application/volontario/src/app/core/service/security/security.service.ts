import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TokenService } from './token.service';
import { map, Observable } from 'rxjs';
import { VolunteerRegisterDTO } from '../../model/volunteer.model';
import {
  LoginInterface,
  RefreshTokenInterface,
  TokenPairInterface,
} from '../../interface/authorization.interface';
import { VolontarioRestService } from '../volontarioRest.service';

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

  // TODO implement logic when backend service is ready
  public registerVolunteer(
    volunteerRegisterDto: VolunteerRegisterDTO
  ): Observable<any> {
    return new Observable<any>();
  }

  // TODO implement logic when backend service is ready
  public login(loginDto: LoginInterface): Observable<void> {
    const url = this.endpointBaseUrl + '/login';
    return this.httpClient.post(url, loginDto).pipe(
      map(result => {
        const loginResult = <TokenPairInterface>result;
        this.tokenService.saveToken(loginResult.token);
        this.tokenService.saveRefreshToken(loginResult.refreshToken);
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
      refreshToken: this.tokenService.getRefreshToken(),
    };
    return this.volRestService.post('/refreshToken', refreshTokenObj);
  }

  public isUserLoggedIn(): boolean {
    return this.isLoggedIn;
  }
}
