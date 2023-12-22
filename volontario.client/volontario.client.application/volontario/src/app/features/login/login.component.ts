import { Component, OnInit } from '@angular/core';
import { SecurityService } from '../../core/service/security/security.service';
import { LoginInterface } from '../../core/interface/authorization.interface';
import { ActivatedRoute, Router, UrlTree } from '@angular/router';
import { isNil } from 'lodash';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { SingleDataInputComponent } from '../../shared/features/single-data-input/single-data-input.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../core/service/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  public loginHasFailed = false;
  public isPerformingLogin = false;
  public isAccountInactive = false;
  public nextDestination?: UrlTree;

  resetPasswordForm: FormGroup;

  constructor(
    private authService: SecurityService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private matDialog: MatDialog,
    private formBuilder: FormBuilder,
    private userService: UserService
  ) {
    this.resetPasswordForm = this.createFormGroup();
  }

  ngOnInit(): void {
    const next = this.activatedRoute.snapshot.queryParams['next'];
    if (!isNil(next)) {
      this.nextDestination = this.router.parseUrl(next);
    }
  }

  onLoginFormSubmit(event: LoginInterface) {
    this.isPerformingLogin = true;
    this.authService.login(event).subscribe({
      next: () => {
        if (!isNil(this.nextDestination)) {
          return this.router.navigateByUrl(this.nextDestination);
        }
        return this.router.navigate(['home']);
      },
      error: this.handleLoginError.bind(this),
    });
  }

  private handleLoginError(error: HttpErrorResponse) {
    this.isPerformingLogin = false;
    this.loginHasFailed = true;
    if (error.status === 403) {
      this.isAccountInactive = true;
    }
    console.error(error);
  }

  private createFormGroup(): FormGroup {
    return this.formBuilder.group({
      contactEmailAddress: [null, [Validators.required, Validators.email]],
    });
  }

  onRegisterButtonClicked() {
    return this.router.navigate(['register']);
  }

  onRegisterInstitutionClicked() {
    return this.router.navigate(['institution/register']);
  }

  onPasswordResetButtonClicked(): void {
    this.matDialog
      .open(SingleDataInputComponent, {
        width: '400px',
        data: {
          description:
            'Podaj kontaktowy adres email przypisany do Twojego konta. Jeżeli konto\n' +
            '        z takim adresem email istnieje, wyślemy na ten adres maila z linkiem do\n' +
            '        zresetowania hasła.',
          iconName: 'mail',
          hintsMap: new Map([
            ['required', 'Pole jest wymagane'],
            ['email', 'Niepoprawny format adresu email'],
          ]),
          formGroup: this.resetPasswordForm,
          labelName: 'Podaj kontaktowy adres email',
        },
      })
      .afterClosed()
      .subscribe((contactEmailAddress: string) => {
        if (!isNil(contactEmailAddress)) {
          this.userService.resetUserPassword(contactEmailAddress).subscribe();
        }
      });
  }
}
