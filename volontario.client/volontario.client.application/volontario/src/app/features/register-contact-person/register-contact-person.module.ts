import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterContactPersonComponent } from 'src/app/features/register-contact-person/register-contact-person.component';
import { SideBannerModule } from 'src/app/shared/features/side-banner/side-banner.module';
import { LogoModule } from 'src/app/shared/features/logo/logo.module';
import { MatCardModule } from '@angular/material/card';
import { SetPasswordModule } from 'src/app/shared/features/set-password-form/set-password.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { InfoCardModule } from 'src/app/shared/features/success-info-card/info-card.module';

@NgModule({
  declarations: [RegisterContactPersonComponent],
  imports: [
    CommonModule,
    SideBannerModule,
    LogoModule,
    MatCardModule,
    SetPasswordModule,
    MatProgressSpinnerModule,
    InfoCardModule,
  ],
  exports: [RegisterContactPersonComponent],
})
export class RegisterContactPersonModule {}
