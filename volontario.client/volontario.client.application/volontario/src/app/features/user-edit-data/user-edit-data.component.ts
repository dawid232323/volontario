import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { phoneNumberValidator } from '../../utils/validator.utils';
import { UserService } from '../../core/service/user.service';
import { isNil } from 'lodash';
import { InterestCategoryService } from '../../core/service/interestCategory.service';
import { VolunteerExperienceService } from '../../core/service/volunteer-experience.service';
import { forkJoin } from 'rxjs';
import { UserRoleEnum } from '../../core/model/user-role.model';
import { UserProfile } from '../../core/model/user.model';
import { VolunteerExperience } from '../../core/model/volunteer-experience.model';
import { InterestCategoryDTO } from '../../core/model/interestCategory.model';

@Component({
  selector: 'app-volunteer-edit-data',
  templateUrl: './user-edit-data.component.html',
  styleUrls: ['./user-edit-data.component.scss'],
})
export class UserEditDataComponent implements OnInit {
  isDataLoading: boolean;

  userId: number;
  registerFormGroup: FormGroup;

  userProfile?: UserProfile;

  availableExperienceLevels: VolunteerExperience[] = [];

  availableInterestCategories: InterestCategoryDTO[] = [];

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private interestCategoryService: InterestCategoryService,
    private experienceLevelService: VolunteerExperienceService
  ) {
    this.isDataLoading = true;
    this.userId = this.resolveUserId();
    this.registerFormGroup = this.createForm();
  }

  ngOnInit(): void {
    this.preSelectFormWithUserData();
  }

  onEditDataSubmit(): void {
    // TODO: Mikołaj connect with backend.
  }

  createForm(): FormGroup {
    return new FormGroup({
      firstName: new FormControl('', [
        Validators.required,
        Validators.pattern('^[A-źa-ź]+$'),
      ]),
      lastName: new FormControl('', [
        Validators.required,
        Validators.pattern('^[A-źa-ź]+$'),
      ]),
      contactEmailAddress: new FormControl('', [
        Validators.required,
        Validators.email,
      ]),
      experienceLevel: new FormControl(null, [Validators.required]),
      interestCategories: new FormControl(null, [Validators.required]),
      participationMotivation: new FormControl('', [
        Validators.required,
        Validators.maxLength(1500),
      ]),
      fieldOfStudy: new FormControl(null, [Validators.maxLength(100)]),
      phoneNumber: new FormControl(null, [phoneNumberValidator()]),
    });
  }

  resolveUserId(): number {
    const optionalUserId = this.route.snapshot.paramMap.get('user_id');
    if (!isNil(optionalUserId) && !isNaN(+optionalUserId)) {
      return +optionalUserId;
    }

    throw new Error('No user id found in given route');
  }

  preSelectFormWithUserData(): void {
    forkJoin([
      this.userService.getUserProfileDetails(this.userId),
      this.interestCategoryService.getPublicValues(),
      this.experienceLevelService.getPublicValues(),
    ]).subscribe(([userProfile, interestCategories, experienceLevels]) => {
      this.availableExperienceLevels = experienceLevels.map(experienceLevel => {
        return new VolunteerExperience(
          experienceLevel.id,
          experienceLevel.name,
          experienceLevel.isUsed,
          experienceLevel.definition
        );
      });

      this.availableInterestCategories = interestCategories.map(
        interestCategory => {
          return new InterestCategoryDTO(
            interestCategory.id,
            interestCategory.name,
            interestCategory.isUsed,
            interestCategory.description
          );
        }
      );

      this.userProfile = userProfile;

      this.registerFormGroup.patchValue({
        firstName: userProfile.firstName,
        lastName: userProfile.lastName,
        phoneNumber: userProfile.phoneNumber,
        contactEmailAddress: userProfile.contactEmailAddress,
        fieldOfStudy: userProfile.fieldOfStudy,
        participationMotivation: userProfile.participationMotivation,
        experienceLevel: userProfile.experienceLevel?.id,
        interestCategories: userProfile.interestCategories?.map(
          interestCategory => interestCategory.id
        ),
      });
    });

    this.isDataLoading = false;
  }

  protected readonly UserRoleEnum = UserRoleEnum;
}
