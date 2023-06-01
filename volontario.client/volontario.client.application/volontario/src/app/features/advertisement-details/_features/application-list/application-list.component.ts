import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ApplicationDetails } from 'src/app/core/model/application.model';
import { MatTableDataSource } from '@angular/material/table';
import {
  BaseApplicationFiltersIf,
  OfferApplicationService,
} from 'src/app/core/service/offer-application.service';
import { MatSort } from '@angular/material/sort';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-application-list',
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.scss'],
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

  // TODO add navigation to single application view
  public onRowClick(applicationId: number) {}

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
