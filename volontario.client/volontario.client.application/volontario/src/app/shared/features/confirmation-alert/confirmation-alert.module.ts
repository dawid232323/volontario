import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmationAlertComponent } from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [ConfirmationAlertComponent],
  imports: [CommonModule, MatButtonModule, MatDialogModule],
  exports: [ConfirmationAlertComponent],
})
export class ConfirmationAlertModule {}
