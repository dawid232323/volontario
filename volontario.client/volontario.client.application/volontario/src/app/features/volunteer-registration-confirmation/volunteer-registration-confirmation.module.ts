import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VolunteerRegistrationConfirmationComponent } from './volunteer-registration-confirmation.component';
import { InfoCardModule } from '../../shared/features/success-info-card/info-card.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SideBannerModule } from '../../shared/features/side-banner/side-banner.module';

@NgModule({
  declarations: [VolunteerRegistrationConfirmationComponent],
  imports: [
    CommonModule,
    InfoCardModule,
    MatProgressSpinnerModule,
    SideBannerModule,
  ],
})
export class VolunteerRegistrationConfirmationModule {}
