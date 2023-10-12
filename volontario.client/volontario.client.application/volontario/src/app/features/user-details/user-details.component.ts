import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from 'src/app/core/service/user.service';
import { User, UserProfile } from 'src/app/core/model/user.model';
import { ActivatedRoute, Params } from '@angular/router';
import { forkJoin, Observable, Subscription } from 'rxjs';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import {
  UserExperienceDescriptionConfigProvider,
  UserInterestsConfigProvider,
} from 'src/app/features/user-details/_features/basic-user-details/_features/single-field-user-details-form/single-user-details-config.provider';
import { isNil } from 'lodash';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss'],
})
export class UserDetailsComponent implements OnInit, OnDestroy {
  private _userProfile?: UserProfile;
  private _loggedUser?: User;
  private _userId: number;

  private subscriptions = new Subscription();
  private _isLoadingData = true;

  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute
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
    return this._loggedUser?.id === this._userProfile?.id;
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
    ]).subscribe(([userProfileData, loggedUser]) => {
      this._userProfile = userProfileData;
      this._loggedUser = loggedUser;
      this._isLoadingData = false;
    });
  }

  private performQuickUpdate(
    volunteerId: number,
    updateValue: string,
    updateCallback: (id: number, value: string) => Observable<any>
  ) {
    updateCallback(volunteerId, updateValue).subscribe();
  }
}
