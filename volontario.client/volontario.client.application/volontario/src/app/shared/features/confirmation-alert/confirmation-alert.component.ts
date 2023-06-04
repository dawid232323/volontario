import { Component, Inject, Input, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export class ConfirmationAlertInitialData {
  confirmationMessage?: string;
  confirmButtonLabel?: string;
  cancelButtonLabel?: string;
}

export enum ConfirmationAlertResult {
  Cancel,
  Accept,
}

@Component({
  selector: 'app-confirmation-alert',
  templateUrl: './confirmation-alert.component.html',
  styleUrls: ['./confirmation-alert.component.scss'],
})
export class ConfirmationAlertComponent {
  private _defaultConfirmationMessage = 'Czy na pewno chcesz wykonać tę akcję?';
  private _defaultConfirmButtonLabel = 'Tak';
  private _defaultCancelButtonLabel = 'Nie';

  private readonly _confirmationMessage: string;
  private readonly _confirmButtonLabel: string;
  private readonly _cancelButtonLabel: string;
  constructor(
    public dialogRef: MatDialogRef<ConfirmationAlertComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmationAlertInitialData
  ) {
    this._confirmationMessage =
      data.confirmationMessage || this._defaultConfirmationMessage;
    this._confirmButtonLabel =
      data.confirmButtonLabel || this._defaultConfirmButtonLabel;
    this._cancelButtonLabel =
      data.cancelButtonLabel || this._defaultCancelButtonLabel;
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

  public onConfirmButtonClicked() {
    this.dialogRef.close(ConfirmationAlertResult.Accept);
  }
}
