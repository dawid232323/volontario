import {Injectable} from "@angular/core";


@Injectable({providedIn: 'root'})
export class TokenService {

  private readonly TOKEN_KEY = 'AUTH_TOKEN';
  private readonly REFRESH_TOKEN_KEY = 'AUTH_REFRESH_TOKEN';

  constructor() {
  }

  public saveToken(newToken: string) {
    window.sessionStorage.removeItem(this.TOKEN_KEY);
    window.sessionStorage.setItem(this.TOKEN_KEY, newToken);
  }

  public saveRefreshToken(newRefreshToken: string) {
    window.sessionStorage.removeItem(this.REFRESH_TOKEN_KEY);
    window.sessionStorage.setItem(this.REFRESH_TOKEN_KEY, newRefreshToken);
  }

  public getToken(): string | null {
    return window.sessionStorage.getItem(this.TOKEN_KEY);
  }

  public getRefreshToken(): string | null {
    return window.sessionStorage.getItem(this.REFRESH_TOKEN_KEY);
  }
}
