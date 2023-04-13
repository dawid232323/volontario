import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterAdditionalInfoFormComponent } from 'src/app/features/register-institution/_features/register-additional-info-form/register-additional-info-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatCheckboxModule } from '@angular/material/checkbox';

@NgModule({
  declarations: [RegisterAdditionalInfoFormComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatChipsModule,
    MatIconModule,
    MatButtonModule,
    MatStepperModule,
    MatCheckboxModule,
  ],
  exports: [RegisterAdditionalInfoFormComponent],
})
export class RegisterAdditionalInfoFormModule {}
