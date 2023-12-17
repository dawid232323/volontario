import { Component, Inject, Input, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { AdvertisementType } from 'src/app/core/model/advertisement.model';
import { OfferListDialogDataIf } from 'src/app/features/offer-list/offer-list.component';
import { AdvertisementFilterIf } from 'src/app/core/service/advertisement.service';

@Component({
  selector: 'app-mobile-filter-view',
  templateUrl: './mobile-filter-view.component.html',
  styleUrls: ['./mobile-filter-view.component.scss'],
})
export class MobileFilterViewComponent implements OnInit {
  private _selectedCategories: Set<number> = new Set<number>();
  private _selectedTypes: Set<number> = new Set<number>();
  private _interestCategories: InterestCategoryDTO[] = [];
  private _offerTypes: AdvertisementType[] = [];
  private _canSeeHiddenOffers = false;

  public typedText?: string;
  public dateFrom?: Date;
  public dateTo?: Date;
  public showHiddenOffers: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<MobileFilterViewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit(): void {
    const initialData: OfferListDialogDataIf = this.data.dialogData;
    this._offerTypes = initialData.offerTypes;
    this._interestCategories = initialData.interestCategories;
    this._selectedCategories = initialData.selectedCategories;
    this._selectedTypes = initialData.selectedTypes;
    this._canSeeHiddenOffers = initialData.canSeeHiddenOffers;
    this.showHiddenOffers = initialData.showHidden || false;
  }

  public onDialogClose() {
    this.dialogRef.close(this.filter);
  }

  private get filter(): OfferListDialogDataIf {
    return {
      typedText: this.typedText,
      dateFrom: this.dateFrom,
      dateTo: this.dateTo,
      selectedCategories: this.selectedCategories,
      selectedTypes: this.selectedTypes,
      interestCategories: this._interestCategories,
      offerTypes: this.offerTypes,
      canSeeHiddenOffers: this._canSeeHiddenOffers,
      showHidden: this.showHiddenOffers,
    };
  }

  get selectedCategories(): Set<number> {
    return this._selectedCategories;
  }

  get selectedTypes(): Set<number> {
    return this._selectedTypes;
  }

  public get interestCategories(): InterestCategoryDTO[] {
    return this._interestCategories;
  }

  public get offerTypes(): AdvertisementType[] {
    return this._offerTypes;
  }

  public get canSeeHiddenOffers(): boolean {
    return this._canSeeHiddenOffers;
  }
}
