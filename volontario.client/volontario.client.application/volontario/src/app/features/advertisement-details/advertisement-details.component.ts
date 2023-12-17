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
import { formatDate } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import {
  VolConfirmPresenceInitialData,
  VolunteerConfirmPresenceComponent,
} from 'src/app/features/advertisement-details/_features/volunteer-confirm-presence/volunteer-confirm-presence.component';
import {
  InstitutionVoluntaryPresenceModel,
  PresenceStateEnum,
  VolunteerPresenceModel,
} from 'src/app/core/model/offer-presence.model';
import { OfferPresenceHandler } from 'src/app/features/advertisement-details/offer-presence.handler';
import { InstitutionService } from '../../core/service/institution.service';
import {
  ConfirmationAlertComponent,
  ConfirmationAlertInitialData,
  ConfirmationAlertResult,
  ConfirmationAlertResultIf,
} from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';

@Component({
  selector: 'app-advertisement-details',
  templateUrl: './advertisement-details.component.html',
  styleUrls: ['./advertisement-details.component.scss'],
  providers: [OfferPresenceHandler],
})
export class AdvertisementDetailsComponent implements OnInit, OnDestroy {
  public advertisementData: AdvertisementDto | null = null;
  public loggedUser?: User;
  private subscriptions = new Subscription();
  private _canManageOffer = false;
  private _isOfferPresenceReady = false;
  private _canConfirmPresence = false;
  private _hasAppliedForOffer = true;
  private _canChangePresenceDecision = false;
  private _hasVolunteerAlreadyMadePresenceDecision = false;
  private _applicationState?: string;
  private _advertisementId = <number>this.route.snapshot.params['adv_id'];
  private _isLoadingData = true;

  private _volunteerPresenceState?: VolunteerPresenceModel;
  private _institutionPresenceState?: InstitutionVoluntaryPresenceModel;
  private _hasNotAcceptedApplications: boolean = true;

  constructor(
    private advertisementService: AdvertisementService,
    private offerApplicationService: OfferApplicationService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private matDialog: MatDialog,
    private presenceHandler: OfferPresenceHandler,
    private institutionService: InstitutionService
  ) {}

  ngOnInit(): void {
    forkJoin([
      this.advertisementService.getAdvertisement(this._advertisementId),
      this.userService.getCurrentUserData(),
      this.presenceHandler.resolveIsOfferPresenceReady(this._advertisementId),
    ]).subscribe(([advertisement, user, isPresenceReady]) => {
      this.advertisementData = advertisement;
      this.loggedUser = user;
      this._isOfferPresenceReady = isPresenceReady;
      this._canManageOffer = this.advertisementService.canManageOffer(
        this.advertisementData,
        this.loggedUser
      );
      this.determineIfUserCanApply();
    });
  }

