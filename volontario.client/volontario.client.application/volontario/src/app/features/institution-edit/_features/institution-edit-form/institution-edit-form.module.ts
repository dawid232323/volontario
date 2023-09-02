import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InstitutionEditFormComponent } from './institution-edit-form.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [InstitutionEditFormComponent],
  imports: [CommonModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule, MatIconModule, MatChipsModule, MatButtonModule],
  exports: [InstitutionEditFormComponent],
})
export class InstitutionEditFormModule {}
