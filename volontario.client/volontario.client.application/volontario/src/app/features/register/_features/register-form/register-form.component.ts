import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { isNil } from 'lodash';
import { SelectFieldModelIf } from 'src/app/core/interface/selectField.interface';
import { VolunteerRegisterDTO } from 'src/app/core/model/volunteer.model';
import {
  countCharacters,
  hasMaxLengthError,
  phoneNumberValidator,
} from 'src/app/utils/validator.utils';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.scss'],
})
export class RegisterFormComponent implements OnInit {
  @Output() showUseRegulations = new EventEmitter<void>();
  @Output() showRodoRegulations = new EventEmitter<void>();
  @Output() formSubmit = new EventEmitter<VolunteerRegisterDTO>();
  @Input() isRegistering: boolean = false;
  @Input() availableCategories: SelectFieldModelIf[] = [];
  @Input() availableExperiences: SelectFieldModelIf[] = [];

  private _isPasswordShown: boolean = false;

  registerFormGroup = new FormGroup({
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
    domainEmail: new FormControl('', [
      Validators.required,
      Validators.email,
      Validators.pattern('.*.st.amu.edu.pl'),
    ]),
    contactEmail: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.pattern('.*[a-z].*'),
      Validators.pattern('.*[A-Z].*'),
      Validators.pattern('.*[0-9].*'),
      Validators.pattern('.*[!@#$%^&*()_+].*'),
    ]),
    passwordRepeat: new FormControl('', [Validators.required]),
    experience: new FormControl(null, [Validators.required]),
    interestCategories: new FormControl([1], [Validators.required]),
    participationMotivation: new FormControl('', [
      Validators.required,
      Validators.maxLength(500),
    ]),
    fieldOfStudy: new FormControl(null, [Validators.maxLength(100)]),
    phoneNumber: new FormControl(null, [
      Validators.required,
      phoneNumberValidator(),
    ]),
    rodo: new FormControl('', [Validators.required, Validators.requiredTrue]),
    regulations: new FormControl('', [
      Validators.required,
      Validators.requiredTrue,
    ]),
  });

  constructor() {}

  ngOnInit(): void {
    this.registerFormGroup.addValidators(this.equalPasswordValidator);
  }

  public get passwordValue(): any {
    return this.registerFormGroup.get('password')?.value;
  }

  public get repeatPasswordValue(): any {
    return this.registerFormGroup.get('passwordRepeat')?.value;
  }

  public onFormSubmit(): void {
    if (this.registerFormGroup.invalid) {
      return;
    }
    const {
      firstName,
      lastName,
      domainEmail,
      contactEmail,
      password,
      experience,
      interestCategories,
      participationMotivation,
      fieldOfStudy,
      phoneNumber,
    } = this.registerFormGroup.value;

    const registerDTO: VolunteerRegisterDTO = {
      firstName: <string>firstName,
      lastName: <string>lastName,
      domainEmail: <string>domainEmail,
      contactEmail: <string>contactEmail,
      password: <string>password,
      participationMotivation: <string>participationMotivation,
      experienceId: <number>1,
      interestCategoriesIds: <number[]>interestCategories,
      fieldOfStudy: fieldOfStudy || null,
      phoneNumber: phoneNumber,
    };

    this.formSubmit.emit(registerDTO);
  }

  public get isPasswordShown(): boolean {
    return this._isPasswordShown;
  }

  public set isPasswordShown(isShown: boolean) {
    this._isPasswordShown = isShown;
  }

  public get passwordInputType(): string {
    if (this.isPasswordShown) {
      return 'text';
    }
    return 'password';
  }

  private equalPasswordValidator: ValidatorFn = (
    group: AbstractControl
  ): ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('passwordRepeat')?.value;
    return pass === confirmPass ? null : { notSame: true };
  };

  protected readonly hasMaxLengthError = hasMaxLengthError;

  protected readonly isNil = isNil;

  protected readonly countCharacters = countCharacters;
}
