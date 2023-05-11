import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvertisementOptionalInfoComponent } from 'src/app/features/add-edit-advertisement/_features/advertisement-optional-info/advertisement-optional-info.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatTooltipModule } from '@angular/material/tooltip';

@NgModule({
  declarations: [AdvertisementOptionalInfoComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatSlideToggleModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule,
    MatButtonModule,
    MatStepperModule,
    MatTooltipModule,
  ],
  exports: [AdvertisementOptionalInfoComponent],
})
export class AdvertisementOptionalInfoModule {}
