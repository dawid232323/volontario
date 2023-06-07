import { Component, OnInit } from '@angular/core';
import { SecurityService } from '../../core/service/security/security.service';
import { LoginInterface } from '../../core/interface/authorization.interface';
import {
  ActivatedRoute,
  Router,
  UrlSerializer,
  UrlTree,
} from '@angular/router';
import { isNil } from 'lodash';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginHasFailed = false;
  isPerformingLogin = false;
  nextDestination?: UrlTree;
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

  private handleLoginError(error: any) {
    this.isPerformingLogin = false;
    this.loginHasFailed = true;
    console.error(error);
  }

  onRegisterButtonClicked() {
    return this.router.navigate(['register']);
  }

  onRegisterInstitutionClicked() {
    return this.router.navigate(['institution/register']);
  }
}
