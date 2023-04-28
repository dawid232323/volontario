import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { RegisterContactPersonInterface } from 'src/app/core/interface/authorization.interface';

@Component({
  selector: 'app-register-contact-person-form',
  templateUrl: './register-contact-person-form.component.html',
  styleUrls: ['./register-contact-person-form.component.scss'],
})
export class RegisterContactPersonFormComponent implements OnInit {
  public registerContactPersonFormGroup = new FormGroup<any>({});
  private _isPasswordShown: boolean = false;

  @Output() formSubmitEvent =
    new EventEmitter<RegisterContactPersonInterface>();
  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.registerContactPersonFormGroup = this.formBuilder.group({
      password: [
        null,
        [
          Validators.required,
          Validators.pattern('.*[a-z].*'),
          Validators.pattern('.*[A-Z].*'),
          Validators.pattern('.*[0-9].*'),
          Validators.pattern('.*[!@#$%^&*()_+].*'),
        ],
      ],
      repeatPassword: [
        null,
        [Validators.required, this.equalPasswordValidator],
      ],
    });
    this.registerContactPersonFormGroup.addValidators(
      this.equalPasswordValidator
    );
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

  public get passwordValue(): string {
    return this.registerContactPersonFormGroup.value.password;
  }

  public get repeatPasswordValue(): string {
    return this.registerContactPersonFormGroup.value.repeatPassword;
  }

  public onFormSubmit() {
    const { password } = this.registerContactPersonFormGroup.value;
    this.formSubmitEvent.emit({ password: password });
  }

  private equalPasswordValidator: ValidatorFn = (
    group: AbstractControl
  ): ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('repeatPassword')?.value;
    return pass === confirmPass ? null : { notSame: true };
  };
}
