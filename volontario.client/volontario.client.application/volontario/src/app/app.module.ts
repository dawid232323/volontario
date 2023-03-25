import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SideBannerModule } from './shared/features/side-banner/side-banner.module';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, AppRoutingModule, SideBannerModule ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
