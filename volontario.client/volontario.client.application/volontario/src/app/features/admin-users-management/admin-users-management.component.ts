import {
  AfterViewInit,
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { UserService } from 'src/app/core/service/user.service';
import {
  AdminUsersManagementQueryParamsIf,
  UsersFilterPaneComponent,
} from 'src/app/features/admin-users-management/_features/users-filter-pane/users-filter-pane.component';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { isNil } from 'lodash';
import { HttpParams } from '@angular/common/http';
import { updateActiveUrl } from 'src/app/utils/url.util';
import { AdministrativeUserDetails } from 'src/app/core/model/user.model';
import { PageEvent } from '@angular/material/paginator';
import { UserAdministrativeOperationsPerformerFactory } from 'src/app/features/admin-users-management/utils/userAdministrativeOperationsPerformers';
import {
  UserAdministrativeOperationDetailsIf,
  UserAdministrativeOperationPerformerInterface,
} from 'src/app/features/admin-users-management/utils/userAdministrativeOperations.interface';

@Component({
  selector: 'app-admin-users-management',
  templateUrl: './admin-users-management.component.html',
  styleUrls: ['./admin-users-management.component.scss'],
  providers: [UserAdministrativeOperationsPerformerFactory],
})
export class AdminUsersManagementComponent
  implements OnInit, AfterViewInit, OnDestroy
{
  @ViewChild('filterPanel')
  private filterPanel?: UsersFilterPaneComponent;

  private subscriptions = new Subscription();
  private _users: AdministrativeUserDetails[] = [];
  private _availablePageSizes = [5, 10, 15];
  private _currentPage = 0;
  private _pageSize = this._availablePageSizes[0];
  private _totalElements = 0;

  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private performerFactory: UserAdministrativeOperationsPerformerFactory
  ) {}

  ngOnInit(): void {}

  ngAfterViewInit() {
    this.makeSubscriptions();
    this.loadInitialData();
  }
  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  public get users(): AdministrativeUserDetails[] {
    return this._users;
  }

  public get availablePageSizes(): number[] {
    return this._availablePageSizes;
  }

  public get currentPage(): number {
    return this._currentPage;
  }

  public get pageSize(): number {
    return this._pageSize;
  }

  public get totalElements(): number {
    return this._totalElements;
  }

  public onPaginatorValueChange(pageEvent: PageEvent) {
    // if page size changes we need to "reset" paginator
    if (pageEvent.pageSize != this.pageSize) {
      this._currentPage = 0;
      this._pageSize = pageEvent.pageSize;
    } else {
      this._currentPage = pageEvent.pageIndex;
    }
    const selectedFilters = this.filterPanel?.getQuerySearchParams();
    this.loadData(selectedFilters);
  }

  public onAdministrativeAction(
    operationDetails: UserAdministrativeOperationDetailsIf
  ) {
    const operationPerformer =
      this.performerFactory.getOperationPerformer(operationDetails);
    const operationDialogRef =
      operationPerformer?.getDialogRef(operationDetails);
    operationDialogRef
      ?.afterClosed()
      .subscribe(result =>
        this.onOperationDialogClose(
          operationPerformer!,
          result,
          operationDetails
        )
      );
  }

  private onOperationDialogClose(
    operationPerformer: UserAdministrativeOperationPerformerInterface,
    dialogResult: any,
    operationDetails: UserAdministrativeOperationDetailsIf
  ) {
    const operationObs = operationPerformer?.getOperationObservable(
      dialogResult,
      operationDetails
    );
    if (isNil(operationObs)) {
      return;
    }
    const queryParams = this.filterPanel?.getQuerySearchParams();
    operationObs.subscribe(this.loadData.bind(this, queryParams));
  }

  private makeSubscriptions() {
    this.subscriptions.add(
      this.filterPanel?.searchSubmitEvent.subscribe(
        this.onSearchEvent.bind(this)
      )
    );
    this.subscriptions.add(
      this.filterPanel?.filterClearEvent.subscribe(this.onClearEvent.bind(this))
    );
  }

  private onSearchEvent(queryParams: AdminUsersManagementQueryParamsIf) {
    const params: Params = new HttpParams();
    if (!isNil(queryParams.firstName) && queryParams.firstName !== '') {
      params['t'] = queryParams.firstName;
    }
    if (!isNil(queryParams.roleIds) && queryParams.roleIds.length > 0) {
      params['r'] = queryParams.roleIds;
    }
    updateActiveUrl(this.router, this.activatedRoute, params);
    this.loadData(queryParams);
  }

  private onClearEvent() {
    updateActiveUrl(this.router, this.activatedRoute, {});
    this.loadData();
  }

  private loadInitialData() {
    const initialQueryParams = this.filterPanel?.getQuerySearchParams();
    this.loadData(initialQueryParams);
  }

  private loadData(queryParams?: AdminUsersManagementQueryParamsIf) {
    this.userService
      .getAdministrativeUserDetails(
        queryParams,
        this._currentPage,
        this._pageSize
      )
      .subscribe(result => {
        this._users = result.content.map(AdministrativeUserDetails.fromPayload);
        this._totalElements = result.totalElements;
      });
  }
}
