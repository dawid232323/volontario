import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { HomePageComponent } from './features/home-page/home-page.component';
import { RouterGuard } from 'src/app/core/guard/router.guard';
import { RegisterComponent } from './features/register/register.component';
import { RegisterInstitutionComponent } from 'src/app/features/register-institution/register-institution.component';
import { InstitutionVerifyComponent } from 'src/app/features/institution-verify/institution-verify.component';
import {
  InstitutionRegistrationGuard,
  InstitutionRegistrationStage,
} from 'src/app/core/guard/institution-registration.guard';
import { RegisterContactPersonComponent } from 'src/app/features/register-contact-person/register-contact-person.component';
import { MainPageComponent } from './features/main-page/main-page.component';
import {
  AddEditAdvertisementComponent,
  AdvertisementCrudOperationType,
} from 'src/app/features/add-edit-advertisement/add-edit-advertisement.component';
import { RoleDependentGuard } from 'src/app/core/guard/role-dependent.guard';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import { InstitutionAdvertisementPanelComponent } from 'src/app/features/institution-advertisement-panel/institution-advertisement-panel.component';
import { OfferListComponent } from 'src/app/features/offer-list/offer-list.component';
import { AdvertisementDetailsComponent } from './features/advertisement-details/advertisement-details.component';
import { OfferApplyComponent } from 'src/app/features/offer-apply/offer-apply.component';
import { VolunteerApplicationsListComponent } from 'src/app/features/volunteer-applications-list/volunteer-applications-list.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomePageComponent, canActivate: [RouterGuard] },
  { path: 'institution', redirectTo: 'home' },
  {
    path: 'institution',
    children: [
      {
        path: 'verify',
        component: InstitutionVerifyComponent,
        canActivate: [InstitutionRegistrationGuard],
        data: { stage: InstitutionRegistrationStage.Verification },
      },
      { path: 'register', component: RegisterInstitutionComponent },
      {
        path: 'register-contact-person',
        component: RegisterContactPersonComponent,
        canActivate: [InstitutionRegistrationGuard],
        data: { stage: InstitutionRegistrationStage.ContactPersonRegistration },
      },
      {
        path: 'advertisement-panel',
        component: InstitutionAdvertisementPanelComponent,
        canActivate: [RouterGuard, RoleDependentGuard],
        data: {
          roles: [
            UserRoleEnum.InstitutionWorker,
            UserRoleEnum.InstitutionAdmin,
          ],
        },
      },
    ],
  },
  { path: '', component: MainPageComponent },
  {
    path: 'advertisement',
    children: [
      {
        path: 'list',
        component: OfferListComponent,
        canActivate: [RouterGuard],
      },
      {
        path: 'add',
        component: AddEditAdvertisementComponent,
        canActivate: [RouterGuard, RoleDependentGuard],
        data: {
          roles: [
            UserRoleEnum.InstitutionWorker,
            UserRoleEnum.InstitutionAdmin,
          ],
          operationType: AdvertisementCrudOperationType.Add,
        },
      },
      {
        path: 'edit/:adv_id',
        component: AddEditAdvertisementComponent,
        canActivate: [RouterGuard, RoleDependentGuard],
        data: {
          roles: [
            UserRoleEnum.InstitutionWorker,
            UserRoleEnum.InstitutionAdmin,
          ],
          operationType: AdvertisementCrudOperationType.Edit,
        },
      },
      {
        path: ':adv_id',
        component: AdvertisementDetailsComponent,
        canActivate: [RouterGuard],
      },
      {
        path: ':adv_id/apply',
        component: OfferApplyComponent,
        canActivate: [RouterGuard, RoleDependentGuard],
        data: {
          roles: [UserRoleEnum.Volunteer],
        },
      },
    ],
  },
  {
    path: 'applications',
    component: VolunteerApplicationsListComponent,
    canActivate: [RouterGuard, RoleDependentGuard],
    data: {
      roles: [UserRoleEnum.Volunteer],
    },
  },
  { path: '**', pathMatch: 'full', redirectTo: 'home' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
