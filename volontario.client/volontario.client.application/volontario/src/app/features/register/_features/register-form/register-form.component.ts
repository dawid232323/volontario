import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SelectFieldModelIf } from 'src/app/core/interface/selectField.interface';
import { VolunteerRegisterDTO } from 'src/app/core/model/volunteer.model';

interface passwordVerification {
  hasLowerCase: boolean;
  hasUpperCase: boolean;
  hasNumber: boolean;
  hasSpecialChar: boolean;
  isTheSame: boolean;
}

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.scss'],
})
export class RegisterFormComponent {
  @Output() formSubmit = new EventEmitter<VolunteerRegisterDTO>();
  @Input() isRegistering: boolean = false;
  @Input() availableCategories: SelectFieldModelIf[] = [];
  @Input() availableExperiences: SelectFieldModelIf[] = [];

  registerFormGroup = new FormGroup({
    firstName: new FormControl('', [
      Validators.required,
      Validators.pattern('^[A-źa-ź]+$'),
    ]),
    lastName: new FormControl('', [
      Validators.required,
      Validators.pattern('^[A-źa-ź]+$'),
    ]),
    domainEmail: new FormControl('', [
      Validators.required,
      Validators.email,
      Validators.pattern('.*.st.amu.edu.pl'),
    ]),
    contactEmail: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
    passwordRepeat: new FormControl('', [Validators.required]),
    experience: new FormControl(null, [Validators.required]),
    interestCategories: new FormControl([1], [Validators.required]),
    participationMotivation: new FormControl('', [
      Validators.required,
      Validators.maxLength(150),
    ]),
    rodo: new FormControl('', [Validators.required]),
  });

  constructor() {}

  public checkPassword(): passwordVerification {
    const passwordValue = this.registerFormGroup.get('password')?.value;
    const passwordRepeatValue =
      this.registerFormGroup.get('passwordRepeat')?.value;

    const hasLowerCase = Boolean(passwordValue && passwordValue.match(/[a-z]/));
    const hasUpperCase = Boolean(passwordValue && passwordValue.match(/[A-Z]/));
    const hasNumber = Boolean(passwordValue && passwordValue.match(/\d/));
    const hasSpecialChar = Boolean(
      passwordValue && passwordValue.match(/[!@#$%^&*()_+]/)
    );
    const isTheSame = Boolean(passwordValue === passwordRepeatValue);

    return { hasLowerCase, hasUpperCase, hasNumber, hasSpecialChar, isTheSame };
  }

  public onFormSubmit(): void {
    const {
      firstName,
      lastName,
      domainEmail,
      contactEmail,
      password,
      experience,
      interestCategories,
      participationMotivation,
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
      phoneNumber: (
        Math.random() * (999999999 - 0o00000001) +
        0o00000001
      ).toString(),
    };

    this.formSubmit.emit(registerDTO);
  }
}
