import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterContactPersonFormComponent } from 'src/app/features/register-contact-person/_features/register-contact-person-form/register-contact-person-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { PasswordCheckCardModule } from 'src/app/shared/features/password-check-card/password-check-card.module';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';

@NgModule({
  declarations: [RegisterContactPersonFormComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatIconModule,
    PasswordCheckCardModule,
    MatButtonModule,
    MatStepperModule,
  ],
  exports: [RegisterContactPersonFormComponent],
})
export class RegisterContactPersonFormModule {}
