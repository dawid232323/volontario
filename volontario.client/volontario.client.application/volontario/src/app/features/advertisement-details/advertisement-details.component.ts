import { Component, OnDestroy, OnInit } from '@angular/core';
import { AdvertisementService } from '../../core/service/advertisement.service';
import { AdvertisementDto } from '../../core/model/advertisement.model';
import { UserRoleEnum } from '../../core/model/user-role.model';
import { User } from '../../core/model/user.model';
import { UserService } from '../../core/service/user.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { isNil } from 'lodash';

@Component({
  selector: 'app-advertisement-details',
  templateUrl: './advertisement-details.component.html',
  styleUrls: ['./advertisement-details.component.scss'],
})
export class AdvertisementDetailsComponent implements OnInit, OnDestroy {
  public advertisementData: AdvertisementDto | null = null;
  public loggedUser?: User;
  private subscriptions = new Subscription();
  constructor(
    private advertisementService: AdvertisementService,
    private userService: UserService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.advertisementService
      .getAdvertisement(this.route.snapshot.params['adv_id'])
      .subscribe(result => {
        this.advertisementData = result;
      });

    this.userService.getCurrentUserData().subscribe(result => {
      this.loggedUser = result;
    });
  }

  public convertDate(getDate: any | undefined) {
    return getDate.split('T')[0];
  }

  protected readonly UserRoleEnum = UserRoleEnum;

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  protected readonly isNil = isNil;
}
