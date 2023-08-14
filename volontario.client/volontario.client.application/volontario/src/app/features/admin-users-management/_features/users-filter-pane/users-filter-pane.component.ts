import {
  Component,
  ElementRef,
  EventEmitter,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FilterPaneInterface } from 'src/app/core/interface/filter-pane.interface';
import { MatButton } from '@angular/material/button';
import { Subscription } from 'rxjs';
import { UserRoles } from 'src/app/core/model/user-role.model';
import { isNil } from 'lodash';
import { ActivatedRoute, Router } from '@angular/router';

export interface AdminUsersManagementQueryParamsIf {
  firstName?: string;
  lastName?: string;
  email?: string;
  roleIds?: number[];
}

@Component({
  selector: 'app-users-filter-pane',
  templateUrl: './users-filter-pane.component.html',
  styleUrls: ['./users-filter-pane.component.scss'],
})
export class UsersFilterPaneComponent
  implements
    OnInit,
    OnDestroy,
    FilterPaneInterface<AdminUsersManagementQueryParamsIf>
{
  @ViewChild('clearButton')
  clearButton: ElementRef<MatButton> | undefined;

  @ViewChild('searchButton')
  searchButton: ElementRef<MatButton> | undefined;

  public filterClearEvent: EventEmitter<void> = new EventEmitter<void>();
  public searchSubmitEvent: EventEmitter<AdminUsersManagementQueryParamsIf> =
    new EventEmitter<AdminUsersManagementQueryParamsIf>();
  public selectedRoles: number[] = [];
  public typedText: string | undefined;

  private subscriptions = new Subscription();

  constructor(private activatedRoute: ActivatedRoute) {}

  public ngOnInit() {
    this.checkInitialQueryParams();
  }

  public ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  public onFilterClear(): void {
    this.clearTextField();
    this.selectedRoles = [];
    this.filterClearEvent.emit();
  }

  public onSearchSubmit(): void {
    const queryParams = this.getQuerySearchParams();
    this.searchSubmitEvent.emit(queryParams);
  }

  public clearTextField() {
    this.typedText = undefined;
  }

  public get canClearFilters(): boolean {
    return this.hasTypedText || this.selectedRoles.length > 0;
  }

  public get hasTypedText(): boolean {
    return !isNil(this.typedText) && this.typedText !== '';
  }

  private checkInitialQueryParams() {
    const paramTypedText = this.activatedRoute.snapshot.queryParams['t'];
    const paramRoleIds = this.activatedRoute.snapshot.queryParams['r'];
    this.setFilterValues(paramTypedText, paramRoleIds);
  }

  public setFilterValues(typedText: string, selectedRoles: number[]) {
    if (!isNil(typedText) && typedText !== '') {
      this.typedText = typedText;
    }
    if (!isNil(selectedRoles) && selectedRoles.length > 0) {
      this.selectedRoles = [...selectedRoles].map(role => Number(role));
    }
  }

  public getQuerySearchParams(): AdminUsersManagementQueryParamsIf {
    const queryParams: AdminUsersManagementQueryParamsIf = {};
    if (this.hasTypedText) {
      queryParams.lastName = this.typedText;
      queryParams.firstName = this.typedText;
      queryParams.email = this.typedText;
    }
    if (this.selectedRoles.length > 0) {
      queryParams.roleIds = this.selectedRoles;
    }
    return queryParams;
  }

  protected readonly UserRoles = UserRoles;
}
