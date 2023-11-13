import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvertisementDetailsComponent } from './advertisement-details.component';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatChipsModule } from '@angular/material/chips';
import { PrimaryButtonModule } from '../../shared/features/primary-button/primary-button.module';
import { RouterLink } from '@angular/router';
import { ApplicationListModule } from 'src/app/features/advertisement-details/_features/application-list/application-list.module';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTabsModule } from '@angular/material/tabs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { VolunteerConfirmPresenceModule } from 'src/app/features/advertisement-details/_features/volunteer-confirm-presence/volunteer-confirm-presence.module';

@NgModule({
  declarations: [AdvertisementDetailsComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatDividerModule,
    MatListModule,
    MatChipsModule,
    PrimaryButtonModule,
    RouterLink,
    ApplicationListModule,
    MatButtonModule,
    MatTooltipModule,
    MatTabsModule,
    MatFormFieldModule,
    MatIconModule,
    VolunteerConfirmPresenceModule,
  ],
  exports: [AdvertisementDetailsComponent],
})
export class AdvertisementDetailsModule {}
