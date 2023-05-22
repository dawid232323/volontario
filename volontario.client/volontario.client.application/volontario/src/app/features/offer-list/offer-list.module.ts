import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OfferListComponent } from 'src/app/features/offer-list/offer-list.component';
import { SideFilterPanelModule } from 'src/app/features/offer-list/_features/side-filter-panel/side-filter-panel.module';
import { TopFilterPanelModule } from 'src/app/features/offer-list/_features/top-filter-panel/top-filter-panel.module';
import { OfferCardModule } from 'src/app/shared/features/offer-card/offer-card.module';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MobileFilterViewModule } from 'src/app/features/offer-list/_features/mobile-filter-view/mobile-filter-view.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatPaginatorModule } from '@angular/material/paginator';

@NgModule({
  declarations: [OfferListComponent],
  imports: [
    CommonModule,
    SideFilterPanelModule,
    TopFilterPanelModule,
    OfferCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MobileFilterViewModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
  ],
  exports: [OfferListComponent],
})
export class OfferListModule {}
