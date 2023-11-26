import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InstitutionService } from 'src/app/core/service/institution.service';
import { arrayLengthValidator } from 'src/app/utils/validator.utils';
import {
  InfoCardButtonEnum,
  InfoCardTypeEnum,
} from 'src/app/shared/features/success-info-card/info-card.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-institution',
  templateUrl: './register-institution.component.html',
  styleUrls: ['./register-institution.component.scss'],
})
export class RegisterInstitutionComponent implements OnInit {
  basicInfoFormGroup: FormGroup = new FormGroup<any>({});
  additionalInfoFormGroup: FormGroup = new FormGroup<any>({});
  hasRegisteredCorrectly: boolean = false;
  isPerformingRegistration: boolean = false;

  successTitle = 'Wniosek złożony pomyślnie';
  successContent =
    'Wniosek o rejestrację instytucji został złożony pomyślnie! Nasza moderacja rozpatrzy go jak najszybciej.';
  buttonText = 'Strona główna';

  constructor(
    private formBuilder: FormBuilder,
    private institutionService: InstitutionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.basicInfoFormGroup = this.formBuilder.group({
      institutionName: [null, [Validators.required, Validators.maxLength(50)]],
      krsNumber: [
        null,
        [
          Validators.minLength(10),
          Validators.maxLength(10),
          Validators.pattern('[0-9]*'),
        ],
      ],
      address: [null, [Validators.required]],
      registerPersonName: [null, [Validators.required]],
      registerPersonLastName: [null, [Validators.required]],
      registerPersonEmail: [null, [Validators.required, Validators.email]],
      registerPersonPhoneNumber: [
        null,
        [
          Validators.required,
          Validators.minLength(9),
          Validators.maxLength(9),
          Validators.pattern('[0-9]*'),
        ],
      ],
    });
    this.additionalInfoFormGroup = this.formBuilder.group({
      institutionTags: [[], [Validators.required, arrayLengthValidator(10)]],
      institutionDescription: [
        null,
        [Validators.required, Validators.maxLength(200)],
      ],
      operationPlace: [null, [Validators.required]],
      rulesApprove: [false, [Validators.requiredTrue]],
      rodoApprove: [false, [Validators.requiredTrue]],
    });
  }

  public onFormSubmit() {
    if (!this.canSubmitForm) {
      return;
    }
    this.isPerformingRegistration = true;
    const institutionModel =
      this.institutionService.getInstitutionModelFromFormData(
        this.basicInfoFormGroup.value,
        this.additionalInfoFormGroup.value
      );
    this.institutionService.createInstitution(institutionModel).subscribe({
      next: this.onRegisterSuccess.bind(this),
      error: err => console.log(err),
    });
  }

  public onCardButtonClicked($event: InfoCardButtonEnum) {
    if ($event === InfoCardButtonEnum.Primary) {
      this.router.navigate(['/']);
    }
  }

  private onRegisterSuccess(result: any) {
    this.isPerformingRegistration = false;
    this.hasRegisteredCorrectly = true;
  }

  get canSubmitForm(): boolean {
    return this.basicInfoFormGroup.valid && this.additionalInfoFormGroup.valid;
  }

  protected readonly InfoCardTypeEnum = InfoCardTypeEnum;
}
