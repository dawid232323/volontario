import { Component, OnDestroy, OnInit } from '@angular/core';
import { AdvertisementService } from '../../core/service/advertisement.service';
import { AdvertisementDto } from '../../core/model/advertisement.model';
import { UserRoleEnum } from '../../core/model/user-role.model';
import { User } from '../../core/model/user.model';
import { UserService } from '../../core/service/user.service';
import { forkJoin, Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { isNil } from 'lodash';
import { OfferApplicationService } from 'src/app/core/service/offer-application.service';

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
  private _hasAppliedForOffer = true;
  private _applicationState?: string;
  private _advertisementId = <number>this.route.snapshot.params['adv_id'];

  constructor(
    private advertisementService: AdvertisementService,
    private offerApplicationService: OfferApplicationService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
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
      this.determineIfUserCanApply();
    });
  }

  public convertDate(getDate: any | undefined) {
    return getDate?.split('T')[0];
  }

  public get canManageOffer(): boolean {
    return this._canManageOffer;
  }

  public get canApplyForOffer(): boolean {
    if (this._hasAppliedForOffer) {
      return false;
    }
    if (this.loggedUser?.hasUserRole(UserRoleEnum.Volunteer)) {
      return true;
    }
    return false;
  }

  public get applicationState(): string {
    return `Zaaplikowałeś już na tę ofertę. Aktualnie jest ona w stanie: ${this._applicationState?.toLocaleLowerCase()}`;
  }

  public onEditButtonClicked() {
    if (this.canManageOffer) {
      return this.router.navigate([
        'advertisement',
        'edit',
        this._advertisementId,
      ]);
    }
    return;
  }

  public onGoBackToOfferListButtonClicked() {
    return this.router.navigate(['advertisement', 'list']);
  }

  private determineIfUserCanApply() {
    if (this?.loggedUser?.hasUserRole(UserRoleEnum.Volunteer)) {
      this.offerApplicationService
        .checkApplicationState(this.loggedUser?.id, this._advertisementId)
        .subscribe({
          next: result => {
            this._hasAppliedForOffer = result.applied;
            this._applicationState = result.state;
          },
        });
    }
  }

  public onApplyButtonCLicked() {
    if (!this.canApplyForOffer) {
      return;
    }
    this.router.navigate(['apply'], { relativeTo: this.route });
  }

  protected readonly UserRoleEnum = UserRoleEnum;

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  protected readonly isNil = isNil;
}
