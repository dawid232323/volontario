import { Component, Input, OnInit } from '@angular/core';
import { AdvertisementPreview } from '../../../core/model/advertisement.model';
import { Router } from '@angular/router';
import { isNil } from 'lodash';
import * as moment from 'moment/moment';

@Component({
  selector: 'app-offer-card',
  templateUrl: './offer-card.component.html',
  styleUrls: ['./offer-card.component.scss'],
})
export class OfferCardComponent {
  @Input() advertisement: AdvertisementPreview | undefined = undefined;
  constructor(private router: Router) {}

  public getFormattedDate(date?: Date): string {
    if (isNil(date)) {
      return '';
    }
    return moment(date).format('DD-MM-yyyy');
  }

  public goToEditAdvertisement() {
    this.router.navigate(['advertisement', 'edit', this.advertisement?.id]);
  }

  public shortTitle(title: string | undefined) {
    // @ts-ignore
    if (title.length > 100) {
      // @ts-ignore
      return title.substring(0, 100) + '...';
    } else {
      return title;
    }
  }

  protected readonly undefined = undefined;
}
