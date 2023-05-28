import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PatchUserDto, User } from 'src/app/core/model/user.model';
import { UserService } from 'src/app/core/service/user.service';
import { isNil } from 'lodash';
import { ActivatedRoute, Router } from '@angular/router';
import { OfferApplicationService } from 'src/app/core/service/offer-application.service';
import { firstValueFrom } from 'rxjs';
import { OfferApplicationModelDto } from 'src/app/core/model/offerApplication.model';
import { SuccessInfoCardButtonEnum } from 'src/app/shared/features/success-info-card/success-info-card.component';

@Component({
  selector: 'app-offer-apply',
  templateUrl: './offer-apply.component.html',
  styleUrls: ['./offer-apply.component.scss'],
})
export class OfferApplyComponent implements OnInit {
  public contactFormGroup = new FormGroup<any>({});
  public reasonFormGroup = new FormGroup<any>({});
  private loggedUser?: User;
  private _advertisementId?: number;
  private _isLoadingData = true;
  private _hasAppliedForOffer: boolean = false;
  private _createdApplicationId?: number;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute,
    private applicationService: OfferApplicationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this._advertisementId = <number>this.route.snapshot.params['adv_id'];
    this.initializeContactFormData();
    this.initializePurposeFormData();
    this.userService.getCurrentUserData().subscribe(user => {
      this.loggedUser = user;
      this.contactFormGroup.setValue(
        {
          contactEmail: user.contactEmailAddress,
          phoneNumber: user.phoneNumber,
        },
        { emitEvent: true }
      );
      this.contactFormGroup.markAsUntouched();
      this._isLoadingData = false;
    });
  }

  public get offerId(): number {
    return Number(this._advertisementId!);
  }

  public get isLoadingData(): boolean {
    return this._isLoadingData;
  }

  public get hasAppliedForOffer(): boolean {
    return this._hasAppliedForOffer;
  }

  public get canSubmitForm(): boolean {
    return this.contactFormGroup.valid && this.reasonFormGroup.valid;
  }

  public async onFormSubmit() {
    this._isLoadingData = true;
    let failedUserEdit = false;
    if (this.contactFormGroup.dirty) {
      await this.submitUserContactData().catch(error => {
        failedUserEdit = true;
        alert(JSON.stringify(error.error));
      });
    }
    if (failedUserEdit) {
      this._isLoadingData = false;
      return;
    }
    const applicationDto = OfferApplicationModelDto.fromApplyForm(
      this.loggedUser?.id!,
      this.offerId,
      this.reasonFormGroup.value['reason']
    );
    this.applicationService.createApplication(applicationDto).subscribe({
      next: result => {
        this._isLoadingData = false;
        this._hasAppliedForOffer = true;
        this._createdApplicationId = result.id;
      },
      error: error => {
        alert(JSON.stringify(error.error));
        this._isLoadingData = false;
      },
    });
  }

  public onSuccessCardButtonClicked(button: SuccessInfoCardButtonEnum) {
    if (button === SuccessInfoCardButtonEnum.Primary) {
      this.onGoToApplicationDetails();
    }
    if (button === SuccessInfoCardButtonEnum.Secondary) {
      this.onReturnToOfferDetails();
    }
  }

  //TODO assign corresponding url when single application view is created
  private onGoToApplicationDetails() {}

  private onReturnToOfferDetails() {
    return this.router.navigate(['/advertisement', this.offerId]);
  }

  private async submitUserContactData() {
    return firstValueFrom(
      this.userService.patchVolunteerData(
        this.loggedUser?.id!,
        PatchUserDto.fromApplyFormVerification(this.contactFormGroup.value)
      )
    );
  }

  private initializeContactFormData() {
    this.contactFormGroup = this.formBuilder.group({
      contactEmail: [
        this.loggedUser?.contactEmailAddress,
        [Validators.required, Validators.email],
      ],
      phoneNumber: [
        this.getFormNullableValue(this.loggedUser?.phoneNumber),
        [Validators.minLength(1), Validators.maxLength(9)],
      ],
    });
  }

  private initializePurposeFormData() {
    this.reasonFormGroup = this.formBuilder.group({
      reason: [null, [Validators.required, Validators.maxLength(100)]],
    });
  }

  private getFormNullableValue(value: any): any {
    if (isNil(value)) {
      return null;
    }
    return value;
  }
}
