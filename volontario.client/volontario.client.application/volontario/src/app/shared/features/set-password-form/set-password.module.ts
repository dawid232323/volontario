import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SetPasswordComponent } from 'src/app/shared/features/set-password-form/set-password.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { PasswordCheckCardModule } from 'src/app/shared/features/password-check-card/password-check-card.module';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [SetPasswordComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatIconModule,
    PasswordCheckCardModule,
    MatButtonModule,
    MatStepperModule,
    MatDialogModule,
  ],
  exports: [SetPasswordComponent],
})
export class SetPasswordModule {}
