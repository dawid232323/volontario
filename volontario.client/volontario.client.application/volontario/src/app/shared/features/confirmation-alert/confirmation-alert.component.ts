import { Component, Inject, Input, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatInput } from '@angular/material/input';
import { isNil } from 'lodash';

export class ConfirmationAlertInitialData {
  confirmationMessage?: string;
  confirmButtonLabel?: string;
  cancelButtonLabel?: string;
  shouldAskForReason?: boolean;
}

export enum ConfirmationAlertResult {
  Cancel,
  Accept,
}

export interface ConfirmationAlertResultIf {
  confirmationAlertResult: ConfirmationAlertResult;
  resultReason?: string;
}

@Component({
  selector: 'app-confirmation-alert',
  templateUrl: './confirmation-alert.component.html',
  styleUrls: ['./confirmation-alert.component.scss'],
})
export class ConfirmationAlertComponent {
  public confirmationReason?: string;

  private _defaultConfirmationMessage = 'Czy na pewno chcesz wykonać tę akcję?';
  private _defaultConfirmButtonLabel = 'Tak';
  private _defaultCancelButtonLabel = 'Nie';

  private readonly _confirmationMessage: string;
  private readonly _confirmButtonLabel: string;
  private readonly _cancelButtonLabel: string;
  private readonly _shouldAskForReason: boolean;
  constructor(public dialogRef: MatDialogRef<ConfirmationAlertComponent>, @Inject(MAT_DIALOG_DATA) public data: ConfirmationAlertInitialData) {
    this._confirmationMessage = data?.confirmationMessage || this._defaultConfirmationMessage;
    this._confirmButtonLabel = data?.confirmButtonLabel || this._defaultConfirmButtonLabel;
    this._cancelButtonLabel = data?.cancelButtonLabel || this._defaultCancelButtonLabel;
    this._shouldAskForReason = data?.shouldAskForReason || false;
  }

  get confirmationMessage(): string {
    return this._confirmationMessage;
  }

  get confirmButtonLabel(): string {
    return this._confirmButtonLabel;
  }

  get cancelButtonLabel(): string {
    return this._cancelButtonLabel;
  }

  get shouldAskForReason(): boolean {
    return this._shouldAskForReason;
  }

  public isDialogValid(): boolean {
    if (!this.shouldAskForReason) {
      return true;
    }
    return !isNil(this.confirmationReason) && this.confirmationReason !== '';
  }

  public onConfirmButtonClicked() {
    if (!this.isDialogValid()) {
      return;
    }
    const result: ConfirmationAlertResultIf = { confirmationAlertResult: ConfirmationAlertResult.Accept };
    if (this._shouldAskForReason) {
      result.resultReason = this.confirmationReason;
    }
    this.dialogRef.close(result);
  }
}
