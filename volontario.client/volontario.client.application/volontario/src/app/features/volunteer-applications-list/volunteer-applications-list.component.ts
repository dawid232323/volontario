import { Component, OnInit } from '@angular/core';
import { ApplicationBaseInfo } from 'src/app/core/model/application.model';
import {
  BaseApplicationFiltersIf,
  OfferApplicationService,
} from 'src/app/core/service/offer-application.service';
import { UserService } from 'src/app/core/service/user.service';
import { User } from 'src/app/core/model/user.model';
import { firstValueFrom } from 'rxjs';
import { PageableModel } from 'src/app/core/model/pageable.model';
import { isNil } from 'lodash';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'app-volunteer-applications-list',
  templateUrl: './volunteer-applications-list.component.html',
  styleUrls: ['./volunteer-applications-list.component.scss'],
})
export class VolunteerApplicationsListComponent implements OnInit {
  public applications: ApplicationBaseInfo[] = [];
  public pageableResult?: PageableModel<ApplicationBaseInfo>;
  public loggedUser?: User;

  private _offerTitle?: string;
  private _offerStartDate?: Date;
  private _offerEndDate?: Date;
  private _offerState?: string = '-1';
  private _currentPageSize: number = 5;
  private _totalElementsNumber: number = 0;
  private _pageIndex: number = 0;
  private _availablePageSizes: number[] = [5, 10, 15];

  constructor(
    private applicationsService: OfferApplicationService,
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {}

  async ngOnInit() {
    this.loggedUser = await firstValueFrom(
      this.userService.getCurrentUserData()
    );
    this.processUrlQueryParams();
    this.loadData();
  }

  get offerTitle(): string | undefined {
    return this._offerTitle;
  }

  set offerTitle(value: string | undefined) {
    this._offerTitle = value;
  }

  get offerStartDate(): Date | undefined {
    return this._offerStartDate;
  }

  set offerStartDate(value: Date | undefined) {
    this._offerStartDate = value;
  }

  get offerEndDate(): Date | undefined {
    return this._offerEndDate;
  }

  set offerEndDate(value: Date | undefined) {
    this._offerEndDate = value;
  }

  get offerState(): string | undefined {
    return this._offerState;
  }

  set offerState(value: string | undefined) {
    this._offerState = value;
    const queryParams: Params = {};
    queryParams[VolunteerApplicationListQueryParamsEnum.State] = value;
    this.updateUrl(queryParams);
  }

  get currentPageSize(): number {
    return this._currentPageSize;
  }

  set currentPageSize(value: number) {
    this._currentPageSize = value;
  }

  get pageIndex(): number {
    return this._pageIndex;
  }

  set pageIndex(value: number) {
    this._pageIndex = value;
  }

  get availablePageSizes(): number[] {
    return this._availablePageSizes;
  }

  set availablePageSizes(value: number[]) {
    this._availablePageSizes = value;
  }

  get totalElementsNumber(): number {
    return this._totalElementsNumber;
  }

  set totalElementsNumber(value: number) {
    this._totalElementsNumber = value;
  }

  public onSearchButtonClicked() {
    this.loadData();
  }

  private loadData() {
    this.applicationsService
      .getBaseApplicationList(
        this.getDownloadFilters(),
        this._pageIndex,
        this._currentPageSize
      )
      .subscribe({
        next: result => {
          this.pageableResult = result;
          this.applications = result.content;
          this._totalElementsNumber = this.pageableResult.totalElements;
        },
      });
  }

  private getDownloadFilters(): BaseApplicationFiltersIf {
    const baseFilter: BaseApplicationFiltersIf = {
      volunteerId: this.loggedUser?.id,
    };
    if (!isNil(this.offerState) && this.offerState !== '-1') {
      baseFilter.state = this.offerState;
    }
    return baseFilter;
  }

  onPaginatorDataChange(event: PageEvent) {
    this._pageIndex = event.pageIndex;
    this._currentPageSize = event.pageSize;
    this.loadData();
  }

  private processUrlQueryParams() {
    const urlParams = this.activatedRoute.snapshot.queryParams;
    if (!isNil(urlParams[VolunteerApplicationListQueryParamsEnum.State])) {
      this.offerState =
        urlParams[VolunteerApplicationListQueryParamsEnum.State];
    }
  }

  private updateUrl(params: Params) {
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: params,
      queryParamsHandling: 'merge',
    });
  }

  onListRowClicked(selectedRow: ApplicationBaseInfo) {
    this.router.navigate(['advertisement', selectedRow.offer?.id]);
  }
}

enum VolunteerApplicationListQueryParamsEnum {
  State = 'state',
}
