import { Injectable } from '@angular/core';
import { UserService } from 'src/app/core/service/user.service';
import {
  UserAdministrativeOperationDetailsIf,
  UserAdministrativeOperationPerformerInterface,
  UserAdministrativeOperationType,
} from 'src/app/features/admin-users-management/utils/userAdministrativeOperations.interface';
import { Observable, of } from 'rxjs';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { AdministrativeUserDetails } from 'src/app/core/model/user.model';
import {
  ConfirmationAlertComponent,
  ConfirmationAlertInitialData,
  ConfirmationAlertResult,
  ConfirmationAlertResultIf,
} from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';
import {
  RoleChangeDialogComponent,
  RoleChangeDialogDataIf,
} from 'src/app/features/admin-users-management/_features/role-change-dialog/role-change-dialog.component';
import { isNil } from 'lodash';
import { SetPasswordComponent } from 'src/app/shared/features/set-password-form/set-password.component';
import { SetPasswordInterface } from 'src/app/core/interface/authorization.interface';

@Injectable()
export class UserAdministrativeOperationsPerformerFactory {
  constructor(private userService: UserService, private dialog: MatDialog) {}

  public getOperationPerformer(operationDetails: UserAdministrativeOperationDetailsIf): UserAdministrativeOperationPerformerInterface | undefined {
    switch (operationDetails.operationType) {
      case UserAdministrativeOperationType.ChangeActivityStatus:
        return new UserActivityStatusChangePerformer(this.userService, this.dialog);
      case UserAdministrativeOperationType.ChangeRoles:
        return new UserRoleChangePerformer(this.userService, this.dialog);
      case UserAdministrativeOperationType.ResetPassword:
        return new UserPasswordChangePerformer(this.userService, this.dialog);
      default:
        return undefined;
    }
  }
}

class UserRoleChangePerformer implements UserAdministrativeOperationPerformerInterface {
  userService: UserService;
  dialog: MatDialog;

  constructor(userService: UserService, dialog: MatDialog) {
    this.userService = userService;
    this.dialog = dialog;
  }

  getDialogRef(operationDetails: UserAdministrativeOperationDetailsIf): MatDialogRef<any> {
    const roleIds = operationDetails.userDetails.userRoles.map(role => role.id);
    const initialData: RoleChangeDialogDataIf = { selectedRoles: roleIds };
    return this.dialog.open(RoleChangeDialogComponent, {
      data: initialData,
    });
  }

  getOperationObservable(dialogResult: RoleChangeDialogDataIf, operationDetails: UserAdministrativeOperationDetailsIf): Observable<any> | undefined {
    if (isNil(dialogResult)) {
      return undefined;
    }
    return this.userService.changeUserRoles(operationDetails.userDetails.userId, dialogResult.selectedRoles);
  }
}

class UserPasswordChangePerformer implements UserAdministrativeOperationPerformerInterface {
  userService: UserService;
  dialog: MatDialog;

  constructor(userService: UserService, dialog: MatDialog) {
    this.userService = userService;
    this.dialog = dialog;
  }

  getDialogRef(operationDetails: UserAdministrativeOperationDetailsIf): MatDialogRef<any> | undefined {
    return this.dialog.open(SetPasswordComponent);
  }

  getOperationObservable(dialogResult: SetPasswordInterface, operationDetails: UserAdministrativeOperationDetailsIf): Observable<any> | undefined {
    if (isNil(dialogResult) || dialogResult?.password === '' || isNil(dialogResult.password)) {
      return undefined;
    }
    return this.userService.changeUserPassword(operationDetails.userDetails.userId, dialogResult.password);
  }
}

class UserActivityStatusChangePerformer implements UserAdministrativeOperationPerformerInterface {
  userService: UserService;
  dialog: MatDialog;

  constructor(userService: UserService, dialog: MatDialog) {
    this.userService = userService;
    this.dialog = dialog;
  }

  getDialogRef(operationDetails: UserAdministrativeOperationDetailsIf): MatDialogRef<any> | undefined {
    const newActivityStatus = this.getNewUserActivityStatus(operationDetails.userDetails);
    const dialogInitialData: ConfirmationAlertInitialData = {
      confirmationMessage: this.getConfirmationMessage(newActivityStatus),
    };
    const dialogRef = this.dialog.open(ConfirmationAlertComponent, {
      data: dialogInitialData,
    });
    return dialogRef;
  }

  getOperationObservable(
    dialogResult: ConfirmationAlertResultIf,
    operationDetails: UserAdministrativeOperationDetailsIf
  ): Observable<any> | undefined {
    if (dialogResult.confirmationAlertResult !== ConfirmationAlertResult.Accept) {
      return undefined;
    }
    const newActivityStatus = this.getNewUserActivityStatus(operationDetails.userDetails);
    return this.userService.changeUserActivityStatus(operationDetails.userDetails.userId, newActivityStatus);
  }

  private getNewUserActivityStatus(userDetails: AdministrativeUserDetails): boolean {
    return userDetails.verified;
  }

  private getConfirmationMessage(willUserBeDisabled: boolean): string {
    if (willUserBeDisabled) {
      return 'Zablokowanie użytkownika odbierze mu możliwość wykonywania jakichkolwiek operacji w systemie włącznie z logowaniem. Jest to operacja odwracalna';
    }
    return 'Operacja spowoduje że użytkownik ponownie będzie mógł wykonywać wszystkie operacje powiązane z jego rolą';
  }
}
