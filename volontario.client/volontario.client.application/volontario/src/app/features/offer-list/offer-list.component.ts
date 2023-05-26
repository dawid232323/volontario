import { Component, OnInit } from '@angular/core';
import { InterestCategoryService } from 'src/app/core/service/interestCategory.service';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { forkJoin } from 'rxjs';
import {
  AdvertisementFilterIf,
  AdvertisementService,
} from 'src/app/core/service/advertisement.service';
import {
  AdvertisementPreview,
  AdvertisementType,
} from 'src/app/core/model/advertisement.model';
import { UserService } from 'src/app/core/service/user.service';
import { User } from 'src/app/core/model/user.model';
import { PageableModel } from 'src/app/core/model/pageable.model';
import { MatDialog } from '@angular/material/dialog';
import { MobileFilterViewComponent } from 'src/app/features/offer-list/_features/mobile-filter-view/mobile-filter-view.component';
import { isNil } from 'lodash';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-offer-list',
  templateUrl: './offer-list.component.html',
  styleUrls: ['./offer-list.component.scss'],
})
export class OfferListComponent implements OnInit {
  private _interestCategories: InterestCategoryDTO[] = [];
  private _offerTypes: AdvertisementType[] = [];
  private _pageableResult?: PageableModel<AdvertisementPreview>;
  private _offerPreviews: AdvertisementPreview[] = [];
  private _loggedUser?: User;

  private _selectedCategories: Set<number> = new Set<number>();
  private _selectedTypes: Set<number> = new Set<number>();

  private _selectedPageIndex = 0;
  private _selectedPageSize = 5;

  public typedText?: string | null = null;
  public dateFrom?: Date | null = null;
  public dateTo?: Date | null = null;
  public isLoadingData: boolean = true;

  constructor(
    private interestCategoryService: InterestCategoryService,
    private offerService: AdvertisementService,
    private userService: UserService,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.downloadData();
  }

  public get interestCategories(): InterestCategoryDTO[] {
    return this._interestCategories;
  }

  public get offerTypes(): AdvertisementType[] {
    return this._offerTypes;
  }

  public get offerPreviews(): AdvertisementPreview[] {
    return this._offerPreviews;
  }

  get selectedCategories(): Set<number> {
    return this._selectedCategories;
  }

  get selectedTypes(): Set<number> {
    return this._selectedTypes;
  }

  public openMobileFilterDialog(): void {
    const dialogRef = this.dialog.open(MobileFilterViewComponent, {
      width: '100vw',
      height: '90vh',
      data: {
        dialogData: <OfferListDialogDataIf>{
          interestCategories: this.interestCategories,
          selectedCategories: this.selectedCategories,
          offerTypes: this.offerTypes,
          selectedTypes: this.selectedTypes,
        },
      },
    });

    dialogRef.afterClosed().subscribe(this.onMobileFilterClose.bind(this));
  }

  public onSearchClicked() {
    this.downloadOffers();
  }

  private downloadData() {
    forkJoin([
      this.interestCategoryService.getAllInterestCategories(),
      this.offerService.getAllAdvertisementTypes(),
      this.userService.getCurrentUserData(),
    ]).subscribe(([interestCategories, offerTypes, user]) => {
      this._interestCategories = interestCategories;
      this._offerTypes = offerTypes;
      this._loggedUser = user;
      this.setInitialData();
      this.downloadOffers();
    });
  }

  private setInitialData() {
    this._loggedUser?.volunteerData?.interestCategories?.forEach(category => {
      this._selectedCategories.add(category.id);
    });
  }

  private getListFilters(): AdvertisementFilterIf {
    const filters: AdvertisementFilterIf = {};
    if (!isNil(this.typedText) && this.typedText !== '') {
      filters.title = this.typedText;
    }
    if (!isNil(this.dateFrom)) {
      filters.startDate = this.dateFrom;
    }
    if (!isNil(this.dateTo)) {
      filters.endDate = this.dateTo;
    }
    if (!isNil(this.selectedCategories) && this.selectedCategories.size > 0) {
      filters.interestCategoryIds = [...this.selectedCategories.values()];
    }
    if (!isNil(this.selectedTypes) && this.selectedTypes.size > 0) {
      filters.typeIds = [...this.selectedTypes.values()];
    }
    return filters;
  }

  private onMobileFilterClose(dialogFilters: OfferListDialogDataIf) {
    const { typedText, dateFrom, dateTo, selectedCategories, selectedTypes } =
      dialogFilters;
    this.typedText = typedText;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this._selectedCategories = selectedCategories;
    this._selectedTypes = selectedTypes;
    this.downloadOffers();
  }

  private downloadOffers() {
    this.isLoadingData = true;
    this.offerService
      .getAdvertisementPreviews(
        this.getListFilters(),
        this.pageIndex,
        this._selectedPageSize
      )
      .subscribe(pagedPreviews => {
        this._pageableResult = pagedPreviews;
        this._offerPreviews = pagedPreviews.content;
        this.isLoadingData = false;
      });
  }

  public onClearFilters() {
    this.typedText = null;
    this.dateFrom = null;
    this.dateTo = null;
    this.selectedCategories.clear();
    this.selectedTypes.clear();
    this.downloadOffers();
  }

  public get hasActiveFilters(): boolean {
    if (!isNil(this.typedText) && this.typedText !== '') {
      return true;
    }
    if (!isNil(this.dateFrom)) {
      return true;
    }
    if (!isNil(this.dateTo)) {
      return true;
    }
    if (!isNil(this.selectedCategories) && this.selectedCategories.size > 0) {
      return true;
    }
    if (!isNil(this.selectedTypes) && this.selectedTypes.size > 0) {
      return true;
    }
    return false;
  }

  public onPaginatorDataChange(event: PageEvent) {
    this._selectedPageIndex = event.pageIndex;
    this._selectedPageSize = event.pageSize;
    this.downloadOffers();
  }

  public get currentPageSize(): number | undefined {
    return this._pageableResult?.pageSize;
  }

  public get totalPages(): number | undefined {
    return this._pageableResult?.totalPages;
  }

  public get totalElementsNumber(): number | undefined {
    return this._pageableResult?.totalElements;
  }

  public get pageIndex(): number {
    return this._selectedPageIndex;
  }
}

export interface OfferListDialogDataIf {
  interestCategories: InterestCategoryDTO[];
  selectedCategories: Set<number>;
  offerTypes: AdvertisementType[];
  selectedTypes: Set<number>;
  typedText?: string;
  dateFrom?: Date;
  dateTo?: Date;
}