import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';
import { PasswordCheckCardModule } from '../../shared/features/password-check-card/password-check-card.module';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { UserEditDataComponent } from './user-edit-data.component';

@NgModule({
  declarations: [UserEditDataComponent],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatIconModule,
    MatCheckboxModule,
    MatOptionModule,
    MatSelectModule,
    MatTooltipModule,
    PasswordCheckCardModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
  ],
})
export class UserEditDataModule {}
