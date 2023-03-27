import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginFormComponent } from './login-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { PrimaryButtonModule } from 'src/app/shared/features/primary-button/primary-button.module';

@NgModule({
  declarations: [LoginFormComponent],
  imports: [CommonModule, ReactiveFormsModule, PrimaryButtonModule],
  exports: [LoginFormComponent],
})
export class LoginFormModule {}
