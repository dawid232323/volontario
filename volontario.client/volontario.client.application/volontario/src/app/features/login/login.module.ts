import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login.component';
import { SideBannerModule } from '../../shared/features/side-banner/side-banner.module';
import { LoginFormModule } from './_features/login-form/login-form.module';
import { LogoModule } from '../../shared/features/logo/logo.module';

@NgModule({
  declarations: [LoginComponent],
  imports: [CommonModule, SideBannerModule, LoginFormModule, LogoModule],
  exports: [LoginComponent],
})
export class LoginModule {}
