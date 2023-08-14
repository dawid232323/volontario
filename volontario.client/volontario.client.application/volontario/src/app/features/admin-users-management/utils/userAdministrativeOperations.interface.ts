import { UserService } from 'src/app/core/service/user.service';
import { Observable } from 'rxjs';
import { AdministrativeUserDetails } from 'src/app/core/model/user.model';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

export enum UserAdministrativeOperationType {
  ResetPassword,
  ChangeRoles,
  ChangeActivityStatus,
}

export interface UserAdministrativeOperationDetailsIf {
  operationType: UserAdministrativeOperationType;
  userDetails: AdministrativeUserDetails;
}

export interface UserAdministrativeOperationPerformerInterface {
  userService: UserService;
  dialog: MatDialog;
  getDialogRef(
    operationDetails: UserAdministrativeOperationDetailsIf
  ): MatDialogRef<any> | undefined;
  getOperationObservable(
    dialogResult: any,
    operationDetails: UserAdministrativeOperationDetailsIf
  ): Observable<any> | undefined;
}
