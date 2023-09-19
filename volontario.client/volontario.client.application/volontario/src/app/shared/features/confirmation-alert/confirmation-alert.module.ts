import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { ConfirmationAlertComponent } from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [ConfirmationAlertComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    FormsModule,
    NgOptimizedImage,
  ],
  exports: [ConfirmationAlertComponent],
})
export class ConfirmationAlertModule {}
