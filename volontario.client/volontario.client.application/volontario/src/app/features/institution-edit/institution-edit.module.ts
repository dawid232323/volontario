import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InstitutionEditComponent } from './institution-edit.component';
import { InstitutionEditFormModule } from 'src/app/features/institution-edit/_features/institution-edit-form/institution-edit-form.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@NgModule({
  declarations: [InstitutionEditComponent],
  imports: [CommonModule, InstitutionEditFormModule, MatProgressSpinnerModule],
  exports: [InstitutionEditComponent],
})
export class InstitutionEditModule {}
