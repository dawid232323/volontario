import { Component, EventEmitter, OnInit } from '@angular/core';
import {
  AdvertisementPreview,
  AdvertisementType,
} from 'src/app/core/model/advertisement.model';
import { PageableModel } from 'src/app/core/model/pageable.model';
import { isNil } from 'lodash';
import { InterestCategoryService } from 'src/app/core/service/interestCategory.service';
import {
  AdvertisementFilterIf,
  AdvertisementService,
} from 'src/app/core/service/advertisement.service';
import { forkJoin } from 'rxjs';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { UserService } from 'src/app/core/service/user.service';
import { User } from 'src/app/core/model/user.model';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { Router } from '@angular/router';
import { PageEvent } from '@angular/material/paginator';

export enum AdvertisementPanelTabEnum {
  Assigned = 0,
  All = 1,
}

@Component({
  selector: 'app-institution-advertisement-panel',
  templateUrl: './institution-advertisement-panel.component.html',
  styleUrls: ['./institution-advertisement-panel.component.scss'],
})
export class InstitutionAdvertisementPanelComponent implements OnInit {
  public selectedTab = AdvertisementPanelTabEnum.Assigned;
  public isLoadingData = true;
  public currentlySelectedPageIndex = 0;
  public currentlySelectedPageSize = 5;
  public advertisements: AdvertisementPreview[] = [];
  public types: AdvertisementType[] = [];
  public categories: InterestCategoryDTO[] = [];
  public loggedUser?: User;
  private _pageableData?: PageableModel<AdvertisementPreview>;
  private _filterData?: AdvertisementFilterIf;
  private _shouldShowFilterPanel = false;

  public filterClearEvent = new EventEmitter<void>();

  constructor(
    private interestCategoryService: InterestCategoryService,
    private advertisementService: AdvertisementService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.downloadInitialData();
  }

  public onSelectedTabChange(tabChangeEvent: MatTabChangeEvent) {
    this.currentlySelectedPageIndex = 0;
    this.currentlySelectedPageSize = 5;
    this.selectedTab = tabChangeEvent.index;
    this.advertisements = [];
    const filterOptions: AdvertisementFilterIf = {
      institutionId: this.loggedUser!.institution!.id!,
    };
    if (this.selectedTab === AdvertisementPanelTabEnum.Assigned) {
      filterOptions.contactPersonId = this.loggedUser?.id;
    }
    this.filterClearEvent.emit();
    this._filterData = filterOptions;
    this.loadListData();
  }

  public onPaginatorDataChange(event: PageEvent) {
    this.currentlySelectedPageIndex = event.pageIndex;
    this.currentlySelectedPageSize = event.pageSize;
    this.loadListData();
  }

  public onFilterSearchEvent(filterOptions: AdvertisementFilterIf) {
    this.currentlySelectedPageIndex = 0;
    this._filterData = filterOptions;
    this.loadListData();
  }

  private loadListData() {
    this.isLoadingData = true;
    this.advertisementService
      .getAdvertisementPreviews(
        this._filterData!,
        this.currentlySelectedPageIndex,
        this.currentlySelectedPageSize
      )
      .subscribe(previews => {
        this._pageableData = previews;
        this.advertisements = previews.content;
        this.isLoadingData = false;
      });
  }

  public onAddAdvertisementClicked() {
    this.router.navigate(['advertisement', 'add']);
  }

  public onShowFilterPanel() {
    this._shouldShowFilterPanel = !this._shouldShowFilterPanel;
  }

  private downloadInitialData() {
    forkJoin([
      this.interestCategoryService.getAllInterestCategories(),
      this.advertisementService.getAllAdvertisementTypes(),
      this.userService.getCurrentUserData(),
    ]).subscribe(([categories, types, user]) => {
      this.types = types;
      this.categories = categories;
      this.loggedUser = user;
      this._filterData = {
        institutionId: this.loggedUser!.institution!.id!,
        contactPersonId: this.loggedUser?.id,
      };
      this.loadListData();
    });
  }

  public get totalElements(): number {
    if (isNil(this._pageableData?.totalElements)) {
      return 0;
    }
    return this._pageableData!.totalElements;
  }

  public get shouldShowFilterPanel(): boolean {
    return this._shouldShowFilterPanel;
  }
}
