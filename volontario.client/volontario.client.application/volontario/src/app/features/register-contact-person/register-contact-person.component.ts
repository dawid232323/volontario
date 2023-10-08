import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SetPasswordInterface } from 'src/app/core/interface/authorization.interface';
import { InstitutionService } from 'src/app/core/service/institution.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

export enum EmployeeRegistrationModeEnum {
  RegisterContactPerson,
  RegisterEmployee,
}

@Component({
  selector: 'app-register-contact-person',
  templateUrl: './register-contact-person.component.html',
  styleUrls: ['./register-contact-person.component.scss'],
})
export class RegisterContactPersonComponent implements OnInit {
  private token: string = '';
  private _hasRegisteredCorrectly = false;
  private _isPerformingRegistration = false;
  private readonly _workingMode: EmployeeRegistrationModeEnum;
  private readonly _institutionId?: number;
  constructor(
    private activatedRoute: ActivatedRoute,
    private institutionService: InstitutionService,
    private router: Router
  ) {
    this._workingMode = <EmployeeRegistrationModeEnum>(
      this.activatedRoute.snapshot.data['mode']
    );
    if (this._workingMode === EmployeeRegistrationModeEnum.RegisterEmployee) {
      this._institutionId =
        this.activatedRoute.snapshot.params['institution_id'];
    }
  }

  ngOnInit(): void {
    this.token = this.activatedRoute.snapshot.queryParams['t'];
  }

  public registerUser(registerIf: SetPasswordInterface) {
    this._isPerformingRegistration = true;
    this.getSubmitObservable(registerIf)?.subscribe({
      next: this.onSubmitSuccess.bind(this),
      error: this.onSubmitError.bind(this),
    });
  }

  private getSubmitObservable(
    registerIf: SetPasswordInterface
  ): Observable<any> | null {
    if (
      this._workingMode === EmployeeRegistrationModeEnum.RegisterContactPerson
    ) {
      return this.institutionService.registerContactPerson(
        this.token,
        registerIf
      );
    } else if (
      this._workingMode === EmployeeRegistrationModeEnum.RegisterEmployee
    ) {
      return this.institutionService.setNewEmployeePassword(
        this._institutionId!,
        this.token,
        registerIf
      );
    }
    return null;
  }

  private onSubmitSuccess() {
    this._hasRegisteredCorrectly = true;
    this._isPerformingRegistration = false;
  }

  private onSubmitError(error: HttpErrorResponse) {
    this._isPerformingRegistration = false;
    console.log(error);
    alert(error);
    if (error.status === 207) {
      this._hasRegisteredCorrectly = true;
    }
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
