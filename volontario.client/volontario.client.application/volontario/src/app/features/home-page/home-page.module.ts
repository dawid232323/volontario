import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomePageComponent } from './home-page.component';
import { NavModule } from '../../shared/features/nav/nav.module';
import { OfferCardModule } from '../../shared/features/offer-card/offer-card.module';

@NgModule({
  declarations: [HomePageComponent],
  imports: [CommonModule, NavModule, OfferCardModule],
  exports: [HomePageComponent],
})
export class HomePageModule {}
