import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { EvaluationModalComponent } from 'src/app/shared/features/evaluation-modal/evaluation-modal.component';
import { MatDialogModule } from '@angular/material/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [EvaluationModalComponent],
  imports: [
    CommonModule,
    NgOptimizedImage,
    MatDialogModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
  ],
  exports: [EvaluationModalComponent],
})
export class EvaluationModalModule {}
