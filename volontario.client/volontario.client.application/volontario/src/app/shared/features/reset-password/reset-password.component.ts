import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../core/service/user.service';
import { SetPasswordInterface } from '../../../core/interface/authorization.interface';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss'],
})
export class ResetPasswordComponent implements OnInit {
  token: string = '';
  requestSent: boolean = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.token = this.activatedRoute.snapshot.queryParams['t'];
  }

  resetPassword(data: SetPasswordInterface): void {
    this.requestSent = true;
    this.userService
      .changeUserPasswordByToken(this.token, data.password)
      .subscribe({
        complete: () => {
          setTimeout(() => {
            this.router.navigate(['/login']);
            this.requestSent = false;
          }, 3000);
        },
      });
  }
}
