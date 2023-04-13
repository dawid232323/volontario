import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { LoginInterface } from 'src/app/core/interface/authorization.interface';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
})
export class LoginFormComponent {
  loginFormGroup = new FormGroup({
    login: new FormControl('', [
      Validators.required,
      Validators.email,
      Validators.pattern('.*.st.amu.edu.pl'),
    ]),
    password: new FormControl('', [Validators.required]),
  });

  @Output() formSubmit = new EventEmitter<LoginInterface>();
  @Output() registerButtonClicked = new EventEmitter<void>();
  @Output() registerInstitutionButtonClicked = new EventEmitter<void>();
  @Input() loginHasFailed: boolean = false;
  @Input() isLogging = false;

  constructor() {}

  onFormSubmit() {
    const { login, password } = this.loginFormGroup.value;
    const loginDTO: LoginInterface = {
      domainEmailAddress: <string>login,
      password: <string>password,
    };
    this.formSubmit.emit(loginDTO);
  }
}
