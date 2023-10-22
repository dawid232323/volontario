import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from 'src/app/core/service/user.service';
import { User, UserProfile } from 'src/app/core/model/user.model';
import { ActivatedRoute, Params } from '@angular/router';
import { forkJoin, from, Observable, Subscription } from 'rxjs';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import {
  UserExperienceDescriptionConfigProvider,
  UserInterestsConfigProvider,
} from 'src/app/features/user-details/_features/basic-user-details/_features/single-field-user-details-form/single-user-details-config.provider';

import { isNil } from 'lodash';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorDialogService } from 'src/app/core/service/error-dialog.service';

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
  public _userProfilePicture?: string;

  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private errorDialogService: ErrorDialogService
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
    ]).subscribe(([userProfileData, loggedUser, profilePicture]) => {
      this._userProfile = userProfileData;
      this._loggedUser = loggedUser;
      if (!isNil(profilePicture)) {
        this._userProfilePicture = <string>profilePicture;
      }
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
