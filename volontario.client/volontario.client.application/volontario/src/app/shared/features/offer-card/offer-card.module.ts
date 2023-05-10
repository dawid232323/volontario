import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OfferCardComponent } from './offer-card.component';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [OfferCardComponent],
  imports: [CommonModule, MatCardModule, MatButtonModule],
  exports: [OfferCardComponent],
})
export class OfferCardModule {}
