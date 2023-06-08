import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationListComponent } from 'src/app/features/advertisement-details/_features/application-list/application-list.component';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { ApplicationDetailsRowModule } from 'src/app/features/advertisement-details/_features/application-list/_features/application-details-row/application-details-row.module';

@NgModule({
  declarations: [ApplicationListComponent],
  imports: [
    CommonModule,
    MatTableModule,
    MatSortModule,
    ApplicationDetailsRowModule,
  ],
  exports: [ApplicationListComponent],
})
export class ApplicationListModule {}
