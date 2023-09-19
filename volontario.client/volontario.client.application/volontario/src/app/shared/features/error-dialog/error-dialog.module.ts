import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { ErrorDialogComponent } from './error-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [ErrorDialogComponent],
  imports: [CommonModule, MatDialogModule, NgOptimizedImage, MatButtonModule],
  exports: [ErrorDialogComponent],
})
export class ErrorDialogModule {}
