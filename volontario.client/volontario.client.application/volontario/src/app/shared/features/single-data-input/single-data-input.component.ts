import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-single-data-input',
  templateUrl: './single-data-input.component.html',
  styleUrls: ['./single-data-input.component.scss'],
})
export class SingleDataInputComponent implements OnInit {
  description: string = '';
  iconName: string = '';
  fieldName: string = '';
  hints: Map<string, string> = new Map<string, string>();
  labelName: string = '';
  formGroup: FormGroup;

  constructor(
    @Inject(MAT_DIALOG_DATA) private configuration: SingleDataInputConfig,
    private matDialogRef: MatDialogRef<SingleDataInputComponent>
  ) {
    this.formGroup = configuration.formGroup;

    this.throwErrorWhenFormGroupDoesNotHaveOneControl();

    this.description = configuration.description;
    this.iconName = configuration.iconName;
    this.hints = configuration.hintsMap;
    this.labelName = configuration.labelName;

    Object.keys(this.formGroup.controls).forEach(
      (key: string) => (this.fieldName = key)
    );
  }

  private throwErrorWhenFormGroupDoesNotHaveOneControl(): void {
    if (Object.keys(this.formGroup.controls).length !== 1) {
      throw new Error(
        'Form Group in SingleDataInputComponent must poses one form control.'
      );
    }
  }

  ngOnInit(): void {}

  onFormSubmit(): void {
    if (!this.isFormInvalid()) {
      this.matDialogRef.close(this.formGroup.controls[this.fieldName].value);
    }
  }

  isFormInvalid(): boolean {
    return this.formGroup.invalid;
  }

  resolveHint(): string {
    for (let [errorName, errorHint] of this.hints) {
      if (this.formGroup.controls[this.fieldName].hasError(errorName)) {
        return errorHint;
      }
    }

    return '';
  }
}

export interface SingleDataInputConfig {
  description: string;
  iconName: string;
  hintsMap: Map<string, string>;
  labelName: string;
  formGroup: FormGroup;
}
