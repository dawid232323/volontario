import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvertisementPreviewCardComponent } from 'src/app/shared/features/advertisement-preview-card/advertisement-preview-card.component';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ConfirmationAlertModule } from 'src/app/shared/features/confirmation-alert/confirmation-alert.module';

@NgModule({
  declarations: [AdvertisementPreviewCardComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatToolbarModule,
    MatMenuModule,
    MatInputModule,
    RouterLink,
    MatTooltipModule,
    ConfirmationAlertModule,
  ],
  exports: [AdvertisementPreviewCardComponent],
})
export class AdvertisementPreviewCardModule {}
