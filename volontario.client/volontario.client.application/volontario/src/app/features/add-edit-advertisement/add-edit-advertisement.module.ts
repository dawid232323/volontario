import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddEditAdvertisementComponent } from 'src/app/features/add-edit-advertisement/add-edit-advertisement.component';
import { FooterModule } from 'src/app/shared/features/footer/footer.module';
import { MatStepperModule } from '@angular/material/stepper';
import { RegisterBasicInfoFormModule } from 'src/app/features/register-institution/_features/register-basic-info-form/register-basic-info-form.module';
import { MatIconModule } from '@angular/material/icon';
import { AdvertisementBasicInfoModule } from 'src/app/features/add-edit-advertisement/_features/advertisement-basic-info/advertisement-basic-info.module';
import { AdvertisementAdditionalInfoModule } from 'src/app/features/add-edit-advertisement/_features/advertisement-additional-info/advertisement-additional-info.module';
import { AdvertisementOptionalInfoModule } from 'src/app/features/add-edit-advertisement/_features/advertisement-optional-info/advertisement-optional-info.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SuccessInfoCardModule } from 'src/app/shared/features/success-info-card/success-info-card.module';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [AddEditAdvertisementComponent],
  imports: [
    CommonModule,
    FooterModule,
    MatStepperModule,
    RegisterBasicInfoFormModule,
    MatIconModule,
    AdvertisementBasicInfoModule,
    AdvertisementAdditionalInfoModule,
    AdvertisementOptionalInfoModule,
    MatProgressSpinnerModule,
    SuccessInfoCardModule,
    MatButtonModule,
  ],
  exports: [AddEditAdvertisementComponent],
})
export class AddEditAdvertisementModule {}
