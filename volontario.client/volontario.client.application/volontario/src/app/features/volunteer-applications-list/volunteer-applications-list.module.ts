import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VolunteerApplicationsListComponent } from 'src/app/features/volunteer-applications-list/volunteer-applications-list.component';
import { ApplicationsListModule } from 'src/app/features/volunteer-applications-list/_features/applications-list/applications-list.module';
import { ApplicationsFilterModule } from 'src/app/features/volunteer-applications-list/_features/applications-filter/applications-filter.module';
import { MatPaginatorModule } from '@angular/material/paginator';

@NgModule({
  declarations: [VolunteerApplicationsListComponent],
  imports: [
    CommonModule,
    ApplicationsListModule,
    ApplicationsFilterModule,
    MatPaginatorModule,
  ],
  exports: [VolunteerApplicationsListComponent],
})
export class VolunteerApplicationsListModule {}
