import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RegisterContactPersonInterface } from 'src/app/core/interface/authorization.interface';
import { InstitutionService } from 'src/app/core/service/institution.service';

@Component({
  selector: 'app-register-contact-person',
  templateUrl: './register-contact-person.component.html',
  styleUrls: ['./register-contact-person.component.scss'],
})
export class RegisterContactPersonComponent implements OnInit {
  private token: string = '';
  private _hasRegisteredCorrectly = false;
  private _isPerformingRegistration = false;
  constructor(
    private activatedRoute: ActivatedRoute,
    private institutionService: InstitutionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.token = this.activatedRoute.snapshot.queryParams['t'];
  }

  public registerUser(registerIf: RegisterContactPersonInterface) {
    this._isPerformingRegistration = true;
    this.institutionService
      .registerContactPerson(this.token, registerIf)
      .subscribe({
        next: () => {
          this._hasRegisteredCorrectly = true;
          this._isPerformingRegistration = false;
        },
        error: err => {
          this._isPerformingRegistration = false;
          console.log(err);
          alert(err);
        },
      });
  }

  public onSuccessCardButtonClicked() {
    this.router.navigate(['/login']);
  }

  public get hasRegisteredCorrectly() {
    return this._hasRegisteredCorrectly;
  }

  public get isPerformingRegistration() {
    return this._isPerformingRegistration;
  }
}
