import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationsFilterComponent } from 'src/app/features/volunteer-applications-list/_features/applications-filter/applications-filter.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';

@NgModule({
  declarations: [ApplicationsFilterComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    FormsModule,
    MatDatepickerModule,
    MatSelectModule,
  ],
  exports: [ApplicationsFilterComponent],
})
export class ApplicationsFilterModule {}
