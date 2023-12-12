import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterFormComponent } from './register-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PrimaryButtonModule } from 'src/app/shared/features/primary-button/primary-button.module';
import { MatSelectModule } from '@angular/material/select';
import { MatSliderModule } from '@angular/material/slider';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { PasswordCheckCardModule } from 'src/app/shared/features/password-check-card/password-check-card.module';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { MatMenuModule } from '@angular/material/menu';

@NgModule({
  declarations: [RegisterFormComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    PrimaryButtonModule,
    MatSelectModule,
    MatSliderModule,
    MatInputModule,
    MatIconModule,
    MatCheckboxModule,
    FormsModule,
    PasswordCheckCardModule,
    MatTooltipModule,
    RouterLink,
    MatMenuModule,
  ],
  exports: [RegisterFormComponent],
})
export class RegisterFormModule {}
