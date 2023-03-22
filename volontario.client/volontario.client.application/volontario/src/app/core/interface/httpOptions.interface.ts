import {HttpContext, HttpHeaders, HttpParams} from "@angular/common/http";


export interface HttpOptionsInterface {
  headers?: HttpHeaders;
  context?: HttpContext;
  observe?: 'body';
  params?: HttpParams;
  reportProgress?: boolean;
  responseType?: 'json';
  withCredentials?: boolean;

}
