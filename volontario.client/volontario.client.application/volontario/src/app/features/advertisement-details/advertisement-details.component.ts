import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { AdvertisementService } from '../../core/service/advertisement.service';
import { AdvertisementDto } from '../../core/model/advertisement.model';
import { UserRoleEnum } from '../../core/model/user-role.model';
import { User } from '../../core/model/user.model';
import { UserService } from '../../core/service/user.service';
import { forkJoin, Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { isNil } from 'lodash';
import { ApplicationBaseInfo } from 'src/app/core/model/application.model';
import {
  BaseApplicationFiltersIf,
  OfferApplicationService,
} from 'src/app/core/service/offer-application.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-advertisement-details',
  templateUrl: './advertisement-details.component.html',
  styleUrls: ['./advertisement-details.component.scss'],
})
export class AdvertisementDetailsComponent implements OnInit, OnDestroy {
  public advertisementData: AdvertisementDto | null = null;
  public loggedUser?: User;
  private subscriptions = new Subscription();
  private _canManageOffer = false;
  private _advertisementId = <number>this.route.snapshot.params['adv_id'];

  @ViewChild(MatSort) sort?: MatSort;

  constructor(
    private advertisementService: AdvertisementService,
    private offerApplicationService: OfferApplicationService,
    private userService: UserService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    forkJoin([
      this.advertisementService.getAdvertisement(this._advertisementId),
      this.userService.getCurrentUserData(),
    ]).subscribe(([advertisement, user]) => {
      this.advertisementData = advertisement;
      this.loggedUser = user;
      this._canManageOffer = this.advertisementService.canManageOffer(
        this.advertisementData,
        this.loggedUser
      );
    });
  }

  public convertDate(getDate: any | undefined) {
    return getDate?.split('T')[0];
  }

  public get canManageOffer(): boolean {
    return this._canManageOffer;
  }

  protected readonly UserRoleEnum = UserRoleEnum;

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  protected readonly isNil = isNil;
}
