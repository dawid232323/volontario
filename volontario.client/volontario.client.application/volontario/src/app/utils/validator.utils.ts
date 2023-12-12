import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { isNil } from 'lodash';
import * as moment from 'moment';
import { Moment } from 'moment';

export enum DateValidatorUsageEnum {
  Before = 'dateBefore',
  After = 'dateAfter',
}

export function dateBeforeAfterValidator(
  usage: DateValidatorUsageEnum,
  masterFieldName: string,
  dependentFieldName: string
): ValidatorFn {
  return (form: AbstractControl): ValidationErrors | null => {
    const dateFromField = form.get(masterFieldName)?.value;
    const dateToFormField = form.get(dependentFieldName)?.value;
    if (isNil(dateFromField) || isNil(dateToFormField)) {
      return null;
    }
    const dateFromMoment = moment(dateFromField);
    const dateToMoment = moment(dateToFormField);
    if (usage === DateValidatorUsageEnum.Before) {
      return dateBeforeValidator(dateFromMoment, dateToMoment);
    }
    return dateAfterValidator(dateFromMoment, dateToMoment);
  };
}

function dateBeforeValidator(
  masterDate: Moment,
  dependentDate: Moment
): ValidationErrors | null {
  if (masterDate.isSameOrBefore(dependentDate)) {
    return null;
  }
  return { [DateValidatorUsageEnum.Before]: true };
}

function dateAfterValidator(
  masterDate: Moment,
  dependentDate: Moment
): ValidationErrors | null {
  if (masterDate.isSameOrAfter(dependentDate)) {
    return null;
  }
  return { [DateValidatorUsageEnum.After]: true };
}

export function arrayLengthValidator(arrayMaxLength: number) {
  return (tagsInput: FormControl): ValidationErrors | null => {
    if ((<string[]>tagsInput.value).length > arrayMaxLength) {
      return { valid: false };
    }
    return null;
  };
}

export function phoneNumberValidator(): ValidatorFn {
  return (formControl: AbstractControl): ValidationErrors | null => {
    if (isNil(formControl.value)) {
      return null;
    }
    return Validators.pattern('[1-9][0-9]{8}')(formControl);
  };
}

/**
 * Checks if given form control has max length error.
 *
 * @param formControl form control to be checked.
 *
 * @returns undefined if given control doesn't have max length error or required field length
 */
export function hasMaxLengthError(
  formControl: AbstractControl
): string | undefined {
  if (isNil(formControl.errors?.['maxlength'])) {
    return undefined;
  }
  return formControl.errors?.['maxlength']?.['requiredLength'];
}

/**
 * Checks if given form control has required error
 *
 * @param formControl form control to be checked.
 *
 * @returns boolean value that determines if form control has required error.
 */
export function hasRequiredError(formControl: AbstractControl): boolean {
  return !isNil(formControl.errors?.['required']);
}

export function countCharacters(
  controlName: string,
  formGroup: FormGroup,
  maxCharacters: number
): string {
  const controlValueLength: number = isNil(
    formGroup.controls[controlName].value
  )
    ? 0
    : formGroup.controls[controlName].value.length;

  if (controlValueLength === 0) {
    return '';
  }

  return '(' + controlValueLength + '/' + maxCharacters + ')';
}
