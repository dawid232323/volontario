import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterFormComponent } from './register-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { PrimaryButtonModule } from 'src/app/shared/features/primary-button/primary-button.module';

@NgModule({
  declarations: [RegisterFormComponent],
  imports: [CommonModule, ReactiveFormsModule, PrimaryButtonModule],
  exports: [RegisterFormComponent],
})
export class RegisterFormModule {}
