import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/core/model/user.model';
import { UserService } from 'src/app/core/service/user.service';
import { forkJoin } from 'rxjs';
import { AdvertisementService } from 'src/app/core/service/advertisement.service';
import {
  AdvertisementAdditionalInfo,
  AdvertisementBasicInfo,
  AdvertisementBenefit,
  AdvertisementOptionalInfo,
  AdvertisementType,
} from 'src/app/core/model/advertisement.model';
import { InterestCategoryService } from 'src/app/core/service/interestCategory.service';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
import { VolunteerExperienceService } from 'src/app/core/service/volunteer-experience.service';
import {
  ActivatedRoute,
  ActivatedRouteSnapshot,
  Router,
} from '@angular/router';
import {
  dateBeforeAfterValidator,
  DateValidatorUsageEnum,
} from 'src/app/utils/validator.utils';

export enum AdvertisementCrudOperationType {
  Add,
  Edit,
}

@Component({
  selector: 'app-add-advertisement',
  templateUrl: './add-edit-advertisement.component.html',
  styleUrls: ['./add-edit-advertisement.component.scss'],
})
export class AddEditAdvertisementComponent implements OnInit {
  public basicInfoFormGroup: FormGroup = new FormGroup<any>({});
  public additionalInfoFormGroup: FormGroup = new FormGroup<any>({});
  public optionalInfoFormGroup: FormGroup = new FormGroup<any>({});

  public institutionWorkers: User[] = [];
  public advertisementTypes: AdvertisementType[] = [];
  public interestCategories: InterestCategoryDTO[] = [];
  public experienceLevel: VolunteerExperience[] = [];
  public advertisementBenefits: AdvertisementBenefit[] = [];
  public operationType: AdvertisementCrudOperationType =
    AdvertisementCrudOperationType.Add;

  public isAddingAdvertisement = false;
  public hasAddedAdvertisement = false;

  private editedAdvertisementId = 0;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private advertisementService: AdvertisementService,
    private interestCategoryService: InterestCategoryService,
    private experienceService: VolunteerExperienceService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.operationType = this.route.snapshot.data['operationType'];
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
    let advertisementCrudCallback;
    if (this.operationType === AdvertisementCrudOperationType.Add) {
      advertisementCrudCallback =
        this.advertisementService.createNewAdvertisement(advertisementDto);
    } else {
      advertisementCrudCallback = this.advertisementService.updateAdvertisement(
        this.editedAdvertisementId,
        advertisementDto
      );
    }

    advertisementCrudCallback.subscribe({
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
    this.router.navigate(['/institution', 'advertisement-panel']);
  }

  private setAdvertisementToEdit() {
    this.editedAdvertisementId = +this.route.snapshot.params['adv_id'];
    this.advertisementService
      .getAdvertisement(this.editedAdvertisementId)
      .subscribe(result => {
        this.basicInfoFormGroup.setValue(
          AdvertisementBasicInfo.fromAdvertisementDto(result)
        );
        this.additionalInfoFormGroup.setValue(
          AdvertisementAdditionalInfo.fromAdvertisementDto(result)
        );
        this.optionalInfoFormGroup.setValue(
          AdvertisementOptionalInfo.fromAdvertisementDto(result)
        );
        this.basicInfoFormGroup.updateValueAndValidity();
        this.additionalInfoFormGroup.updateValueAndValidity();
        this.optionalInfoFormGroup.updateValueAndValidity();
        this.basicInfoFormGroup.controls[
          'advertisementType'
        ].updateValueAndValidity({ onlySelf: true, emitEvent: true });
        this.optionalInfoFormGroup.controls[
          'isPoznanOnly'
        ].updateValueAndValidity({ onlySelf: true, emitEvent: true });
        this.isAddingAdvertisement = false;
      });
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
        if (this.operationType === AdvertisementCrudOperationType.Add) {
          // default contact person should be currently logged-in user
          this.basicInfoFormGroup?.controls['contactPerson']?.setValue(user.id);
          this.isAddingAdvertisement = false;
        } else {
          this.setAdvertisementToEdit();
        }
      },
      error => console.log(error)
    );
  }

  public get successTitleMessage(): string {
    if (this.operationType === AdvertisementCrudOperationType.Add) {
      return 'Pomyślnie dodano nowe ogłoszenie';
    }
    return 'Pomyślnie zaktualizowano ogłoszenie';
  }

  public get successContentMessage(): string {
    if (this.operationType === AdvertisementCrudOperationType.Add) {
      return 'Twoje ogłoszenie będzie widoczne dla wszystkich zarejetrowanych wolontariuszy';
    }
    return 'Zaktualizowane dane są widoczne dla użytkowników systemu';
  }

  public get formTitle(): string {
    if (this.operationType === AdvertisementCrudOperationType.Add) {
      return 'Dodaj nowe ogłoszenie';
    }
    return 'Edytuj ogłoszenie';
  }
}
