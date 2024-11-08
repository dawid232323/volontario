import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvertisementAdditionalInfoComponent } from 'src/app/features/add-edit-advertisement/_features/advertisement-additional-info/advertisement-additional-info.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AngularEditorModule } from '@kolkov/angular-editor';

@NgModule({
  declarations: [AdvertisementAdditionalInfoComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatStepperModule,
    MatIconModule,
    MatCheckboxModule,
    MatSlideToggleModule,
    MatInputModule,
    MatTooltipModule,
    AngularEditorModule,
  ],
  exports: [AdvertisementAdditionalInfoComponent],
})
export class AdvertisementAdditionalInfoModule {}
