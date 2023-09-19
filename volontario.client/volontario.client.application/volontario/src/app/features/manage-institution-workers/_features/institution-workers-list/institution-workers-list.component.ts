import { AfterViewInit, Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { InstitutionWorker, User } from 'src/app/core/model/user.model';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { UserRoles } from 'src/app/core/model/user-role.model';
import { Institution } from 'src/app/core/model/institution.model';
import { InstitutionWorkerRoleChangeTypeEnum } from 'src/app/core/service/institution.service';

export interface WorkerRoleChangeEventIf {
  worker: InstitutionWorker;
  operation: InstitutionWorkerRoleChangeTypeEnum;
}

@Component({
  selector: 'app-institution-workers-list',
  templateUrl: './institution-workers-list.component.html',
  styleUrls: ['./institution-workers-list.component.scss'],
})
export class InstitutionWorkersListComponent implements AfterViewInit {
  private _institutionWorkers: InstitutionWorker[] = [];

  @Input() public institutionData?: Institution;
  @Input() public user?: User;

  @Output() public workerRoleChangeEvent: EventEmitter<WorkerRoleChangeEventIf>;

  @ViewChild(MatPaginator) paginator?: MatPaginator;
  @ViewChild(MatSort) sort?: MatSort;

  private _displayedColumns: string[] = ['firstName', 'lastName', 'role', 'menu'];
  private readonly _dataSource: MatTableDataSource<InstitutionWorker>;
  constructor() {
    this._dataSource = new MatTableDataSource<InstitutionWorker>([]);
    this.workerRoleChangeEvent = new EventEmitter<WorkerRoleChangeEventIf>();
  }

  @Input()
  public get institutionWorkers(): InstitutionWorker[] {
    return this._institutionWorkers;
  }

  public set institutionWorkers(workers: InstitutionWorker[]) {
    this._institutionWorkers = workers;
    this._dataSource.data = workers;
  }

  public get dataSource(): MatTableDataSource<InstitutionWorker> {
    return this._dataSource;
  }

  get displayedColumns(): string[] {
    return this._displayedColumns;
  }

  ngAfterViewInit() {
    this._dataSource.sort = this.sort!;
    this._dataSource.paginator = this.paginator!;
  }

  public getChangeRoleActionTitle(worker: InstitutionWorker): string {
    if (worker.role?.toLowerCase() === UserRoles.InstitutionWorker.name.toLowerCase()) {
      return 'Nadaj uprawnienia administratora instytucji';
    }
    return 'Nadaj uprawnienia pracownika instytucji';
  }

  public canChangeRoles(worker: InstitutionWorker): boolean {
    const adminsCount = this._institutionWorkers.reduce((accumulator, institutionWorker) => {
      if (institutionWorker.role?.toLowerCase() === UserRoles.InstitutionAdmin.name.toLowerCase()) {
        accumulator = accumulator + 1;
      }
      return accumulator;
    }, 0);
    if (this.user?.id === worker.id) {
      return false;
    }
    return !(adminsCount === 1 && worker.role?.toLowerCase() === UserRoles.InstitutionAdmin.name.toLowerCase());
  }

  public onChangeRoleActionClicked(worker: InstitutionWorker) {
    if (!this.canChangeRoles(worker)) {
      return;
    }
    if (worker.role?.toLowerCase() === UserRoles.InstitutionAdmin.name.toLowerCase()) {
      this.workerRoleChangeEvent.emit({ worker: worker, operation: InstitutionWorkerRoleChangeTypeEnum.MarkAsEmployee });
      return;
    }
    this.workerRoleChangeEvent.emit({ worker: worker, operation: InstitutionWorkerRoleChangeTypeEnum.MarkAsAdmin });
  }

  public addWorker(worker: InstitutionWorker) {
    this._institutionWorkers.push(worker);
    this._dataSource.data = this._institutionWorkers;
    this._dataSource._updateChangeSubscription();
  }
}
