import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { InstitutionService } from 'src/app/core/service/institution.service';

@Component({
  selector: 'app-register-institution',
  templateUrl: './register-institution.component.html',
  styleUrls: ['./register-institution.component.scss'],
})
export class RegisterInstitutionComponent implements OnInit {
  basicInfoFormGroup: FormGroup = new FormGroup<any>({});
  additionalInfoFormGroup: FormGroup = new FormGroup<any>({});
  hasRegisteredCorrectly: boolean = false;

  successTitle = 'Wniosek złożony pomyślnie';
  successContent =
    'Wniosek o rejestrację instytucji został złożony pomyślnie! Nasi moderatorzy rozpatrzą go w czasie do 5 dni roboczych.';
  buttonText = 'Strona główna';

  constructor(
    private formBuilder: FormBuilder,
    private institutionService: InstitutionService
  ) {}

  ngOnInit(): void {
    this.basicInfoFormGroup = this.formBuilder.group({
      institutionName: [null, [Validators.required, Validators.maxLength(50)]],
      krsNumber: [
        null,
        [
          Validators.required,
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
      institutionTags: [[], [Validators.required, this.arrayLengthValidator]],
      institutionDescription: [null, [Validators.maxLength(200)]],
      operationPlace: [null, [Validators.required]],
      rulesApprove: [false, [Validators.requiredTrue]],
      rodoApprove: [false, [Validators.requiredTrue]],
    });
  }

  private arrayLengthValidator(
    tagsInput: FormControl
  ): ValidationErrors | null {
    if ((<string[]>tagsInput.value).length > 10) {
      return { valid: false };
    }
    return null;
  }

  onFormSubmit() {
    const institutionMode =
      this.institutionService.getInstitutionModelFromFormData(
        this.basicInfoFormGroup.value,
        this.additionalInfoFormGroup.value
      );
    this.institutionService.createInstitution(institutionMode).subscribe({
      next: this.onRegisterSuccess.bind(this),
      error: err => console.log(err),
    });
  }

  private onRegisterSuccess(result: any) {
    this.hasRegisteredCorrectly = true;
  }

  get canSubmitForm(): boolean {
    return this.basicInfoFormGroup.valid && this.additionalInfoFormGroup.valid;
  }
}