import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SideFilterPanelComponent } from 'src/app/features/offer-list/_features/side-filter-panel/side-filter-panel.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

@NgModule({
  declarations: [SideFilterPanelComponent],
  imports: [CommonModule, MatButtonModule, MatIconModule, MatInputModule],
  exports: [SideFilterPanelComponent],
})
export class SideFilterPanelModule {}
