import { DictionaryValuesServiceInterface } from 'src/app/core/interface/dictionary-values-service.interface';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import {
  DictValueOperationInterface,
  DictValueOperationTypeEnum,
} from 'src/app/features/manage-dict-values/_features/dict-values-list/dict-values-list.component';
import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';
import { ConfirmationAlertResult } from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';

export interface DictValuesOperationPerformerInterface<
  T extends DictionaryValueInterface
> {
  performerService: DictionaryValuesServiceInterface<T>;
  dialog: MatDialog;
  getDialogRef(
    operationDetails: DictValueOperationInterface
  ): MatDialogRef<any> | undefined;
  getOperationObservable(
    modalResult: T,
    operationType: DictValueOperationTypeEnum,
    confirmationResult?: ConfirmationAlertResult
  ): Observable<T> | undefined;
}
