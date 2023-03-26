import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthorizationInterceptor } from './core/interceptor/AuthorizationInterceptor';
import { HttpErrorInterceptor } from './core/interceptor/HttpErrorInterceptor';
import { SideBannerModule } from './shared/features/side-banner/side-banner.module';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, AppRoutingModule, SideBannerModule],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useFactory: AuthorizationInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useFactory: HttpErrorInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
