import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InstitutionWorkersListComponent } from './institution-workers-list.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';

@NgModule({
  declarations: [InstitutionWorkersListComponent],
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatMenuModule,
  ],
  exports: [InstitutionWorkersListComponent],
})
export class InstitutionWorkersListModule {}
