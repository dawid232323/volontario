import { Component, OnInit, ViewChild } from '@angular/core';
import { ApplicationDetails } from 'src/app/core/model/application.model';
import { MatTableDataSource } from '@angular/material/table';
import {
  BaseApplicationFiltersIf,
  OfferApplicationService,
} from 'src/app/core/service/offer-application.service';
import { MatSort } from '@angular/material/sort';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
import {
  ApplicationActionIf,
  ApplicationActionTypeEnum,
} from 'src/app/core/interface/application.interface';

@Component({
  selector: 'app-application-list',
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition(
        'expanded <=> collapsed',
        animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')
      ),
    ]),
  ],
})
export class ApplicationListComponent implements OnInit {
  @ViewChild(MatSort) sort?: MatSort;

  private _visibleColumns = [
    'firstName',
    'lastName',
    'participationMotivation',
    'state',
    'starred',
  ];
  private _advertisementId?: number;
  private _offerApplications: ApplicationDetails[] = [];
  private _dataSource: MatTableDataSource<ApplicationDetails> =
    new MatTableDataSource<ApplicationDetails>();

  public columnsToDisplayWithExpand = [...this._visibleColumns, 'expand'];
  public expandedElementId?: null | number;
  public expandedElementIds = new Set<number>();
  constructor(
    private offerApplicationService: OfferApplicationService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this._advertisementId = <number>this.route.snapshot.params['adv_id'];
    this.getOfferApplications();
  }

  public get visibleColumns(): string[] {
    return this._visibleColumns;
  }

  public get offerApplications(): ApplicationDetails[] {
    return this._offerApplications;
  }

  public get dataSource(): MatTableDataSource<ApplicationDetails> {
    return this._dataSource;
  }

  public onIsStarredClick(applicationId: number, isStarred: boolean) {
    const rowIndex = this._dataSource.data.findIndex(
      value => value.id === applicationId
    );
    if (rowIndex === -1) {
      return;
    }
    this._dataSource.data[rowIndex].starred = !isStarred;
    this._dataSource.data = this._dataSource._orderData(this.dataSource.data);
    const applicationStarredCallback: (id: number) => Observable<any> =
      isStarred
        ? this.offerApplicationService.markApplicationUnStarred.bind(
            this.offerApplicationService
          )
        : this.offerApplicationService.markApplicationStarred.bind(
            this.offerApplicationService
          );
    applicationStarredCallback(applicationId).subscribe({
      error: err => {
        throw new Error(err.error);
      },
    });
  }

  public onRowClick(applicationId: number) {
    if (this.expandedElementIds.has(applicationId)) {
      this.expandedElementIds.delete(applicationId);
      return;
    }
    this.expandedElementIds.add(applicationId);
  }

  public isElementExpanded(applicationId: number): boolean {
    return this.expandedElementIds.has(applicationId);
  }

  public onApplicationStatusChange(event: ApplicationActionIf) {
    this.onAfterApplicationStatusChange(event);
    this.offerApplicationService
      .changeApplicationState(
        event.application.id,
        event.actionType,
        event.actionReason
      )
      .subscribe({
        error: err => {
          throw new Error(err);
        },
      });
  }

  private onAfterApplicationStatusChange(event: ApplicationActionIf) {
    if (event.actionType !== ApplicationActionTypeEnum.Reject) {
      return;
    }
    const rowIndex = this._dataSource.data.findIndex(
      value => value.id === event.application.id
    );
    if (rowIndex === -1) {
      return;
    }
    this._dataSource.data[rowIndex].decisionReason = event.actionReason!;
    this._dataSource.data = this._dataSource._orderData(this.dataSource.data);
  }

  private getOfferApplications() {
    const filters: BaseApplicationFiltersIf = {
      offerId: this._advertisementId,
    };
    this.offerApplicationService.getApplicationDetailsList(filters).subscribe({
      next: result => {
        this._offerApplications = result.content;
        this._dataSource = new MatTableDataSource<ApplicationDetails>(
          this.offerApplications
        );
        this._dataSource.sort = this.sort!;
      },
    });
  }
}
