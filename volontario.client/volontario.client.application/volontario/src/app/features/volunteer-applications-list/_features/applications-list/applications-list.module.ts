import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationsListComponent } from 'src/app/features/volunteer-applications-list/_features/applications-list/applications-list.component';
import { MatTableModule } from '@angular/material/table';

@NgModule({
  declarations: [ApplicationsListComponent],
  imports: [CommonModule, MatTableModule],
  exports: [ApplicationsListComponent],
})
export class ApplicationsListModule {}
