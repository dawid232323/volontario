import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InstitutionWorker, User } from 'src/app/core/model/user.model';
import { UserService } from 'src/app/core/service/user.service';
import { firstValueFrom, forkJoin, Observable, of, Subscription } from 'rxjs';
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
import { ActivatedRoute, Router } from '@angular/router';
import {
  dateBeforeAfterValidator,
  DateValidatorUsageEnum,
} from 'src/app/utils/validator.utils';
import { ViewportScroller } from '@angular/common';
import {
  InfoCardButtonEnum,
  InfoCardTypeEnum,
} from 'src/app/shared/features/success-info-card/info-card.component';
import { OfferBenefitService } from 'src/app/core/service/offer-benefit.service';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import { InstitutionService } from 'src/app/core/service/institution.service';

export enum AdvertisementCrudOperationType {
  Add,
  Edit,
}

@Component({
  selector: 'app-add-advertisement',
  templateUrl: './add-edit-advertisement.component.html',
  styleUrls: ['./add-edit-advertisement.component.scss'],
})
export class AddEditAdvertisementComponent implements OnInit, OnDestroy {
  public basicInfoFormGroup: FormGroup = new FormGroup<any>({});
  public additionalInfoFormGroup: FormGroup = new FormGroup<any>({});
  public optionalInfoFormGroup: FormGroup = new FormGroup<any>({});

  public institutionWorkers: InstitutionWorker[] = [];
  public advertisementTypes: AdvertisementType[] = [];
  public interestCategories: InterestCategoryDTO[] = [];
  public experienceLevel: VolunteerExperience[] = [];
  public advertisementBenefits: AdvertisementBenefit[] = [];
  public operationType: AdvertisementCrudOperationType =
    AdvertisementCrudOperationType.Add;

  public isAddingAdvertisement = false;
  public hasAddedAdvertisement = false;
  public canSelectUser = false;
  public isLoadingData = true;

