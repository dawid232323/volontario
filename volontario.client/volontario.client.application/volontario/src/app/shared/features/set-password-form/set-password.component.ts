import {
  Component,
  EventEmitter,
  Inject,
  OnInit,
  Optional,
  Output,
} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { SetPasswordInterface } from 'src/app/core/interface/authorization.interface';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { isNil } from 'lodash';

enum ViewMode {
  Standard,
  ModalWindow,
}

@Component({
  selector: 'app-set-password-component',
  templateUrl: './set-password.component.html',
  styleUrls: ['./set-password.component.scss'],
})
export class SetPasswordComponent implements OnInit {
  public registerContactPersonFormGroup = new FormGroup<any>({});
  private _isPasswordShown: boolean = false;
  private _viewMode: ViewMode = ViewMode.Standard;

  @Output() formSubmitEvent = new EventEmitter<SetPasswordInterface>();
  constructor(
    private formBuilder: FormBuilder,
    @Optional() private dialogRef?: MatDialogRef<SetPasswordComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) private dialogData?: any
  ) {}

  ngOnInit(): void {
    this.setFormGroup();
    this.determineViewMode();
  }

  public get isPasswordShown(): boolean {
    return this._isPasswordShown;
  }

  public set isPasswordShown(isShown: boolean) {
    this._isPasswordShown = isShown;
  }

  public get viewMode(): ViewMode {
    return this._viewMode;
  }

  public get passwordInputType(): string {
    if (this.isPasswordShown) {
      return 'text';
    }
    return 'password';
  }

  public get submitButtonLabel(): string {
    if (this._viewMode === ViewMode.Standard) {
      return 'Zarejestruj';
    }
    return 'Resetuj';
  }

  public get passwordValue(): string {
    return this.registerContactPersonFormGroup.value.password;
  }

  public get repeatPasswordValue(): string {
    return this.registerContactPersonFormGroup.value.repeatPassword;
  }

  public onFormSubmit() {
    const { password } = this.registerContactPersonFormGroup.value;
    const result: SetPasswordInterface = { password: password };
    if (this._viewMode === ViewMode.Standard) {
      this.formSubmitEvent.emit(result);
    } else {
      this.dialogRef?.close(result);
    }
  }

  private setFormGroup() {
    this.registerContactPersonFormGroup = this.formBuilder.group({
      password: [
        null,
        [
          Validators.required,
          Validators.pattern('.*[a-z].*'),
          Validators.pattern('.*[A-Z].*'),
          Validators.pattern('.*[0-9].*'),
          Validators.pattern('.*[!@#$%^&*()_+].*'),
        ],
      ],
      repeatPassword: [
        null,
        [Validators.required, this.equalPasswordValidator],
      ],
    });
    this.registerContactPersonFormGroup.addValidators(
      this.equalPasswordValidator
    );
  }

  private determineViewMode() {
    if (!isNil(this.dialogRef)) {
      this._viewMode = ViewMode.ModalWindow;
    }
  }

  private equalPasswordValidator: ValidatorFn = (
    group: AbstractControl
  ): ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('repeatPassword')?.value;
    return pass === confirmPass ? null : { notSame: true };
  };
  protected readonly ViewMode = ViewMode;
}
