import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from './register.component';
import { SideBannerModule } from 'src/app/shared/features/side-banner/side-banner.module';
import { LogoModule } from 'src/app/shared/features/logo/logo.module';
import { RegisterFormModule } from './_features/register-form/register-form.module';
import { SuccessInfoCardModule } from 'src/app/shared/features/success-info-card/success-info-card.module';

@NgModule({
  declarations: [RegisterComponent],
  imports: [
    CommonModule,
    SideBannerModule,
    LogoModule,
    RegisterFormModule,
    SuccessInfoCardModule,
  ],
  exports: [RegisterComponent],
})
export class RegisterModule {}
