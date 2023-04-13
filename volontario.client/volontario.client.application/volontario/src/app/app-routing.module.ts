import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { HomePageComponent } from './features/home-page/home-page.component';
import { RouterGuard } from './core/guard/RouterGuard';
import { RegisterComponent } from './features/register/register.component';
import { RegisterInstitutionComponent } from 'src/app/features/register-institution/register-institution.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'register-institution', component: RegisterInstitutionComponent },
  { path: 'home', component: HomePageComponent, canActivate: [RouterGuard] },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
