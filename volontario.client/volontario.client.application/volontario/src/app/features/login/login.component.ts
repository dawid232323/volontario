import { Component, OnInit } from '@angular/core';
import { SecurityService } from '../../core/service/security/security.service';
import { LoginInterface } from '../../core/interface/authorization.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginHasFailed = false;
  isPerformingLogin = false;
  constructor(private authService: SecurityService, private router: Router) {}

  ngOnInit(): void {}

  onLoginFormSubmit(event: LoginInterface) {
    this.isPerformingLogin = true;
    this.authService.login(event).subscribe({
      next: () => this.router.navigate(['home']),
      error: this.handleLoginError.bind(this),
    });
  }

  private handleLoginError(error: any) {
    this.isPerformingLogin = false;
    this.loginHasFailed = true;
    console.error(error);
  }

  onRegisterButtonClicked() {
    this.router.navigate(['register']);
  }
}
