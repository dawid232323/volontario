import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { phoneNumberValidator } from '../../utils/validator.utils';
import { UserService } from '../../core/service/user.service';
import { isNil } from 'lodash';
import { InterestCategoryService } from '../../core/service/interestCategory.service';
import { forkJoin } from 'rxjs';
import { UserRoleEnum } from '../../core/model/user-role.model';
import { PatchUserDto, UserProfile } from '../../core/model/user.model';
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

  availableInterestCategories: InterestCategoryDTO[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private interestCategoryService: InterestCategoryService
  ) {
    this.isDataLoading = true;
    this.userId = this.resolveUserId();
    this.registerFormGroup = this.createForm();
  }

  ngOnInit(): void {
    this.preSelectFormWithUserData();
  }

  onEditDataSubmit() {
    this.userService
      .patchUserData(
        this.userId,
        PatchUserDto.fromEditDataForm(this.registerFormGroup.value)
      )
      .subscribe(() => {
        this.returnToProfile();
      });
  }

  returnToProfile() {
    this.router.navigate(['user', this.userId]);
  }

  createForm(): FormGroup {
    return new FormGroup({
      firstName: new FormControl('', [
        Validators.required,
        Validators.pattern('^[A-źa-ź]+$'),
        Validators.maxLength(100),
      ]),
      lastName: new FormControl('', [
        Validators.required,
        Validators.pattern('^[A-źa-ź]+$'),
        Validators.maxLength(100),
      ]),
      contactEmailAddress: new FormControl('', [
        Validators.required,
        Validators.email,
      ]),
      experienceLevel: new FormControl(null, [Validators.required]),
      interestCategories: new FormControl(null, [Validators.required]),
      participationMotivation: new FormControl('', [
        Validators.required,
        Validators.maxLength(500),
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
    ]).subscribe(([userProfile, interestCategories]) => {

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
        interestCategories: userProfile.interestCategories?.map(
          interestCategory => interestCategory.id
        ),
      });
    });

    this.isDataLoading = false;
  }

  protected readonly UserRoleEnum = UserRoleEnum;
}
