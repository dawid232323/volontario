import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomePageComponent } from './home-page.component';
import { NavModule } from '../../shared/features/nav/nav.module';
import { OfferCardModule } from '../../shared/features/offer-card/offer-card.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterLink, RouterLinkWithHref } from '@angular/router';

@NgModule({
  declarations: [HomePageComponent],
  imports: [
    CommonModule,
    NavModule,
    OfferCardModule,
    MatProgressSpinnerModule,
    RouterLinkWithHref,
    RouterLink,
  ],
  exports: [HomePageComponent],
})
export class HomePageModule {}
