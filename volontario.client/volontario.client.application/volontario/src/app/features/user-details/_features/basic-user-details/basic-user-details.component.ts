import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UserProfile } from 'src/app/core/model/user.model';
import { isNil } from 'lodash';
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { SingleFieldUserDetailsFormComponent } from 'src/app/features/user-details/_features/basic-user-details/_features/single-field-user-details-form/single-field-user-details-form.component';
import {
  SingleUserDetailsConfigProviderIf,
  SingleUserDetailsFormUsage,
  UserExperienceDescriptionConfigProvider,
  UserInterestsConfigProvider,
} from 'src/app/features/user-details/_features/basic-user-details/_features/single-field-user-details-form/single-user-details-config.provider';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-basic-user-details',
  templateUrl: './basic-user-details.component.html',
  styleUrls: ['./basic-user-details.component.scss'],
})
export class BasicUserDetailsComponent implements OnInit {
  @Input() userProfile?: UserProfile;
  @Input() canEditProfile = false;
  @Input() canQuicklyEditData = false;
  @Input() isLoadingData = true;
  @Input() canEditAdditionalVolunteerInfo = false;

  @Output() interestsChanged = new EventEmitter<UserInterestsConfigProvider>();
  @Output() experienceDescriptionChanged =
    new EventEmitter<UserExperienceDescriptionConfigProvider>();

  constructor(private router: Router, private matDialog: MatDialog) {}

  ngOnInit(): void {}

  public onInstitutionNameCLicked() {
    if (isNil(this.userProfile?.institutionId)) {
      return;
    }
    this.router.navigate(['institution', this.userProfile?.institutionId]);
  }

  public async onAddInterests() {
    const configProvider = new UserInterestsConfigProvider(
      SingleUserDetailsFormUsage.CREATE
    );
    await this.handleInterestsDialog(configProvider);
  }

  public async onEditInterests() {
    const configProvider = new UserInterestsConfigProvider(
      SingleUserDetailsFormUsage.EDIT,
      this.userProfile?.interests
    );
    await this.handleInterestsDialog(configProvider);
  }
  public async onAddExperienceDescription() {
    const configProvider = new UserExperienceDescriptionConfigProvider(
      SingleUserDetailsFormUsage.CREATE
    );
    await this.handleExperienceDialog(configProvider);
  }

  public async onEditExperienceDescription() {
    const configProvider = new UserExperienceDescriptionConfigProvider(
      SingleUserDetailsFormUsage.EDIT,
      this.userProfile?.experienceDescription
    );
    await this.handleExperienceDialog(configProvider);
  }

  private async handleInterestsDialog(
    configProvider: UserInterestsConfigProvider
  ) {
    const dialogRef = this.openFormDialog(configProvider);
    const dialogResult = await firstValueFrom(dialogRef.afterClosed());
    if (isNil(dialogResult) || dialogResult.trim() === '') {
      return;
    }
    configProvider.setData(dialogResult);
    this.interestsChanged.emit(configProvider);
    this.userProfile!.interests = dialogResult;
  }

  private async handleExperienceDialog(
    configProvider: UserExperienceDescriptionConfigProvider
  ) {
    const dialogRef = this.openFormDialog(configProvider);
    const dialogResult = await firstValueFrom(dialogRef.afterClosed());
    if (isNil(dialogResult) || dialogResult.trim() === '') {
      return;
    }
    configProvider.setData(dialogResult);
    this.experienceDescriptionChanged.emit(configProvider);
    this.userProfile!.experienceDescription = dialogResult;
  }

  private openFormDialog(
    configProvider: SingleUserDetailsConfigProviderIf
  ): MatDialogRef<SingleFieldUserDetailsFormComponent> {
    return this.matDialog.open(SingleFieldUserDetailsFormComponent, {
      data: configProvider,
    });
  }

  protected readonly isNil = isNil;
}
