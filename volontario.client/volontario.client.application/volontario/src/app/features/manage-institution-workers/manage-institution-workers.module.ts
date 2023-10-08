import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManageInstitutionWorkersComponent } from './manage-institution-workers.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { InstitutionWorkersListModule } from 'src/app/features/manage-institution-workers/_features/institution-workers-list/institution-workers-list.module';
import { AddEmployeeFormModule } from 'src/app/features/manage-institution-workers/_features/add-employee-form/add-employee-form.module';

@NgModule({
  declarations: [ManageInstitutionWorkersComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    InstitutionWorkersListModule,
    AddEmployeeFormModule,
  ],
  exports: [ManageInstitutionWorkersComponent],
})
export class ManageInstitutionWorkersModule {}
