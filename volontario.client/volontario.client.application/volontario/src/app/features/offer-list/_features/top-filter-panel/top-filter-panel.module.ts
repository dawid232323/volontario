import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TopFilterPanelComponent } from 'src/app/features/offer-list/_features/top-filter-panel/top-filter-panel.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@NgModule({
  declarations: [TopFilterPanelComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    FormsModule,
    MatSlideToggleModule,
  ],
  exports: [TopFilterPanelComponent],
})
export class TopFilterPanelModule {}
