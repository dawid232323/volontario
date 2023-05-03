import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
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
  if (masterDate.isBefore(dependentDate)) {
    return null;
  }
  return { [DateValidatorUsageEnum.Before]: true };
}

function dateAfterValidator(
  masterDate: Moment,
  dependentDate: Moment
): ValidationErrors | null {
  if (masterDate.isAfter(dependentDate)) {
    return null;
  }
  return { [DateValidatorUsageEnum.After]: true };
}
