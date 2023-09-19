import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ErrorDialogComponent, ErrorDialogInitialData } from 'src/app/shared/features/error-dialog/error-dialog.component';

@Injectable({ providedIn: 'root' })
export class ErrorDialogService {
  constructor(private dialog: MatDialog) {}

  public openDefaultErrorDialog(initialData: ErrorDialogInitialData): MatDialogRef<ErrorDialogComponent> {
    return this.dialog.open(ErrorDialogComponent, { data: initialData });
  }
}
