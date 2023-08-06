import { Component, OnInit } from '@angular/core';
import { SecurityService } from '../../core/service/security/security.service';
import { LoginInterface } from '../../core/interface/authorization.interface';
import { ActivatedRoute, Router, UrlTree } from '@angular/router';
import { isNil } from 'lodash';
import { HttpErrorResponse } from '@angular/common/http';

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

  constructor(
    private authService: SecurityService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

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

  onRegisterButtonClicked() {
    return this.router.navigate(['register']);
  }

  onRegisterInstitutionClicked() {
    return this.router.navigate(['institution/register']);
  }
}
