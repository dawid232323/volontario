import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilterPanelComponent } from 'src/app/features/institution-advertisement-panel/_features/filter-panel/filter-panel.component';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [FilterPanelComponent],
  imports: [
    CommonModule,
    MatInputModule,
    MatIconModule,
    MatDatepickerModule,
    MatButtonModule,
    MatMenuModule,
    MatTooltipModule,
    FormsModule,
  ],
  exports: [FilterPanelComponent],
})
export class FilterPanelModule {}
