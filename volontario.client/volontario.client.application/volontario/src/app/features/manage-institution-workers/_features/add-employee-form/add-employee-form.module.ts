import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddEmployeeFormComponent } from './add-employee-form.component';
import { MatDialogModule } from '@angular/material/dialog';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [AddEmployeeFormComponent],
  imports: [
    CommonModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  exports: [AddEmployeeFormComponent],
})
export class AddEmployeeFormModule {}
