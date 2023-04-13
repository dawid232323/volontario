import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterBasicInfoFormComponent } from 'src/app/features/register-institution/_features/register-basic-info-form/register-basic-info-form.component';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatStepperModule } from '@angular/material/stepper';

@NgModule({
  declarations: [RegisterBasicInfoFormComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatStepperModule,
  ],
  exports: [RegisterBasicInfoFormComponent],
})
export class RegisterBasicInfoFormModule {}
