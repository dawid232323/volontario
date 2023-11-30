import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EvaluationComponent } from 'src/app/shared/features/evaluation/evaluation.component';
import { EvaluationCardModule } from 'src/app/shared/features/evaluation-card/evaluation-card.module';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';

@NgModule({
  declarations: [EvaluationComponent],
  imports: [
    CommonModule,
    EvaluationCardModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
  ],
  exports: [EvaluationComponent],
})
export class UserEvaluationModule {}
