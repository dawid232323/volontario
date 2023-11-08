import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { isNil } from 'lodash';
import { UserService } from '../../core/service/user.service';
import { InfoCardTypeEnum } from '../../shared/features/success-info-card/info-card.component';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorDialogService } from 'src/app/core/service/error-dialog.service';

@Component({
  selector: 'app-volunteer-registration-confirmation',
  templateUrl: './volunteer-registration-confirmation.component.html',
  styleUrls: ['./volunteer-registration-confirmation.component.scss'],
})
export class VolunteerRegistrationConfirmationComponent implements OnInit {
  token: string;
  volunteerId: number;

  isRegistrationConfirmed: boolean | null;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router
  ) {
    this.token = this.resolveToken();
    this.volunteerId = this.resolveVolunteerId();
    this.isRegistrationConfirmed = null;
  }

  ngOnInit(): void {
    this.confirmRegistration();
  }

  resolveToken(): string {
    let optionalToken = this.route.snapshot.queryParamMap.get('t');

    if (!isNil(optionalToken)) {
      return optionalToken;
    }

    throw new Error('No token found in given route');
  }

  resolveVolunteerId(): number {
    let optionalVolunteerId = this.route.snapshot.paramMap.get('volunteer_id');

    if (!isNil(optionalVolunteerId) && !isNaN(+optionalVolunteerId)) {
      return +optionalVolunteerId;
    }

    throw new Error('No volunteer id found in given route');
  }

  confirmRegistration(): void {
    this.userService
      .confirmVolunteerRegistrationProcess(this.volunteerId, this.token)
      .subscribe({
        error: (error: HttpErrorResponse) => {
          this.isRegistrationConfirmed = error.status === 208;
        },
        complete: () => {
          this.isRegistrationConfirmed = true;
        },
      });
  }

  navigateToLogin(): void {
    this.router.navigate(['login']);
  }

  navigateToReportIssueForm(): void {
    this.router.navigate(['report-issue']);
  }

  protected readonly isNil = isNil;
  protected readonly InfoCardTypeEnum = InfoCardTypeEnum;
}
