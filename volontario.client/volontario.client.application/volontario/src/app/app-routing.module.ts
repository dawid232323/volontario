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
    ],
  },
  { path: '', component: MainPageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
