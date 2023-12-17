import { Component, OnInit } from '@angular/core';
import { InterestCategoryService } from 'src/app/core/service/interestCategory.service';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { forkJoin } from 'rxjs';
import {
  AdvertisementFilterIf,
  AdvertisementService,
  AdvertisementVisibilityEnum,
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
import * as moment from 'moment';
import { ActivatedRoute, Params, Router } from '@angular/router';
import {
  addQueryParamFromBoolean,
  addQueryParamFromDate,
  addQueryParamFromNumber,
  addQueryParamFromSet,
  addQueryParamFromString,
  updateActiveUrl,
} from '../../utils/url.util';
import { HttpParams } from '@angular/common/http';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';

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

  private _availablePageSizes = [5, 10, 15];
  private _selectedPageIndex = 0;
  private _selectedPageSize = this._availablePageSizes[0];
  private _showHiddenOffers = false;

  public typedText?: string | null = null;
  public dateFrom?: Date | null = null;
  public dateTo?: Date | null = null;
  public isLoadingData: boolean = true;

  constructor(
    private interestCategoryService: InterestCategoryService,
    private offerService: AdvertisementService,
    private userService: UserService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.downloadData();
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
          canSeeHiddenOffers: this.canSeeHiddenOffers,
          showHidden: this._showHiddenOffers,
        },
      },
    });

    dialogRef.afterClosed().subscribe(this.onMobileFilterClose.bind(this));
  }

  public onSearchClicked() {
    this.onSelectionChanged();
    this.downloadOffers();
  }

  public onSelectionChanged() {
    const httpParams: Params = new HttpParams();

    addQueryParamFromSet(
      'interestCategories',
      this.selectedCategories,
      httpParams
    );
    addQueryParamFromSet('offerTypes', this.selectedTypes, httpParams);
    addQueryParamFromNumber('page', this._selectedPageIndex, httpParams);
    addQueryParamFromNumber('pageSize', this._selectedPageSize, httpParams);
    addQueryParamFromString('text', this.typedText, httpParams);
    addQueryParamFromDate('dateFrom', this.dateFrom, httpParams);
    addQueryParamFromDate('dateTo', this.dateTo, httpParams);
    if (this.canSeeHiddenOffers) {
      addQueryParamFromBoolean(
        'showHidden',
        this._showHiddenOffers,
        httpParams
      );
    }

    delete httpParams['encoder']; // to get rid of obsolete http param.

    this.offerService.offerListQueryParams = httpParams;
    updateActiveUrl(this.router, this.activatedRoute, httpParams);
  }

  private initStateFromLastSavedQueryParams() {
    if (!isNil(this.offerService.offerListQueryParams)) {
      this.initStateFromQueryParams(this.offerService.offerListQueryParams);
    }
  }

  private initStateFromQueryParamsAfterRefresh() {
    const urlParams = Object.fromEntries(new URLSearchParams(location.search));
    this.initStateFromQueryParams(urlParams);
  }

  private initStateFromQueryParams(
    urlParams: Params | { [p: string]: string }
  ) {
    this.unsetInitialData();

    if (!isNil(urlParams['interestCategories'])) {
      urlParams['interestCategories']
        .split(',')
        .map((id: string) => Number(id))
        .forEach((id: number) => this._selectedCategories.add(id));
    }

    if (!isNil(urlParams['offerTypes'])) {
      urlParams['offerTypes']
        .split(',')
        .map((id: string) => Number(id))
        .forEach((id: number) => this.selectedTypes.add(id));
    }

    if (!isNil(urlParams['pageSize'])) {
      this._selectedPageSize = Number(urlParams['pageSize']);
    }

    if (!isNil(urlParams['page'])) {
      this._selectedPageIndex = Number(urlParams['page']);
    }

    if (!isNil(urlParams['text'])) {
      this.typedText = urlParams['text'];
    }

    if (!isNil(urlParams['dateFrom'])) {
      this.dateFrom = new Date(urlParams['dateFrom']);
    }

    if (!isNil(urlParams['dateTo'])) {
      this.dateTo = new Date(urlParams['dateTo']);
    }
    if (
      this.canSeeHiddenOffers &&
      urlParams['showHidden'] &&
      JSON.parse(urlParams['showHidden']) === true &&
      this.canSeeHiddenOffers
    ) {
      this._showHiddenOffers = true;
    }

    updateActiveUrl(this.router, this.activatedRoute, urlParams);
  }

  private downloadData() {
    forkJoin([
      this.interestCategoryService.getUsedValues(),
      this.offerService.getAllAdvertisementTypes(),
      this.userService.getCurrentUserData(),
    ]).subscribe(([interestCategories, offerTypes, user]) => {
      this._interestCategories = interestCategories;
      this._offerTypes = offerTypes;
      this._loggedUser = user;
      if (!isNil(this.offerService.offerListQueryParams)) {
        this.initStateFromLastSavedQueryParams();
      } else {
        this.initStateFromQueryParamsAfterRefresh();
      }
      this.setInitialData();
      this.downloadOffers();
    });
  }

  private setInitialData() {
    if (this.offerService.offerListQueryParams === undefined) {
      this._loggedUser?.volunteerData?.interestCategories?.forEach(category => {
        this._selectedCategories.add(category.id);
      });
    } else {
      this.initStateFromLastSavedQueryParams();
    }
    this.onSelectionChanged();
  }

  private unsetInitialData() {
    this._loggedUser?.volunteerData?.interestCategories?.forEach(category => {
      this._selectedCategories.delete(category.id);
    });
  }

  private getListFilters(): AdvertisementFilterIf {
    const filters: AdvertisementFilterIf = {};
    if (!isNil(this.typedText) && this.typedText !== '') {
      filters.title = this.typedText;
    }
    if (!isNil(this.dateFrom)) {
      filters.startDate = moment(this.dateFrom).format('YYYY-MM-DD');
    }
    if (!isNil(this.dateTo)) {
      filters.endDate = moment(this.dateTo).format('YYYY-MM-DD');
    }
    if (!isNil(this.selectedCategories) && this.selectedCategories.size > 0) {
      filters.interestCategoryIds = [...this.selectedCategories.values()];
    }
    if (!isNil(this.selectedTypes) && this.selectedTypes.size > 0) {
      filters.typeIds = [...this.selectedTypes.values()];
    }
    if (this.canSeeHiddenOffers) {
      if (this._showHiddenOffers) {
        filters.visibility = AdvertisementVisibilityEnum.Hidden;
      } else {
        filters.visibility = AdvertisementVisibilityEnum.Active;
      }
    }
    return filters;
  }

  private onMobileFilterClose(dialogFilters: OfferListDialogDataIf) {
    if (isNil(dialogFilters)) {
      return;
    }
    const {
      typedText,
      dateFrom,
      dateTo,
      selectedCategories,
      selectedTypes,
      showHidden,
    } = dialogFilters;
    this.typedText = typedText;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this._selectedCategories = selectedCategories;
    this._selectedTypes = selectedTypes;
    this._showHiddenOffers = showHidden || false;
    this.onSelectionChanged();
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
    this._showHiddenOffers = false;
    this.selectedCategories.clear();
    this.selectedTypes.clear();
    this.onSelectionChanged();
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
    this.onSelectionChanged();
    this.downloadOffers();
  }

  public get currentPageSize(): number {
    return this._selectedPageSize;
  }

  public get availablePageSizes(): number[] {
    return this._availablePageSizes;
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

  public get showHiddenOffers(): boolean {
    return this._showHiddenOffers;
  }

  public set showHiddenOffers(value: boolean) {
    this._showHiddenOffers = value;
  }

  public get canSeeHiddenOffers(): boolean {
    return (
      this._loggedUser?.hasUserRoles([
        UserRoleEnum.Admin,
        UserRoleEnum.Moderator,
      ]) || false
    );
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
}

export interface OfferListDialogDataIf {
  interestCategories: InterestCategoryDTO[];
  selectedCategories: Set<number>;
  offerTypes: AdvertisementType[];
  selectedTypes: Set<number>;
  canSeeHiddenOffers: boolean;
  typedText?: string;
  dateFrom?: Date;
  dateTo?: Date;
  showHidden?: boolean;
}
