import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  Institution,
  InstitutionInterface,
} from 'src/app/core/model/institution.model';
import {
  arrayLengthValidator,
  hasMaxLengthError,
  hasRequiredError,
} from 'src/app/utils/validator.utils';
import { isNil } from 'lodash';
import { MatChipInputEvent } from '@angular/material/chips';
import { COMMA, ENTER } from '@angular/cdk/keycodes';

@Component({
  selector: 'app-institution-edit-form',
  templateUrl: './institution-edit-form.component.html',
  styleUrls: ['./institution-edit-form.component.scss'],
})
export class InstitutionEditFormComponent implements OnInit {
  @Output() formSubmit = new EventEmitter<Institution>();

  public institutionEditFormGroup: FormGroup;

  private _institutionData?: Institution;

  readonly separatorKeysCodes: number[] = [ENTER, COMMA];

  constructor(private formBuilder: FormBuilder) {
    this.institutionEditFormGroup = this.formBuilder.group({
      name: [null, [Validators.required, Validators.maxLength(100)]],
      krsNumber: [
        null,
        [Validators.maxLength(10), Validators.pattern('[0-9]*')],
      ],
      headquartersAddress: [
        null,
        [Validators.required, Validators.maxLength(100)],
      ],
      tags: [[], [arrayLengthValidator(10)]],
      description: [null, [Validators.required, Validators.maxLength(500)]],
      localization: [null, [Validators.required, Validators.maxLength(100)]],
    });
  }

  ngOnInit(): void {}

  @Input()
  public get institutionData(): InstitutionInterface | undefined {
    return this._institutionData;
  }

  public set institutionData(value: InstitutionInterface | undefined) {
    this._institutionData = value;
    if (isNil(this._institutionData)) {
      return;
    }
    this.institutionEditFormGroup.patchValue({
      name: this._institutionData.name,
      krsNumber: this._institutionData.krsNumber,
      headquartersAddress: this._institutionData.headquartersAddress,
      description: this._institutionData.description,
      localization: this._institutionData.localization,
    });
    this.institutionTags?.setValue([...this._institutionData!.tags!]);
    this.institutionTags?.updateValueAndValidity();
  }

  public hasRequiredError(formControlName: string): boolean {
    return hasRequiredError(
      this.institutionEditFormGroup.controls[formControlName]
    );
  }

  public hasMaxLengthError(formControlName: string): string | undefined {
    return hasMaxLengthError(
      this.institutionEditFormGroup.controls[formControlName]
    );
  }

  public get institutionTags() {
    return this.institutionEditFormGroup.get('tags');
  }

  public addTag(event: MatChipInputEvent) {
    const value = event.value;
    const input = event.input;
    if ((value || '').trim()) {
      this.institutionTags?.setValue([
        ...this.institutionTags?.value,
        value.trim(),
      ]);
      this.institutionTags?.updateValueAndValidity();
      if (input) {
        input.value = '';
      }
    }
  }

  public removeTag(tag: string) {
    const tagIndex = this.institutionTags?.value.indexOf(tag);

    if (tagIndex >= 0) {
      this.institutionTags?.value.splice(tagIndex, 1);
      this.institutionTags?.updateValueAndValidity();
    }
  }

  public onFormSubmit() {
    const finalOutput = {
      ...this._institutionData,
      ...this.institutionEditFormGroup.value,
    };
    if (this.institutionEditFormGroup.valid) {
      this.formSubmit.emit(finalOutput);
    }
  }

  protected readonly isNil = isNil;
}
