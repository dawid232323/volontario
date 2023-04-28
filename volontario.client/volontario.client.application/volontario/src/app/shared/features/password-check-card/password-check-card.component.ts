import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-password-check-card',
  templateUrl: './password-check-card.component.html',
  styleUrls: ['./password-check-card.component.scss'],
})
export class PasswordCheckCardComponent implements OnInit {
  @Input() passwordValue: string = '';
  @Input() repeatPasswordValue: string = '';

  constructor() {}

  ngOnInit(): void {}

  public checkPassword(): passwordVerification {
    const passwordValue = this.passwordValue;
    const passwordRepeatValue = this.repeatPasswordValue;

    const hasLowerCase = Boolean(passwordValue && passwordValue.match(/[a-z]/));
    const hasUpperCase = Boolean(passwordValue && passwordValue.match(/[A-Z]/));
    const hasNumber = Boolean(passwordValue && passwordValue.match(/\d/));
    const hasSpecialChar = Boolean(
      passwordValue && passwordValue.match(/[!@#$%^&*()_+]/)
    );
    const isTheSame = Boolean(passwordValue === passwordRepeatValue);

    return { hasLowerCase, hasUpperCase, hasNumber, hasSpecialChar, isTheSame };
  }
}

interface passwordVerification {
  hasLowerCase: boolean;
  hasUpperCase: boolean;
  hasNumber: boolean;
  hasSpecialChar: boolean;
  isTheSame: boolean;
}
