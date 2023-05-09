import { Component, Input, OnInit } from '@angular/core';
import { AdvertisementPreview } from 'src/app/core/model/advertisement.model';
import { formatDate } from '@angular/common';
import { isNil } from 'lodash';
import * as moment from 'moment';

@Component({
  selector: 'app-advertisement-preview-card',
  templateUrl: './advertisement-preview-card.component.html',
  styleUrls: ['./advertisement-preview-card.component.scss'],
})
export class AdvertisementPreviewCardComponent implements OnInit {
  @Input() advertisement: AdvertisementPreview | undefined = undefined;
  constructor() {}

  ngOnInit(): void {}

  public getFormattedDate(date?: Date): string {
    if (isNil(date)) {
      return '';
    }
    return moment(date).format('DD-MM-yyyy');
  }

  protected readonly undefined = undefined;
}
