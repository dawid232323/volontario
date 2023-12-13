import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { UserService } from 'src/app/core/service/user.service';
import { User, UserProfile } from 'src/app/core/model/user.model';
import { ActivatedRoute, Params, Router } from '@angular/router';
import {
  firstValueFrom,
  forkJoin,
  from,
  Observable,
  of,
  Subscription,
} from 'rxjs';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import {
  UserExperienceDescriptionConfigProvider,
  UserInterestsConfigProvider,
} from 'src/app/features/user-details/_features/basic-user-details/_features/single-field-user-details-form/single-user-details-config.provider';

import { isNil } from 'lodash';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorDialogService } from 'src/app/core/service/error-dialog.service';
import {
  EvaluationDto,
  OffersToEvaluateIf,
  UserEvaluation,
} from 'src/app/core/model/evaluation.model';
import { MatTabGroup } from '@angular/material/tabs';
import { updateActiveUrl } from 'src/app/utils/url.util';
import { EvaluationService } from 'src/app/core/service/evaluation.service';
import { EvaluationModalData } from 'src/app/shared/features/evaluation-modal/evaluation-modal.component';

enum SelectedTabIndex {
  BasicData,
  Evaluation,
}

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss'],
})
export class UserDetailsComponent implements OnInit, OnDestroy {
  @ViewChild('userTabs', { static: false }) private userTabs!: MatTabGroup;

  private _userProfile?: UserProfile;
  private _loggedUser?: User;
  private _userId: number;