  private currentOfferId = 0;
  private subscription = new Subscription();
  private loggedUser?: User;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private advertisementService: AdvertisementService,
    private interestCategoryService: InterestCategoryService,
    private experienceService: VolunteerExperienceService,
    private router: Router,
    private route: ActivatedRoute,
    private viewPort: ViewportScroller,
    private offerBenefitService: OfferBenefitService,
    private institutionService: InstitutionService
  ) {}

  ngOnInit(): void {
    this.operationType = this.route.snapshot.data['operationType'];
    this.advertisementService.addAdvertisementReloadEvent.subscribe(
      this.onFormReloadEvent.bind(this)
    );
    this.initializeBasicInfoFormGroup();
    this.initializeAdditionalInfoFormGroup();
    this.initializeOptionalInfoFormGroup();
    this.downloadData();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
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
        periodicDescription: [null, [Validators.maxLength(200)]],
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
            'endDate',
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
      description: [null, [Validators.required, Validators.maxLength(3000)]],
      otherCategories: [null],
    });
  }
  private initializeOptionalInfoFormGroup() {
    this.optionalInfoFormGroup = this.formBuilder.group({
      isPoznanOnly: [true, []],
      eventPlace: [null, []],
      benefits: [[], []],
      otherBenefits: [null],
    });
    this.optionalInfoFormGroup.controls['eventPlace'].disable({
      onlySelf: true,
    });
  }

  public onMatStepperChange() {
    this.viewPort.scrollToPosition([0, 0]);
  }

  public get canSubmitForm(): boolean {
    return (
      this.basicInfoFormGroup.valid &&
      this.additionalInfoFormGroup.valid &&
      this.optionalInfoFormGroup.valid
    );
  }

  public onFormSubmit() {
    if (!this.canSubmitForm) {
      return;
    }
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
        this.currentOfferId,
        advertisementDto
      );
    }

    advertisementCrudCallback.subscribe({
      next: createdOffer => {
        this.currentOfferId = createdOffer.id;
        this.isAddingAdvertisement = false;
        this.hasAddedAdvertisement = true;
      },
    });
  }

  public onSuccessSubmit(buttonType: InfoCardButtonEnum) {
    if (buttonType === InfoCardButtonEnum.Primary) {
      this.router.navigate(['/institution', 'advertisement-panel']);
      return;
    }
    if (buttonType === InfoCardButtonEnum.Secondary) {
      this.router.navigate(['advertisement', this.currentOfferId]);
      return;
    }
  }

  private setAdvertisementToEdit() {
    this.currentOfferId = +this.route.snapshot.params['adv_id'];
    this.advertisementService
      .getAdvertisement(this.currentOfferId)
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
        this.isLoadingData = false;
      });
  }

  private async downloadData() {
    const sources = await this.getInitialDownloadSources();
    forkJoin(sources).subscribe(
      ({
        workers: workers,
        advertisementTypes: types,
        categories: categories,
        experiences: experiences,
        benefits: benefits,
      }) => {
        this.institutionWorkers = workers.sort(
          (a: InstitutionWorker, b: InstitutionWorker) =>
            a.firstName.localeCompare(b.firstName)
        );
        this.advertisementTypes = types;
        this.interestCategories = categories;
        this.experienceLevel = experiences;
        this.advertisementBenefits = benefits;
        if (this.operationType === AdvertisementCrudOperationType.Add) {
          this.setOfferUser(this.loggedUser!);
          this.isAddingAdvertisement = false;
          this.isLoadingData = false;
        } else {
          this.setAdvertisementToEdit();
        }
        this.canSelectUser = this.institutionService.canManageInstitution(
          this.loggedUser!,
          this.loggedUser!.institution
        );
      }
    );
  }

  private async getInitialDownloadSources(): Promise<{
    [sourceIdentifier: string]: Observable<any>;
  }> {
    this.loggedUser = await firstValueFrom(
      this.userService.getCurrentUserData()
    );
    const loggedWorker = InstitutionWorker.fromUser(this.loggedUser);
    const sources = {
      workers: of([loggedWorker]),
      advertisementTypes: this.advertisementService.getAllAdvertisementTypes(),
      categories: this.interestCategoryService.getUsedValues(),
      experiences: this.experienceService.getUsedValues(),
      benefits: this.offerBenefitService.getUsedValues(),
    };
    if (
      this.loggedUser.hasUserRoles([UserRoleEnum.Moderator, UserRoleEnum.Admin])
    ) {
      sources.workers = this.institutionService.getAllInstitutionWorkers();
    } else if (this.loggedUser.hasUserRole(UserRoleEnum.InstitutionAdmin)) {
      sources.workers = this.institutionService.getInstitutionWorkers(
        this.loggedUser.institution!.id!
      );
    }
    return sources;
  }

  private onFormReloadEvent() {
    this.basicInfoFormGroup.reset();
    this.additionalInfoFormGroup.reset();
    this.optionalInfoFormGroup.reset();
    this.hasAddedAdvertisement = false;
  }

  private setOfferUser(loggedUser: User) {
    if (
      this.institutionWorkers.map(worker => worker.id).includes(loggedUser.id)
    ) {
      // default contact person should be currently logged-in user
      this.basicInfoFormGroup?.controls['contactPerson']?.setValue(
        loggedUser.id
      );
    } else {
      this.basicInfoFormGroup?.controls['contactPerson']?.setValue(
        this.institutionWorkers[0].id
      );
    }
  }

  public get successTitleMessage(): string {
    if (this.operationType === AdvertisementCrudOperationType.Add) {
      return 'Pomyślnie dodano nowe ogłoszenie';
    }
    return 'Pomyślnie zaktualizowano ogłoszenie';
  }

  public get successContentMessage(): string {
    if (
      this.operationType === AdvertisementCrudOperationType.Add &&
      this.isInstitutionVerified
    ) {
      return 'Twoje ogłoszenie będzie widoczne dla użytkowników systemu';
    }
    if (
      this.operationType === AdvertisementCrudOperationType.Add &&
      !this.isInstitutionVerified
    ) {
      return 'Twoje ogłoszenie będzie widoczne dla użytkowników systemu. Do momentu weryfikacji Twojej instytucji pozostanie ono niewidoczne';
    }
    return 'Zaktualizowane dane są widoczne dla użytkowników systemu';
  }

  public get formTitle(): string {
    if (this.operationType === AdvertisementCrudOperationType.Add) {
      return 'Dodaj nowe ogłoszenie';
    }
    return 'Edytuj ogłoszenie';
  }

  public get isInstitutionVerified(): boolean {
    return this.loggedUser?.institution?.active || false;
  }

  protected readonly InfoCardTypeEnum = InfoCardTypeEnum;
}
