import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InstitutionDetailsComponent } from './institution-details.component';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatChipsModule } from '@angular/material/chips';
import { OfferCardModule } from 'src/app/shared/features/offer-card/offer-card.module';

@NgModule({
  declarations: [InstitutionDetailsComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatChipsModule,
    OfferCardModule,
  ],
  exports: [InstitutionDetailsComponent],
})
export class InstitutionDetailsModule {}