  private subscriptions = new Subscription();
  private _isLoadingData = true;
  private _canSeePersonalInfo = false;
  public _userProfilePicture?: string;
  private _evaluations?: UserEvaluation;
  private _offersToEvaluate?: OffersToEvaluateIf;

  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private errorDialogService: ErrorDialogService,
    private router: Router,
    private evaluationService: EvaluationService
  ) {
    this._userId = +this.activatedRoute.snapshot.params['user_id'];
  }

  ngOnInit(): void {
    this.makeSubscriptions();
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  public get userProfile(): UserProfile | undefined {
    return this._userProfile;
  }

  public get canEditProfile(): boolean {
    return (
      this.canQuicklyChangeData ||
      this._loggedUser?.hasUserRoles([
        UserRoleEnum.Moderator,
        UserRoleEnum.Admin,
      ]) ||
      false
    );
  }

  public get canQuicklyChangeData(): boolean {
    if (this._loggedUser?.id === this._userProfile?.id) {
      return true;
    }
    return (
      this._loggedUser?.hasUserRoles([
        UserRoleEnum.Admin,
        UserRoleEnum.Moderator,
      ]) || false
    );
  }

  public get isLoadingData(): boolean {
    return this._isLoadingData;
  }

  public onUserInterestsChange(
    interestsConfigProvider: UserInterestsConfigProvider
  ) {
    if (isNil(interestsConfigProvider.initialData)) {
      return;
    }
    this.performQuickUpdate(
      this._userProfile?.id!,
      interestsConfigProvider.initialData!,
      this.userService.updateVolunteerInterests.bind(this.userService)
    );
  }

  public onUserExperienceDescriptionChange(
    experienceDescriptionConfProvider: UserExperienceDescriptionConfigProvider
  ) {
    if (isNil(experienceDescriptionConfProvider.initialData)) {
      return;
    }
    this.performQuickUpdate(
      this._userProfile?.id!,
      experienceDescriptionConfProvider.initialData!,
      this.userService.updateVolunteerExperienceDescription.bind(
        this.userService
      )
    );
  }

  public canEditAdditionalVolunteerInfo(): boolean {
    if (this._loggedUser?.id === this.userProfile?.id) {
      return this._loggedUser?.hasUserRole(UserRoleEnum.Volunteer) || false;
    }
    if (
      this?._loggedUser?.hasUserRoles([
        UserRoleEnum.Admin,
        UserRoleEnum.Moderator,
      ])
    ) {
      return true;
    }
    return false;
  }

  public async onPictureChanged(newFile: File) {
    this._isLoadingData = true;
    forkJoin([
      from(this.userService.getImage(newFile)),
      this.userService.saveUserImage(this._userId, newFile),
    ]).subscribe({
      next: ([imageData, _]) => {
        this._userProfilePicture = <string>imageData;
      },
      error: (error: HttpErrorResponse) => {
        this.errorDialogService.openDefaultErrorDialog({ error });
        this._isLoadingData = false;
      },
      complete: () => {
        this._isLoadingData = false;
      },
    });
  }

  public onEvaluationPerformed($event: EvaluationModalData) {
    this._isLoadingData = true;
    const requestBody: EvaluationDto = {
      volunteerId: this._userId,
      offerId: $event.selectedOfferId!,
      rating: $event.evaluationValue!,
      ratingReason: $event.comment,
    };
    this.evaluationService
      .rateVolunteer(this._userId, requestBody)
      .subscribe(async () => {
        const result = await firstValueFrom(
          this.resolveUserEvaluation(this._loggedUser!)
        );
        this._offersToEvaluate = result.canEvaluate;
        this._evaluations = result.evaluation;
        this._isLoadingData = false;
      });
  }

  public onSelectTabIndexChange($event: number) {
    updateActiveUrl(this.router, this.activatedRoute, { tab: $event }, 'merge');
  }

  private makeSubscriptions() {
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(this.onRouteParamsChange.bind(this))
    );
  }

  private onRouteParamsChange(params: Params) {
    this._isLoadingData = true;
    this._userId = +params['user_id'];
    this.downloadUserData();
  }

  private downloadUserData() {
    forkJoin([
      this.userService.getUserProfileDetails(this._userId),
      this.userService.getCurrentUserData(),
      from(this.userService.downloadUserProfilePicture(this._userId)),
      this.userService.isUserEntitledToSeePersonalData(this._userId),
    ]).subscribe(
      async ([
        userProfileData,
        loggedUser,
        profilePicture,
        isEntitledToSeePersonalData,
      ]) => {
        this._userProfile = userProfileData;
        this._loggedUser = loggedUser;
        this._canSeePersonalInfo =
          isEntitledToSeePersonalData.isEntitledForPersonalData;
        if (!isNil(profilePicture)) {
          this._userProfilePicture = <string>profilePicture;
        }
        if (userProfileData.hasUserRole(UserRoleEnum.Volunteer)) {
          const userRolesResolve = await firstValueFrom(
            this.resolveUserEvaluation(loggedUser)
          );
          this._offersToEvaluate = userRolesResolve.canEvaluate;
          this._evaluations = userRolesResolve.evaluation;
        }
        this.determineSelectedTabIndex();
        this._isLoadingData = false;
      }
    );
  }

  private resolveUserEvaluation(loggedUser: User) {
    let canEvaluate$: Observable<OffersToEvaluateIf>;
    if (
      loggedUser.hasUserRoles([
        UserRoleEnum.InstitutionAdmin,
        UserRoleEnum.InstitutionWorker,
      ])
    ) {
      canEvaluate$ = this.evaluationService.getInstitutionOffersToRateVolunteer(
        loggedUser.institution!.id!,
        this._userId
      );
    } else {
      canEvaluate$ = of({ canEvaluateUser: false, offersToEvaluate: [] });
    }
    return forkJoin({
      evaluation: this.evaluationService.getUserEvaluation(this._userId),
      canEvaluate: canEvaluate$,
    });
  }

  private performQuickUpdate(
    volunteerId: number,
    updateValue: string,
    updateCallback: (id: number, value: string) => Observable<any>
  ) {
    updateCallback(volunteerId, updateValue).subscribe();
  }

  private determineSelectedTabIndex() {
    const queryParamTabIndex = +this.activatedRoute.snapshot.queryParams['tab'];
    if (isNil(queryParamTabIndex) || isNaN(queryParamTabIndex)) {
      return;
    }
    if (Object.values(SelectedTabIndex).indexOf(queryParamTabIndex) === -1) {
      return;
    }
    this.userTabs.selectedIndex = queryParamTabIndex;
  }

  public get evaluations(): UserEvaluation {
    return this._evaluations!;
  }

  public get offersToEvaluate(): OffersToEvaluateIf | undefined {
    return this._offersToEvaluate;
  }

  public get canSeePersonalInfo(): boolean {
    return this._canSeePersonalInfo;
  }

  protected readonly UserRoleEnum = UserRoleEnum;
}
