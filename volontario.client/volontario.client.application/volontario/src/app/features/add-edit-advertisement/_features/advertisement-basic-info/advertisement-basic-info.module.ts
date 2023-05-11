import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvertisementBasicInfoComponent } from 'src/app/features/add-edit-advertisement/_features/advertisement-basic-info/advertisement-basic-info.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';

@NgModule({
  declarations: [AdvertisementBasicInfoComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatStepperModule,
    MatSelectModule,
    MatDatepickerModule,
  ],
  exports: [AdvertisementBasicInfoComponent],
})
export class AdvertisementBasicInfoModule {}
