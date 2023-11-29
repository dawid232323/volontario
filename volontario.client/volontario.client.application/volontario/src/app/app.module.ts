import { ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthorizationInterceptor } from 'src/app/core/interceptor/Authorization.interceptor';
import { HttpErrorInterceptor } from 'src/app/core/interceptor/HttpError.interceptor';
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
import { ManageDictValuesModule } from 'src/app/features/manage-dict-values/manage-dict-values.module';
import { InstitutionEditModule } from 'src/app/features/institution-edit/institution-edit.module';
import { ErrorDialogModule } from 'src/app/shared/features/error-dialog/error-dialog.module';
import { VolontarioGlobalErrorHandlerInterceptor } from 'src/app/core/interceptor/VolontarioGlobalErrorHandler.interceptor';
import { ManageInstitutionWorkersModule } from 'src/app/features/manage-institution-workers/manage-institution-workers.module';
import { DragAndDropModule } from './shared/features/drag-and-drop/drag-and-drop.module';
import { FileUploaderModule } from './shared/features/file-uplodaer/file-uploader.module';
import { ReportIssueModule } from './features/report-issue/report-issue.module';
import { UserDetailsModule } from './features/user-details/user-details.module';
import { UserEditDataModule } from './features/user-edit-data/user-edit-data.module';
import { VolunteerRegistrationConfirmationModule } from './features/volunteer-registration-confirmation/volunteer-registration-confirmation.module';
import { InstitutionOfferPresenceModule } from 'src/app/features/institution-offer-presence/institution-offer-presence.module';
import { DirectiveModule } from 'src/app/core/directive/directive.module';

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
    ManageDictValuesModule,
    InstitutionEditModule,
    ManageInstitutionWorkersModule,
    ErrorDialogModule,
    UserDetailsModule,
    ReportIssueModule,
    FileUploaderModule,
    DragAndDropModule,
    UserEditDataModule,
    VolunteerRegistrationConfirmationModule,
    InstitutionOfferPresenceModule,
    DirectiveModule,
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
    {
      provide: ErrorHandler,
      useClass: VolontarioGlobalErrorHandlerInterceptor,
    },
    { provide: MAT_DATE_LOCALE, useValue: 'pl-PL' },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
