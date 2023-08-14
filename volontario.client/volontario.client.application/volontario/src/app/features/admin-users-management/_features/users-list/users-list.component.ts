import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AdministrativeUserDetails } from 'src/app/core/model/user.model';
import { MatTableDataSource } from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import {
  UserAdministrativeOperationDetailsIf,
  UserAdministrativeOperationType,
} from 'src/app/features/admin-users-management/utils/userAdministrativeOperations.interface';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss'],
})
export class UsersListComponent implements OnInit {
  @Input() availablePageSizes: number[] = [];
  @Input() totalElements: number = 0;
  @Input() currentPageIndex: number = 0;
  @Input() currentPageSize: number = 0;

  @Output() paginatorChange = new EventEmitter<PageEvent>();
  @Output() administrativeAction =
    new EventEmitter<UserAdministrativeOperationDetailsIf>();

  private _usersList: AdministrativeUserDetails[] = [];

  private _visibleColumns = ['fullName', 'roles', 'isActive', 'actions'];
  private readonly _dataSource: MatTableDataSource<AdministrativeUserDetails>;
  constructor() {
    this._dataSource = new MatTableDataSource<AdministrativeUserDetails>();
  }

  ngOnInit(): void {}

  @Input()
  public get usersList(): AdministrativeUserDetails[] {
    return this._usersList;
  }

  public set usersList(value: AdministrativeUserDetails[]) {
    this._usersList = value;
    this._dataSource.data = this._usersList;
  }

  public get visibleColumns(): string[] {
    return this._visibleColumns;
  }

  public get dataSource(): MatTableDataSource<AdministrativeUserDetails> {
    return this._dataSource;
  }

  public onPageEvent(pageEvent: PageEvent) {
    this.paginatorChange.emit(pageEvent);
  }

  public onRowMenuClick(
    row: AdministrativeUserDetails,
    operationType: UserAdministrativeOperationType
  ) {
    const operationDetails: UserAdministrativeOperationDetailsIf = {
      operationType: operationType,
      userDetails: row,
    };
    this.administrativeAction.emit(operationDetails);
  }

  protected readonly undefined = undefined;
  protected readonly UserAdministrativeOperationType =
    UserAdministrativeOperationType;
}
