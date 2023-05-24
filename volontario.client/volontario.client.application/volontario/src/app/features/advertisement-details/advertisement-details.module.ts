import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvertisementDetailsComponent } from './advertisement-details.component';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatChipsModule } from '@angular/material/chips';
import { PrimaryButtonModule } from '../../shared/features/primary-button/primary-button.module';

@NgModule({
  declarations: [AdvertisementDetailsComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatDividerModule,
    MatListModule,
    MatChipsModule,
    PrimaryButtonModule,
  ],
  exports: [AdvertisementDetailsComponent],
})
export class AdvertisementDetailsModule {}
