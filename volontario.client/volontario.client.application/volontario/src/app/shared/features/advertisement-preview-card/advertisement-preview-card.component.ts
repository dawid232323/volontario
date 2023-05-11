import { Component, Input, OnInit } from '@angular/core';
import { AdvertisementPreview } from 'src/app/core/model/advertisement.model';
import { formatDate } from '@angular/common';
import { isNil } from 'lodash';
import * as moment from 'moment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-advertisement-preview-card',
  templateUrl: './advertisement-preview-card.component.html',
  styleUrls: ['./advertisement-preview-card.component.scss'],
})
export class AdvertisementPreviewCardComponent implements OnInit {
  @Input() advertisement: AdvertisementPreview | undefined = undefined;
  constructor(private router: Router) {}

  ngOnInit(): void {}

  public getFormattedDate(date?: Date): string {
    if (isNil(date)) {
      return '';
    }
    return moment(date).format('DD-MM-yyyy');
  }

  public goToEditAdvertisement() {
    this.router.navigate(['advertisement', 'edit', this.advertisement?.id]);
  }

  protected readonly undefined = undefined;
}
