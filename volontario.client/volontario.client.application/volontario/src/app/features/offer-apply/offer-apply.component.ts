import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PatchUserDto, User } from 'src/app/core/model/user.model';
import { UserService } from 'src/app/core/service/user.service';
import { isNil } from 'lodash';
import { ActivatedRoute, Router } from '@angular/router';
import { OfferApplicationService } from 'src/app/core/service/offer-application.service';
import { firstValueFrom } from 'rxjs';
import { OfferApplicationModelDto } from 'src/app/core/model/offerApplication.model';
import {
  InfoCardButtonEnum,
  InfoCardTypeEnum,
} from 'src/app/shared/features/success-info-card/info-card.component';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorDialogService } from 'src/app/core/service/error-dialog.service';
import { ErrorDialogInitialData } from 'src/app/shared/features/error-dialog/error-dialog.component';
import { ApplicationStateCheck } from 'src/app/core/model/application.model';

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
    private router: Router,
    private errorDialogService: ErrorDialogService
  ) {}

  async ngOnInit() {
    this._advertisementId = <number>this.route.snapshot.params['adv_id'];
    this.initializeContactFormData();
    this.initializePurposeFormData();
    const user = await firstValueFrom(this.userService.getCurrentUserData());
    const stateCheck = await firstValueFrom(
      this.applicationService.checkApplicationState(
        user.id,
        this._advertisementId
      )
    );
    if (stateCheck.applied) {
      await this.handleAppliedOffer(stateCheck);
    }
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
    if (!this.canSubmitForm) {
      return;
    }
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
      error: (error: HttpErrorResponse) => {
        alert(JSON.stringify(error.error));
        this._isLoadingData = false;
        if (error.status === 207) {
          // we consider 207 status code as partial success, so it must not be treated exactly like error.
          this.router.navigate(['../'], { relativeTo: this.route });
        } else {
          this.reasonFormGroup.reset();
        }
      },
    });
  }

  public onSuccessCardButtonClicked(button: InfoCardButtonEnum) {
    if (button === InfoCardButtonEnum.Secondary) {
      this.onReturnToOfferDetails();
    }
  }

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
      reason: [null, [Validators.required, Validators.maxLength(500)]],
    });
  }

  private getFormNullableValue(value: any): any {
    if (isNil(value)) {
      return null;
    }
    return value;
  }

  private async handleAppliedOffer(stateCheck: ApplicationStateCheck) {
    const initialData: ErrorDialogInitialData = {
      error: new Error(
        `Znaleziono istniejącą aplikację na dane ogłoszenie ze statusem ${stateCheck.state}`
      ),
      dialogTitle: 'Zaaplikowałeś już na to ogłoszenie',
      dialogMessage: 'Wróć do listy ogłoszeń i wybierz coś innego',
    };
    await firstValueFrom(
      this.errorDialogService.openDefaultErrorDialog(initialData).afterClosed()
    );
    return this.router.navigate(['advertisement', 'list']);
  }

  protected readonly InfoCardTypeEnum = InfoCardTypeEnum;
}
