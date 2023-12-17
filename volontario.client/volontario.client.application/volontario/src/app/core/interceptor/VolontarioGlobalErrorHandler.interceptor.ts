import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { ErrorDialogService } from 'src/app/core/service/error-dialog.service';
import { ErrorDialogInitialData } from 'src/app/shared/features/error-dialog/error-dialog.component';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class VolontarioGlobalErrorHandlerInterceptor implements ErrorHandler {
  constructor(private errorService: ErrorDialogService, private zone: NgZone) {}

  handleError(error: any): void {
    console.error(error);
    this.zone.run(() => {
      if (error instanceof HttpErrorResponse && error.status === 401) {
        return;
      }
      // Endpoint userData throws unrecognised and not deterministic content not supported error which in fact didn't affect anything
      // so to not spoil user experience we made the decision to handle it temporary this way.
      if (error instanceof HttpErrorResponse && error.status === 415) {
        return;
      }
      const initialData: ErrorDialogInitialData = {
        error: error,
      };
      this.errorService.openDefaultErrorDialog(initialData);
    });
  }
}
