import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InstitutionAdvertisementPanelComponent } from 'src/app/features/institution-advertisement-panel/institution-advertisement-panel.component';
import { FooterModule } from 'src/app/shared/features/footer/footer.module';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatButtonModule } from '@angular/material/button';
import { FilterPanelModule } from 'src/app/features/institution-advertisement-panel/_features/filter-panel/filter-panel.module';
import { AdvertisementPreviewCardModule } from 'src/app/shared/features/advertisement-preview-card/advertisement-preview-card.module';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@NgModule({
  declarations: [InstitutionAdvertisementPanelComponent],
  imports: [
    CommonModule,
    FooterModule,
    MatIconModule,
    MatTabsModule,
    MatButtonModule,
    FilterPanelModule,
    AdvertisementPreviewCardModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
  ],
  exports: [InstitutionAdvertisementPanelComponent],
})
export class InstitutionAdvertisementPanelModule {}
