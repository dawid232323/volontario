import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { AdvertisementPreview } from 'src/app/core/model/advertisement.model';
import { isNil } from 'lodash';
import { MatPaginator, PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-volunteer-presence-list',
  templateUrl: './volunteer-presence-list.component.html',
  styleUrls: ['./volunteer-presence-list.component.scss'],
})
export class VolunteerPresenceListComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  private _offers: AdvertisementPreview[] = [];
  private _displayedOffers: AdvertisementPreview[] = [];

  @Input()
  public set offers(offers: AdvertisementPreview[]) {
    if (isNil(offers)) {
      return;
    }
    this._offers = offers;
    this._displayedOffers = offers.slice(0, this.paginator?.pageSize - 1 || 6);
    if (!isNil(this.paginator)) {
      this.paginator.pageIndex = 0;
    }
  }

  constructor() {}

  ngOnInit(): void {}

  public onPageEvent(event: PageEvent): void {
    if (isNil(this._offers)) {
      return;
    }
    const startIndex = event.pageSize * event.pageIndex;
    const endIndex = startIndex + event.pageSize;
    this._displayedOffers = this.offers.slice(startIndex, endIndex);
  }

  public get offers(): AdvertisementPreview[] {
    return this._offers;
  }

  public get displayedOffers(): AdvertisementPreview[] {
    return this._displayedOffers;
  }
}
