import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvertisementPreviewCardComponent } from 'src/app/shared/features/advertisement-preview-card/advertisement-preview-card.component';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';

@NgModule({
  declarations: [AdvertisementPreviewCardComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
  ],
  exports: [AdvertisementPreviewCardComponent],
})
export class AdvertisementPreviewCardModule {}
