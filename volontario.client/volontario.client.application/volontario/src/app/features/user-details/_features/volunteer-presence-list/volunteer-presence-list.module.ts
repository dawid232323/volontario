import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VolunteerPresenceListComponent } from './volunteer-presence-list.component';
import { AdvertisementBasicInfoModule } from 'src/app/features/add-edit-advertisement/_features/advertisement-basic-info/advertisement-basic-info.module';
import { OfferCardModule } from 'src/app/shared/features/offer-card/offer-card.module';
import { MatPaginatorModule } from '@angular/material/paginator';

@NgModule({
  declarations: [VolunteerPresenceListComponent],
  exports: [VolunteerPresenceListComponent],
  imports: [
    CommonModule,
    AdvertisementBasicInfoModule,
    OfferCardModule,
    MatPaginatorModule,
  ],
})
export class VolunteerPresenceListModule {}
