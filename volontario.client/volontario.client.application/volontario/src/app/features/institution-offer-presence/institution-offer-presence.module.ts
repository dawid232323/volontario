import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InstitutionOfferPresenceComponent } from './institution-offer-presence.component';
import { InstitutionOfferPresenceVolunteersListModule } from 'src/app/features/institution-offer-presence/_features/institution-offer-presence-volunteers-list/institution-offer-presence-volunteers-list.module';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@NgModule({
  declarations: [InstitutionOfferPresenceComponent],
  imports: [
    CommonModule,
    InstitutionOfferPresenceVolunteersListModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatProgressSpinnerModule,
  ],
  exports: [InstitutionOfferPresenceComponent],
})
export class InstitutionOfferPresenceModule {}
