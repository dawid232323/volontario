import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { VolunteerRegisterDTO } from 'src/app/core/model/volunteer.model';
import { SecurityService } from 'src/app/core/service/security/security.service';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  isPerformingRegistration: boolean = false;
  constructor(private authService: SecurityService, private router: Router) {}

  onRegisterFormSubmit(event: VolunteerRegisterDTO) {
    this.isPerformingRegistration = true;
    this.authService.registerVolunteer(event).subscribe({
      next: () => this.router.navigate(['login']),
      error: this.handleRegisterError.bind(this),
    });
  }

  private handleRegisterError(error: any) {
    this.isPerformingRegistration = false;
    console.error(error);
  }
}
