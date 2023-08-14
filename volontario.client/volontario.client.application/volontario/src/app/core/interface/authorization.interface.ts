export interface LoginInterface {
  login: string;
  password: string;
}

export interface TokenPairInterface {
  token: string;
  refresh_token: string;
}

export interface RefreshTokenInterface {
  refresh_token: string | null;
}

export interface SetPasswordInterface {
  password: string;
}
