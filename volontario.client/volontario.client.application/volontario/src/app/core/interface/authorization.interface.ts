export interface LoginInterface {
  login: string;
  password: string;
}

export interface TokenPairInterface {
  token: string;
  refreshToken: string;
}

export interface RefreshTokenInterface {
  refreshToken: string | null;
}
