import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MobileFilterViewComponent } from 'src/app/features/offer-list/_features/mobile-filter-view/mobile-filter-view.component';
import { SideFilterPanelModule } from 'src/app/features/offer-list/_features/side-filter-panel/side-filter-panel.module';
import { MatDialogModule } from '@angular/material/dialog';
import { TopFilterPanelModule } from 'src/app/features/offer-list/_features/top-filter-panel/top-filter-panel.module';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [MobileFilterViewComponent],
  imports: [
    CommonModule,
    SideFilterPanelModule,
    MatDialogModule,
    TopFilterPanelModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
  ],
  exports: [MobileFilterViewComponent],
})
export class MobileFilterViewModule {}
