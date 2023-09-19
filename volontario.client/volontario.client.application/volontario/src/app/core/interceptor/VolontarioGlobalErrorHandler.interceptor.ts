import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { ErrorDialogService } from 'src/app/core/service/error-dialog.service';
import { ErrorDialogInitialData } from 'src/app/shared/features/error-dialog/error-dialog.component';

@Injectable()
export class VolontarioGlobalErrorHandlerInterceptor implements ErrorHandler {
  constructor(private errorService: ErrorDialogService, private zone: NgZone) {}

  handleError(error: any): void {
    console.error(error);
    this.zone.run(() => {
      const initialData: ErrorDialogInitialData = {
        error: error,
      };
      this.errorService.openDefaultErrorDialog(initialData);
    });
  }
}
