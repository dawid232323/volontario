import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/core/model/user.model';
import { UserService } from 'src/app/core/service/user.service';
import { forkJoin } from 'rxjs';
import { AdvertisementService } from 'src/app/core/service/advertisement.service';
import {
  AdvertisementBenefit,
  AdvertisementType,
} from 'src/app/core/model/advertisement.model';
import { InterestCategoryService } from 'src/app/core/service/interestCategory.service';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
import { VolunteerExperienceService } from 'src/app/core/service/volunteer-experience.service';
import { Router } from '@angular/router';
import {
  dateBeforeAfterValidator,
  DateValidatorUsageEnum,
} from 'src/app/utils/validator.utils';

@Component({
  selector: 'app-add-advertisement',
  templateUrl: './add-advertisement.component.html',
  styleUrls: ['./add-advertisement.component.scss'],
})
export class AddAdvertisementComponent implements OnInit {
  public basicInfoFormGroup: FormGroup = new FormGroup<any>({});
  public additionalInfoFormGroup: FormGroup = new FormGroup<any>({});
  public optionalInfoFormGroup: FormGroup = new FormGroup<any>({});

  public institutionWorkers: User[] = [];
  public advertisementTypes: AdvertisementType[] = [];
  public interestCategories: InterestCategoryDTO[] = [];
  public experienceLevel: VolunteerExperience[] = [];
  public advertisementBenefits: AdvertisementBenefit[] = [];

  public isAddingAdvertisement = false;
  public hasAddedAdvertisement = false;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private advertisementService: AdvertisementService,
    private interestCategoryService: InterestCategoryService,
    private experienceService: VolunteerExperienceService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeBasicInfoFormGroup();
    this.initializeAdditionalInfoFormGroup();
    this.initializeOptionalInfoFormGroup();
    this.downloadData();
  }

  private initializeBasicInfoFormGroup() {
    this.basicInfoFormGroup = this.formBuilder.group(
      {
        title: [null, [Validators.required, Validators.maxLength(100)]],
        contactPerson: [null, [Validators.required]],
        expirationDate: [null, [Validators.required]],
        advertisementType: [null, [Validators.required]],
        startDate: [null, [Validators.required]],
        endDate: [null, [Validators.required]],
        daysOfWeek: [[], [Validators.required]],
        interval: [null, []],
        durationUnit: [null, [Validators.required]],
        durationValue: [null, [Validators.required]],
      },
      {
        validators: [
          dateBeforeAfterValidator(
            DateValidatorUsageEnum.Before,
            'startDate',
            'endDate'
          ),
          dateBeforeAfterValidator(
            DateValidatorUsageEnum.After,
            'startDate',
            'expirationDate'
          ),
        ],
      }
    );
  }
  private initializeAdditionalInfoFormGroup() {
    this.additionalInfoFormGroup = this.formBuilder.group({
      advertisementCategories: [[], [Validators.required]],
      isExperienceRequired: [false, []],
      experienceLevel: [null],
      description: [null, [Validators.maxLength(300)]],
    });
  }
  private initializeOptionalInfoFormGroup() {
    this.optionalInfoFormGroup = this.formBuilder.group({
      isPoznanOnly: [true, []],
      eventPlace: [null, []],
      benefits: [[], []],
      isInsuranceNeeded: [false, []],
    });
    this.optionalInfoFormGroup.controls['eventPlace'].disable({
      onlySelf: true,
    });
  }

  public get canSubmitForm(): boolean {
    return (
      this.basicInfoFormGroup.valid &&
      this.additionalInfoFormGroup.valid &&
      this.optionalInfoFormGroup.valid
    );
  }

  public onFormSubmit() {
    const advertisementDto =
      this.advertisementService.getAdvertisementDtoFromValue({
        ...this.basicInfoFormGroup.value,
        ...this.additionalInfoFormGroup.value,
        ...this.optionalInfoFormGroup.value,
      });
    this.isAddingAdvertisement = true;
    this.advertisementService
      .createNewAdvertisement(advertisementDto)
      .subscribe({
        next: () => {
          this.isAddingAdvertisement = false;
          this.hasAddedAdvertisement = true;
        },
        error: error => {
          this.isAddingAdvertisement = false;
          console.log(error);
          alert(JSON.stringify(error.error));
        },
      });
  }

  public onSuccessSubmit() {
    this.router.navigate(['/home']);
  }

  private downloadData() {
    forkJoin({
      currentUser: this.userService.getCurrentUserData(),
      advertisementTypes: this.advertisementService.getAllAdvertisementTypes(),
      categories: this.interestCategoryService.getAllInterestCategories(),
      experiences: this.experienceService.getAllExperienceLevels(),
      benefits: this.advertisementService.getAllAdvertisementBenefits(),
    }).subscribe(
      ({
        currentUser: user,
        advertisementTypes: types,
        categories: categories,
        experiences: experiences,
        benefits: benefits,
      }) => {
        this.institutionWorkers = [user];
        this.advertisementTypes = types;
        this.interestCategories = categories;
        this.experienceLevel = experiences;
        this.advertisementBenefits = benefits;
        // default contact person should be currently logged-in user
        this.basicInfoFormGroup?.controls['contactPerson']?.setValue(user.id);
      },
      error => console.log(error)
    );
  }
}
