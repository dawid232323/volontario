import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthorizationInterceptor } from './core/interceptor/AuthorizationInterceptor';
import { HttpErrorInterceptor } from './core/interceptor/HttpErrorInterceptor';
import { SideBannerModule } from './shared/features/side-banner/side-banner.module';
import { LoginModule } from './features/login/login.module';
import { HomePageModule } from './features/home-page/home-page.module';
import { RegisterModule } from './features/register/register.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegisterInstitutionModule } from 'src/app/features/register-institution/register-institution.module';
import { InstitutionVerifyModule } from 'src/app/features/institution-verify/institution-verify.module';
import { MainPageModule } from './features/main-page/main-page.module';
import { RegisterContactPersonModule } from 'src/app/features/register-contact-person/register-contact-person.module';
import { AddEditAdvertisementModule } from 'src/app/features/add-edit-advertisement/add-edit-advertisement.module';
import { MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';
import { InstitutionAdvertisementPanelModule } from './features/institution-advertisement-panel/institution-advertisement-panel.module';
import { FooterModule } from './shared/features/footer/footer.module';
import { NavModule } from './shared/features/nav/nav.module';
import { OfferListModule } from 'src/app/features/offer-list/offer-list.module';
import { MatDialogModule } from '@angular/material/dialog';
import { AdvertisementDetailsModule } from './features/advertisement-details/advertisement-details.module';
import { OfferApplyModule } from 'src/app/features/offer-apply/offer-apply.module';
import { VolunteerApplicationsListModule } from './features/volunteer-applications-list/volunteer-applications-list.module';
import { AdminUsersManagementModule } from './features/admin-users-management/admin-users-management.module';
import { InstitutionDetailsModule } from 'src/app/features/institution-details/institution-details.module';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    SideBannerModule,
    LoginModule,
    RegisterModule,
    HomePageModule,
    BrowserAnimationsModule,
    RegisterInstitutionModule,
    InstitutionVerifyModule,
    MainPageModule,
    RegisterContactPersonModule,
    AddEditAdvertisementModule,
    MatNativeDateModule,
    InstitutionAdvertisementPanelModule,
    FooterModule,
    NavModule,
    OfferListModule,
    MatDialogModule,
    AdvertisementDetailsModule,
    OfferApplyModule,
    VolunteerApplicationsListModule,
    AdminUsersManagementModule,
    InstitutionDetailsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthorizationInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true,
    },
    { provide: MAT_DATE_LOCALE, useValue: 'pl-PL' },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
