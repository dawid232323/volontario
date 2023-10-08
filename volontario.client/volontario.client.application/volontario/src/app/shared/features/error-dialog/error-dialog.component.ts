import { Component, Inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { isNil } from 'lodash';

export interface ErrorDialogInitialData {
  error: Error;
  dialogTitle?: string;
  dialogMessage?: string;
}

@Component({
  selector: 'app-error-dialog',
  templateUrl: './error-dialog.component.html',
  styleUrls: ['./error-dialog.component.scss'],
})
export class ErrorDialogComponent {
  private readonly _dialogTitle: string;
  private readonly _dialogMessage: string;
  private readonly _statusCode?: number;
  private readonly _errorMessage: string;
  private readonly _errorBody?: any;
  constructor(
    @Inject(MAT_DIALOG_DATA) private initialData: ErrorDialogInitialData
  ) {
    this._dialogTitle =
      initialData.dialogTitle || 'Wystąpił nieoczekiwany błąd';
    this._dialogMessage =
      initialData.dialogMessage ||
      'Spróbuj jeszcze raz lub skorzystaj z formularza zgłaszania błędów';
    this._errorMessage = initialData.error.message;
    if (initialData.error instanceof HttpErrorResponse) {
      this._statusCode = initialData.error.status;
      this._errorBody = initialData.error.error;
    }
  }

  public get dialogTitle(): string {
    return this._dialogTitle;
  }

  public get errorMessage(): string {
    return this._errorMessage;
  }

  public get dialogMessage(): string {
    return this._dialogMessage;
  }

  public get statusCode(): number | undefined {
    return this._statusCode;
  }

  public get errorBody(): string | undefined {
    return this._errorBody;
  }

  protected readonly isNil = isNil;
}
