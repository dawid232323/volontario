import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResetPasswordComponent } from './reset-password.component';
import { SetPasswordModule } from '../set-password-form/set-password.module';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@NgModule({
  declarations: [ResetPasswordComponent],
  imports: [
    CommonModule,
    SetPasswordModule,
    MatIconModule,
    MatProgressSpinnerModule,
  ],
})
export class ResetPasswordModule {}
