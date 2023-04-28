import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterContactPersonComponent } from 'src/app/features/register-contact-person/register-contact-person.component';
import { SideBannerModule } from 'src/app/shared/features/side-banner/side-banner.module';
import { LogoModule } from 'src/app/shared/features/logo/logo.module';
import { MatCardModule } from '@angular/material/card';
import { RegisterContactPersonFormModule } from 'src/app/features/register-contact-person/_features/register-contact-person-form/register-contact-person-form.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SuccessInfoCardModule } from 'src/app/shared/features/success-info-card/success-info-card.module';

@NgModule({
  declarations: [RegisterContactPersonComponent],
  imports: [
    CommonModule,
    SideBannerModule,
    LogoModule,
    MatCardModule,
    RegisterContactPersonFormModule,
    MatProgressSpinnerModule,
    SuccessInfoCardModule,
  ],
  exports: [RegisterContactPersonComponent],
})
export class RegisterContactPersonModule {}
