import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterInstitutionComponent } from 'src/app/features/register-institution/register-institution.component';
import { SideBannerModule } from 'src/app/shared/features/side-banner/side-banner.module';
import { LogoModule } from 'src/app/shared/features/logo/logo.module';
import { RegisterBasicInfoFormModule } from 'src/app/features/register-institution/_features/register-basic-info-form/register-basic-info-form.module';
import { RegisterAdditionalInfoFormModule } from 'src/app/features/register-institution/_features/register-additional-info-form/register-additional-info-form.module';
import { MatStepperModule } from '@angular/material/stepper';
import { ReactiveFormsModule } from '@angular/forms';
import { SuccessInfoCardModule } from 'src/app/shared/features/success-info-card/success-info-card.module';

@NgModule({
  declarations: [RegisterInstitutionComponent],
  imports: [
    CommonModule,
    SideBannerModule,
    LogoModule,
    RegisterBasicInfoFormModule,
    RegisterAdditionalInfoFormModule,
    MatStepperModule,
    ReactiveFormsModule,
    SuccessInfoCardModule,
  ],
  exports: [RegisterInstitutionComponent],
})
export class RegisterInstitutionModule {}
