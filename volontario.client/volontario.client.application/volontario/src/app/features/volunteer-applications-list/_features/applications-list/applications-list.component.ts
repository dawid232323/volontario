import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { User } from 'src/app/core/model/user.model';
import { ApplicationBaseInfo } from 'src/app/core/model/application.model';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-applications-list',
  templateUrl: './applications-list.component.html',
  styleUrls: ['./applications-list.component.scss'],
})
export class ApplicationsListComponent implements OnInit {
  public visibleColumns: string[] = [
    'offerTitle',
    'participationMotivation',
    'state',
  ];
  private _loggedUser?: User;
  private _applications: ApplicationBaseInfo[] = [];

  private _dataSource = new MatTableDataSource<ApplicationBaseInfo>();

  constructor() {}

  ngOnInit(): void {}

  @Output() listRowClicked = new EventEmitter<ApplicationBaseInfo>();

  @Input()
  public get loggedUser(): User | undefined {
    return this._loggedUser;
  }

  public set loggedUser(user: User | undefined) {
    this._loggedUser = user;
  }

  @Input()
  public get applications(): ApplicationBaseInfo[] {
    return this._applications;
  }

  public set applications(applications: ApplicationBaseInfo[]) {
    this._applications = applications;
    this.dataSource = new MatTableDataSource<ApplicationBaseInfo>(
      this._applications
    );
  }

  public get dataSource(): MatTableDataSource<ApplicationBaseInfo> {
    return this._dataSource;
  }

  public set dataSource(dataSource: MatTableDataSource<ApplicationBaseInfo>) {
    this._dataSource = dataSource;
  }

  onRowClick(selectedRow: ApplicationBaseInfo) {
    this.listRowClicked.emit(selectedRow);
  }
}