  public convertDate(getDate: any | undefined) {
    if (isNil(getDate)) {
      return getDate;
    }
    return formatDate(getDate, 'dd-MM-yyyy', 'en-EN');
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

  public onConfirmPresenceButtonCLicked() {
    if (!this.canConfirmPresence) {
      return;
    }
    if (!this.loggedUser?.hasUserRole(UserRoleEnum.Volunteer)) {
      return this.router.navigate(
        [
          'institution',
          this.advertisementData?.institutionId,
          'confirm-presence',
        ],
        {
          queryParams: {
            o: this._advertisementId,
          },
        }
      );
    }
    const initialData: VolConfirmPresenceInitialData = {
      canPostponeReminder: this._volunteerPresenceState!.canPostponeReminder,
    };
    const dialogRef = this.matDialog.open(VolunteerConfirmPresenceComponent, {
      data: initialData,
    });
    return dialogRef
      .afterClosed()
      .subscribe(this.handleVolunteerConfirmation.bind(this));
  }

  public getDecisionChangeLabel(): string | null {
    return this.presenceHandler.resolveDecisionChangeLabel(
      this._canConfirmPresence,
      this._canChangePresenceDecision,
      this._canManageOffer,
      this.loggedUser!,
      this._volunteerPresenceState,
      this._institutionPresenceState
    );
  }

  public getInstitutionConfirmPresenceButtonLabel() {
    return this.presenceHandler.resolveInstitutionConfirmButtonLabel(
      this._canChangePresenceDecision,
      this._institutionPresenceState
    );
  }

  public onShowHideOffer() {
    const initialData: ConfirmationAlertInitialData = {
      confirmationMessage: this.getHideOfferMessage(),
    };
    const dialogRef = this.matDialog.open(ConfirmationAlertComponent, {
      data: initialData,
    });
    dialogRef.afterClosed().subscribe((result: ConfirmationAlertResultIf) => {
      if (result.confirmationAlertResult !== ConfirmationAlertResult.Accept) {
        return;
      }
      this.onAfterShowHideOffer();
    });
  }

  public getVolunteerConfirmPresenceButtonLabel() {
    return this.presenceHandler.resolveVolunteerConfirmButtonLabel(
      this._canChangePresenceDecision,
      this._volunteerPresenceState
    );
  }

  private handleVolunteerConfirmation(decision: PresenceStateEnum) {
    if (isNil(decision)) {
      return;
    }
    if (decision === PresenceStateEnum.Unresolved) {
      this.handlePostponeConfirmation();
    } else {
      this.advertisementService
        .determineVoluntaryPresenceForVolunteer(
          this.loggedUser!.id,
          this._advertisementId,
          decision
        )
        .subscribe(() => this.getVolunteerPresenceState());
    }
  }

  private handlePostponeConfirmation() {
    this.advertisementService
      .postponePresenceConfirmationVolunteer(
        this.loggedUser!.id,
        this._advertisementId
      )
      .subscribe(() => this.getVolunteerPresenceState());
  }

  private determineIfUserCanApply() {
    if (this?.loggedUser?.hasUserRole(UserRoleEnum.Volunteer)) {
      this.offerApplicationService
        .checkApplicationState(this.loggedUser?.id, this._advertisementId)
        .subscribe({
          next: result => {
            this._hasAppliedForOffer = result.applied;
            this._applicationState = result.state;
            this.determineIfUserCanConfirmPresence();
            this.getVolunteerPresenceState();
          },
        });
    }
    this.determineIfUserCanConfirmPresence();
    this.getInstitutionPresenceState();
  }

  private determineIfUserCanConfirmPresence() {
    this._canConfirmPresence = this.presenceHandler.canUserConfirmPresence(
      this._applicationState,
      this.canManageOffer
    );
  }

  private getVolunteerPresenceState(): void {
    if (!this._canConfirmPresence) {
      this._isLoadingData = false;
      return;
    }
    this.presenceHandler
      .resolveVolunteerPresenceState(this._advertisementId, this.loggedUser!.id)
      .subscribe(result => {
        this._canChangePresenceDecision = result.canChangeDecision;
        this._volunteerPresenceState = result.presenceState;
        if (!isNil(result.canConfirmPresence)) {
          this._canConfirmPresence = result.canConfirmPresence;
        }
        this._isLoadingData = false;
      });
  }

  private getInstitutionPresenceState() {
    if (!this._canConfirmPresence) {
      this._isLoadingData = false;
      return;
    }
    this.presenceHandler
      .resolveInstitutionPresenceState(this._advertisementId)
      .subscribe(presenceState => {
        this._canChangePresenceDecision = presenceState.canChangeDecision;
        this._institutionPresenceState = presenceState.presenceState;
        this._hasNotAcceptedApplications =
          presenceState.hasNotAcceptedApplications;
        this._isLoadingData = false;
      });
  }

  private getHideOfferMessage() {
    if (this.advertisementData?.hidden) {
      return 'Pokazanie ogłoszenia zmieni jego widoczność i sprawi, że będzie widoczne dla wszystkich użytkowników systemu.';
    }
    return 'Ukrycie ogłoszenia zmieni jego widoczność i sprawi, że przestanie być widoczne dla wszystkich użytkowników systemu.';
  }

  private onAfterShowHideOffer() {
    this._isLoadingData = true;
    this.advertisementService
      .changeOfferVisibility(this._advertisementId, {
        isHidden: !this.advertisementData!.hidden!,
      })
      .subscribe(result => {
        this.advertisementData!.hidden! = result.isHidden;
        this._isLoadingData = false;
      });
  }

  public onApplyButtonCLicked() {
    if (!this.canApplyForOffer) {
      return;
    }
    this.router.navigate(['apply'], { relativeTo: this.route });
  }

  public isWorkingForInstitutionWhichOwnsOffer(): boolean {
    return (
      !isNil(this.loggedUser?.institution) &&
      this.loggedUser?.institution.id === this.advertisementData?.institutionId
    );
  }

  public isInstitutionActive(): boolean {
    return (
      !isNil(this.advertisementData?.institutionId) &&
      this.canManageOffer &&
      (this.loggedUser?.institution?.active || false)
    );
  }

  public shouldDisableInstitutionPresence(): boolean {
    return this._hasNotAcceptedApplications && this._isOfferPresenceReady;
  }

  public get canConfirmPresence(): boolean {
    return this._canConfirmPresence;
  }

  public get hasVolunteerAlreadyMadePresenceDecision(): boolean {
    return this._volunteerPresenceState?.hasAlreadyMadeDecision || false;
  }

  public get hasInstitutionAlreadyMadePresenceDecision(): boolean {
    return this._institutionPresenceState?.wasPresenceConfirmed || false;
  }

  public get isLoadingData(): boolean {
    return this._isLoadingData;
  }

  public get canChangePresenceDecision(): boolean {
    return this._canChangePresenceDecision;
  }

  public get shouldShowBenefits(): boolean {
    return (
      (!isNil(this.advertisementData?.offerBenefitIds) &&
        this.advertisementData!.offerBenefitIds.length > 0) ||
      !isNil(this.advertisementData?.otherBenefits)
    );
  }

  protected readonly UserRoleEnum = UserRoleEnum;

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  protected readonly isNil = isNil;
}
